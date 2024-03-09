package org.gecko.io;

import gecko.parser.SystemDefLexer;
import gecko.parser.SystemDefParser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Alert;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.gecko.exceptions.ModelException;
import org.gecko.model.GeckoModel;
import org.gecko.util.graphlayouting.Graphlayouter;
import org.gecko.viewmodel.GeckoViewModel;

/**
 * The AutomatonFileParser is used to import a project from a sys file. It is responsible for parsing a sys file and
 * creating a {@link GeckoViewModel} from it. It uses the {@link AutomatonFileVisitor} the file into a
 * {@link GeckoModel}. And then uses the {@link ViewModelElementCreator} to create the view model from the model.
 */
public class AutomatonFileParser implements FileParser {
    @Override
    public GeckoViewModel parse(File file) throws IOException {
        CharStream stream = CharStreams.fromFileName(file.getAbsolutePath());
        SystemDefParser parser = new SystemDefParser(new CommonTokenStream(new SystemDefLexer(stream)));
        SyntaxErrorListener listener = new SyntaxErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        AutomatonFileVisitor visitor;

        try {
            visitor = new AutomatonFileVisitor();
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create an instance of an empty model");
        }

        try {
            visitor.visitModel(parser.model());
        } catch (RuntimeException e) {
            throw new IOException(e.getMessage());
        }
        if (listener.syntaxError) {
            throw new IOException(listener.errorMessage);
        }
        if (!visitor.getWarnings().isEmpty()) {
            showWarnings(visitor.getWarnings());
        }

        GeckoViewModel gvm = new GeckoViewModel(visitor.getModel());
        ViewModelElementCreator vmVisitor = new ViewModelElementCreator(gvm, List.of(), List.of());
        vmVisitor.traverseModel(gvm.getGeckoModel().getRoot());
        Graphlayouter graphlayouter = new Graphlayouter(gvm);
        graphlayouter.layout();
        return gvm;
    }

    private void showWarnings(Set<String> warnings) {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Warning");
        warningAlert.setHeaderText("The model has been successfully parsed, but warnings have been emitted");
        warningAlert.setContentText(String.join(System.lineSeparator(), warnings));
        warningAlert.showAndWait();
    }

    private static class SyntaxErrorListener extends BaseErrorListener {
        private boolean syntaxError;
        private String errorMessage;

        private SyntaxErrorListener() {
            syntaxError = false;
        }

        @Override
        public void syntaxError(
            org.antlr.v4.runtime.Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String msg, org.antlr.v4.runtime.RecognitionException e) {
            if (errorMessage == null) {
                errorMessage = msg + " at line " + line + ":" + charPositionInLine;
            }
            syntaxError = true;
        }
    }
}

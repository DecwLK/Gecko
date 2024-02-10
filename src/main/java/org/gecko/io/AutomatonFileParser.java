package org.gecko.io;

import gecko.parser.SystemDefLexer;
import gecko.parser.SystemDefParser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.gecko.exceptions.ModelException;
import org.gecko.util.graphlayouting.Graphlayouter;
import org.gecko.viewmodel.GeckoViewModel;

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

        String result = visitor.visitModel(parser.model());
        if (result != null) {
            throw new IOException(result);
        }
        if (listener.syntaxError) {
            throw new IOException(listener.errorMessage);
        }

        GeckoViewModel gvm = new GeckoViewModel(visitor.getModel());
        ViewModelElementCreator vmVisitor = new ViewModelElementCreator(gvm, List.of());
        vmVisitor.traverseModel(gvm.getGeckoModel().getRoot());
        Graphlayouter graphlayouter = new Graphlayouter(gvm);
        graphlayouter.layout();
        return gvm;
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
                errorMessage = msg;
            }
            syntaxError = true;
        }
    }
}

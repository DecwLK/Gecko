package org.gecko.io;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AutomatonFileParserTest {
    static AutomatonFileParser automatonFileParser;

    @BeforeAll
    static void setUp() {
        automatonFileParser = new AutomatonFileParser();
    }

    @Test
    void parseOneRoot() {
        // Importing a file with multiple top level systems can only be resolved in the view.
        File aebFile = new File("src/test/java/org/gecko/io/files/AEB.sys");
        try {
            automatonFileParser.parse(aebFile);
        } catch (IOException e) {
            fail("View model could not be extracted from automaton file with one root.");
        }
    }

    @Test
    void parseComplexGecko() {
        File serializedExportedComplexFile = new File("src/test/java/org/gecko/io/files/exportedComplexGecko.sys");
        try {
            automatonFileParser.parse(serializedExportedComplexFile);
        } catch (IOException e) {
            fail("View model could not be extracted from automaton file.");
        }
    }
}

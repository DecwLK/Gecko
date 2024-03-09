package org.gecko.io;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.gecko.viewmodel.GeckoViewModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProjectFileParserTest {
    static ProjectFileParser projectFileParser;
    private static ObjectMapper mapper;
    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        projectFileParser = new ProjectFileParser();
    }

    @Test
    void parse() {
        GeckoViewModel parsedEmptyGeckoViewModel = null;
        File fileForEmpty = new File("src/test/java/org/gecko/io/files/emptyGecko.json");
        File serializedParsedEmpty
            = new File("src/test/java/org/gecko/io/files/serializedParsedEmptyGecko.json");

        try {
            parsedEmptyGeckoViewModel = projectFileParser.parse(fileForEmpty);
        } catch (IOException e) {
            fail("Empty Gecko could not be parsed from file.");
        }
        assertNotNull(parsedEmptyGeckoViewModel);

        ProjectFileSerializer serializer = new ProjectFileSerializer(parsedEmptyGeckoViewModel);
        assertDoesNotThrow(() -> serializer.writeToFile(serializedParsedEmpty));
        assertDoesNotThrow(() -> assertEquals(mapper.readTree(fileForEmpty), mapper.readTree(serializedParsedEmpty)));
    }

    @Test
    void parseOneLevel() {
        GeckoViewModel parsedOneLevelGeckoViewModel = null;
        File fileForOneLevel = new File("src/test/java/org/gecko/io/files/oneLevelGecko.json");
        File serializedParsedOneLevel
            = new File("src/test/java/org/gecko/io/files/serializedParsedOneLevelGecko.json");
        try {
            parsedOneLevelGeckoViewModel = projectFileParser.parse(fileForOneLevel);
        } catch (IOException e) {
            fail("One-level Gecko could not be parsed from file.");
        }
        assertNotNull(parsedOneLevelGeckoViewModel);

        ProjectFileSerializer serializer = new ProjectFileSerializer(parsedOneLevelGeckoViewModel);
        assertDoesNotThrow(() -> serializer.writeToFile(serializedParsedOneLevel));

        Scanner scanner1 = null;
        Scanner scanner2 = null;
        try {
            scanner1 = new Scanner(fileForOneLevel);
            scanner1.useDelimiter("\"viewModelProperties\"");

            scanner2 = new Scanner(serializedParsedOneLevel);
            scanner2.useDelimiter("\"viewModelProperties\"");
        } catch (FileNotFoundException e) {
            fail("File to scan was not found.");
        }

        String parsedModel = scanner1.next();
        String serializedParsedModel = scanner2.next();
        assertEquals(parsedModel, serializedParsedModel);
    }

    @Test
    void parseTree() {
        GeckoViewModel parsedTreeGeckoViewModel = null;
        File fileForTree = new File("src/test/java/org/gecko/io/files/treeGecko.json");
        File serializedParsedTree
            = new File("src/test/java/org/gecko/io/files/serializedParsedTreeGecko.json");
        try {
            parsedTreeGeckoViewModel = projectFileParser.parse(fileForTree);
        } catch (IOException e) {
            fail("Tree-structured Gecko could not be parsed from file.");
        }
        assertNotNull(parsedTreeGeckoViewModel);

        ProjectFileSerializer serializer = new ProjectFileSerializer(parsedTreeGeckoViewModel);
        assertDoesNotThrow(() -> serializer.writeToFile(serializedParsedTree));

        Scanner scanner1 = null;
        Scanner scanner2 = null;
        try {
            scanner1 = new Scanner(fileForTree);
            scanner1.useDelimiter("\"viewModelProperties\"");

            scanner2 = new Scanner(serializedParsedTree);
            scanner2.useDelimiter("\"viewModelProperties\"");
        } catch (FileNotFoundException e) {
            fail("File to scan was not found.");
        }

        String parsedModel = scanner1.next();
        String serializedParsedModel = scanner2.next();
        assertEquals(parsedModel, serializedParsedModel);
    }

    @Test
    void parseFileThatContainsANonexistentStartState() {
        File fileForNonexistentStartState = new File("src/test/java/org/gecko/io/files/nonexistentStartState.json");
        assertThrows(IOException.class, () -> projectFileParser.parse(fileForNonexistentStartState));
    }

    @Test
    void parseFileWithValidStartStates() {
        File fileForNonexistentStartState = new File("src/test/java/org/gecko/io/files/existentStartState.json");
        assertDoesNotThrow(() -> projectFileParser.parse(fileForNonexistentStartState));
    }
}

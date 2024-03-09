package org.gecko.io;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import org.gecko.viewmodel.GeckoViewModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AutomatonFileSerializerTest {
    static GeckoViewModel geckoViewModel;

    @BeforeAll
    static void setUp() {
        File aebFile = new File("src/test/java/org/gecko/io/files/AEB.sys");
        AutomatonFileParser automatonFileParser = new AutomatonFileParser();
        geckoViewModel = null;
        try {
            geckoViewModel = automatonFileParser.parse(aebFile);
        } catch (IOException e) {
            fail("View model could not be extracted from basic automaton file with one root.");
        }
    }

    @Test
    void writeToFile() {
        File serializedParsedAEBFile = new File("src/test/java/org/gecko/io/files/serializedParsedAEB.sys");
        AutomatonFileSerializer automatonFileSerializer = new AutomatonFileSerializer(geckoViewModel.getGeckoModel());
        assertDoesNotThrow(() -> automatonFileSerializer.writeToFile(serializedParsedAEBFile));
    }

    @Test
    void parseComplexGecko() {
        ProjectFileParser projectFileParser = new ProjectFileParser();
        File complexGeckoFile = new File("src/test/java/org/gecko/io/files/complexGecko.json");
        GeckoViewModel complexViewModel = null;
        try {
            complexViewModel = projectFileParser.parse(complexGeckoFile);
        } catch (IOException e) {
            fail("Project file could not be parsed.");
        }

        File serializedExportedComplexFile = new File("src/test/java/org/gecko/io/files/exportedComplexGecko.sys");
        AutomatonFileSerializer automatonFileSerializer = new AutomatonFileSerializer(complexViewModel.getGeckoModel());
        assertDoesNotThrow(() -> automatonFileSerializer.writeToFile(serializedExportedComplexFile));
    }
}

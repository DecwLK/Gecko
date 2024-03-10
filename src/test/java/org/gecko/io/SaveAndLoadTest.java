package org.gecko.io;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.gecko.exceptions.ModelException;
import org.gecko.model.GeckoModel;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class SaveAndLoadTest {
    @Nested
    @Order(1)
    public class ProjectFileSerializerTest {
        static ProjectFileSerializer projectFileSerializerForEmpty;
        static ProjectFileSerializer projectFileSerializerForOneLevel;
        static ProjectFileSerializer projectFileSerializerForTree;
        private static GeckoViewModel emptyGeckoViewModel;
        private static GeckoViewModel oneLevelGeckoViewModel;
        private static GeckoViewModel treeGeckoViewModel;
        private static ObjectMapper mapper;
        static String EMPTY_GECKO_JSON = "{\"model\":{\"id\":0,\"name\":\"Element_0\",\"code\":null,\"automaton\":{\"startState\":null,\"regions\":[],\"states\":[],\"edges\":[]},\"children\":[],\"connections\":[],\"variables\":[]},\"startStates\":[],\"viewModelProperties\":[]}";
        static String NON_NULL_AUTOMATON_JSON = "\"automaton\":{";
        static String NON_NULL_START_STATE_JSON = "\"startState\":{";
        static String NON_NULL_REGIONS_JSON = "\"regions\":[{";
        static String NON_NULL_REGION_STATES_JSON = "},\"states\":[{";
        static String NO_CHILDREN = "\"children\":[]";
        static String PRESENT_CHILDREN = "\"children\":[{\"id\":";

        @BeforeAll
        static void setUp() {
            mapper = new ObjectMapper();

            try {
                emptyGeckoViewModel = new GeckoViewModel(new GeckoModel());
            } catch (ModelException e) {
                fail();
            }

            try {
                oneLevelGeckoViewModel = new GeckoViewModel(new GeckoModel());
            } catch (ModelException e) {
                fail();
            }

            SystemViewModel oneLevelRoot = (SystemViewModel) oneLevelGeckoViewModel.getViewModelElement(
                oneLevelGeckoViewModel.getGeckoModel().getRoot());
            ViewModelFactory oneLevelFactory = oneLevelGeckoViewModel.getViewModelFactory();

            assertDoesNotThrow(() -> {
                PortViewModel port1 = oneLevelFactory.createPortViewModelIn(oneLevelRoot);
                port1.setVisibility(Visibility.INPUT);
                port1.setName("emptyPort1");
                port1.updateTarget();

                PortViewModel port2 = oneLevelFactory.createPortViewModelIn(oneLevelRoot);
                port2.setVisibility(Visibility.OUTPUT);
                port2.setName("emptyPort2");
                port2.updateTarget();
            });

            //        NOT OKAY, has to throw:
            //        assertThrows(ModelException.class, () -> oneLevelFactory.createSystemConnectionViewModelIn(oneLevelRoot,
            //            (PortViewModel) oneLevelGeckoViewModel.getViewModelElement(oneLevelRoot.getTarget()
            //                .getVariableByName("emptyVar1")), (PortViewModel) oneLevelGeckoViewModel
            //                .getViewModelElement(oneLevelRoot.getTarget().getVariableByName("emptyVar2"))));

            assertDoesNotThrow(() -> {
                oneLevelFactory.createRegionViewModelIn(oneLevelRoot);

                RegionViewModel regionWithStates = oneLevelFactory.createRegionViewModelIn(oneLevelRoot);
                regionWithStates.addState(oneLevelFactory.createStateViewModelIn(oneLevelRoot));
                regionWithStates.updateTarget();
            });

            assertDoesNotThrow(() -> {
                StateViewModel state1 = oneLevelFactory.createStateViewModelIn(oneLevelRoot);
                StateViewModel state2 = oneLevelFactory.createStateViewModelIn(oneLevelRoot);
                oneLevelFactory.createContractViewModelIn(state2);

                oneLevelFactory.createEdgeViewModelIn(oneLevelRoot, state1, state2);
                state1.updateTarget();
                state2.updateTarget();
            });

            try {
                treeGeckoViewModel = new GeckoViewModel(new GeckoModel());
            } catch (ModelException e) {
                fail();
            }

            SystemViewModel treeRoot =
                (SystemViewModel) treeGeckoViewModel.getViewModelElement(treeGeckoViewModel.getGeckoModel().getRoot());
            ViewModelFactory treeFactory = treeGeckoViewModel.getViewModelFactory();

            assertDoesNotThrow(() -> {
                SystemViewModel child1 = treeFactory.createSystemViewModelIn(treeRoot);
                child1.setName("child1");
                PortViewModel port1 = treeFactory.createPortViewModelIn(child1);
                port1.setVisibility(Visibility.OUTPUT);
                port1.setName("treeVar1");
                RegionViewModel region1 = treeFactory.createRegionViewModelIn(child1);
                region1.addState(treeFactory.createStateViewModelIn(child1));
                child1.updateTarget();
                port1.updateTarget();
                region1.updateTarget();

                SystemViewModel child2 = treeFactory.createSystemViewModelIn(treeRoot);
                child1.setName("child2");
                PortViewModel port2 = treeFactory.createPortViewModelIn(child2);
                port2.setVisibility(Visibility.INPUT);
                port2.setName("treeVar2");
                RegionViewModel region2 = treeFactory.createRegionViewModelIn(child2);
                region2.addState(treeFactory.createStateViewModelIn(child2));
                child2.updateTarget();
                port2.updateTarget();
                region2.updateTarget();

                treeFactory.createSystemConnectionViewModelIn(treeRoot, port1, port2);

                SystemViewModel child3 = treeFactory.createSystemViewModelIn(child2);
                child1.setName("child3");
                child3.updateTarget();
            });

            projectFileSerializerForEmpty = new ProjectFileSerializer(emptyGeckoViewModel);
            projectFileSerializerForOneLevel = new ProjectFileSerializer(oneLevelGeckoViewModel);
            projectFileSerializerForTree = new ProjectFileSerializer(treeGeckoViewModel);
        }

        @Test
        void writeToFileEmpty() {
            File fileForEmpty = new File("src/test/java/org/gecko/io/files/emptyGecko.json");

            assertDoesNotThrow(() -> projectFileSerializerForEmpty.writeToFile(fileForEmpty));

            JsonNode empty = null;
            try {
                empty = mapper.readTree(fileForEmpty);
            } catch (IOException e) {
                fail("File for empty Gecko does not contain a JSON valid string.");
            }

            assertEquals(EMPTY_GECKO_JSON, empty.toString());
        }

        @Test
        void writeToFileOneLevel() {
            File fileForOneLevel = new File("src/test/java/org/gecko/io/files/oneLevelGecko.json");
            assertDoesNotThrow(() -> projectFileSerializerForOneLevel.writeToFile(fileForOneLevel));
            JsonNode oneLevel = null;
            try {
                oneLevel = mapper.readTree(fileForOneLevel);
            } catch (IOException e) {
                fail("File for one-level Gecko does not contain a JSON valid string.");
            }

            assertTrue(oneLevel.toString().contains(NON_NULL_AUTOMATON_JSON) && oneLevel.toString()
                .contains(NON_NULL_START_STATE_JSON) && oneLevel.toString().contains(NON_NULL_REGIONS_JSON)
                && oneLevel.toString().contains(NON_NULL_REGION_STATES_JSON) && oneLevel.toString()
                .contains(NO_CHILDREN));

        }

        @Test
        void writeToFileTree() {
            File fileForTree = new File("src/test/java/org/gecko/io/files/treeGecko.json");
            assertDoesNotThrow(() -> projectFileSerializerForTree.writeToFile(fileForTree));

            JsonNode tree = null;
            try {
                tree = mapper.readTree(fileForTree);
            } catch (IOException e) {
                fail("File for tree-structured Gecko does not contain a JSON valid string.");
            }

            assertTrue(
                tree.toString().contains(NON_NULL_AUTOMATON_JSON) && tree.toString().contains(NON_NULL_START_STATE_JSON)
                    && tree.toString().contains(NON_NULL_REGIONS_JSON) && tree.toString()
                    .contains(NON_NULL_REGION_STATES_JSON) && tree.toString().contains(PRESENT_CHILDREN));
        }
    }

    @Nested
    @Order(2)
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
            File serializedParsedEmpty = new File("src/test/java/org/gecko/io/files/serializedParsedEmptyGecko.json");

            try {
                parsedEmptyGeckoViewModel = projectFileParser.parse(fileForEmpty);
            } catch (IOException e) {
                fail("Empty Gecko could not be parsed from file.");
            }
            assertNotNull(parsedEmptyGeckoViewModel);

            ProjectFileSerializer serializer = new ProjectFileSerializer(parsedEmptyGeckoViewModel);
            assertDoesNotThrow(() -> serializer.writeToFile(serializedParsedEmpty));
            assertDoesNotThrow(
                () -> assertEquals(mapper.readTree(fileForEmpty), mapper.readTree(serializedParsedEmpty)));
        }

        @Test
        void parseOneLevel() {
            GeckoViewModel parsedOneLevelGeckoViewModel = null;
            File fileForOneLevel = new File("src/test/java/org/gecko/io/files/oneLevelGecko.json");
            File serializedParsedOneLevel =
                new File("src/test/java/org/gecko/io/files/serializedParsedOneLevelGecko.json");
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
            File serializedParsedTree = new File("src/test/java/org/gecko/io/files/serializedParsedTreeGecko.json");
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
            File fileForNonexistentStartState
                = new File("src/test/java/org/gecko/io/files/nonexistentStartState.json");
            assertThrows(IOException.class, () -> projectFileParser.parse(fileForNonexistentStartState));
        }

        @Test
        void parseFileWithValidStartStates() {
            File fileForNonexistentStartState
                = new File("src/test/java/org/gecko/io/files/existentStartState.json");
            assertDoesNotThrow(() -> projectFileParser.parse(fileForNonexistentStartState));
        }

        @Test
        void parseProjectFileWithNullRoot() {
            File fileForNullRoot = new File("src/test/java/org/gecko/io/files/nullRoot.json");
            assertThrows(IOException.class, () -> projectFileParser.parse(fileForNullRoot));
        }
    }
}

package org.gecko.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FileTypesTest {
    @Test
    void getFileNameRegex() {
        assertEquals("*.json", FileTypes.JSON.getFileNameGlob());
        assertEquals("*.sys", FileTypes.SYS.getFileNameGlob());
    }
}

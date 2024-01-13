package org.gecko.io;

import org.gecko.application.Gecko;

import java.io.File;

public interface FileParser {
    Gecko parse(File file);
}

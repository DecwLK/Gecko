package org.gecko.io;

import java.io.File;
import org.gecko.application.Gecko;

public interface FileParser {
    Gecko parse(File file);
}

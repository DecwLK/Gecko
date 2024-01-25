package org.gecko.io;

import lombok.Getter;

@Getter
public enum FileTypes {
    JSON("Json Files", "json"), SYS("Sys Files", "sys");

    private final String fileDescription;
    private final String fileExtension;

    FileTypes(String fileDescription, String fileExtension) {
        this.fileDescription = fileDescription;
        this.fileExtension = fileExtension;
    }

    public String getFileNameRegex() {
        return "*." + fileExtension;
    }
}

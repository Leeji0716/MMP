package com.example.MMP.wod;

import lombok.Getter;

public enum OSType {
    Window("C:/web", "file:///c:/web/"),
    Linux("/home/ubuntu/MMP/data", "file:/home/ubuntu/MMP/data/");

    @Getter
    private final String path;
    @Getter
    private final String resourceHandler;

    OSType(String path, String resourceHandler) {
        this.path = path;
        this.resourceHandler = resourceHandler;
    }

    public static OSType getInstance() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win"))
            return Window;
        if (osName.contains("linux"))
            return Linux;
        else {
            System.out.println("Unsupported OS: " + osName);
            return null;
        }
    }
}

package com.francescoceliento.system;

public class SystemViewer {

    //Enum per rappresentare i tipi di sistema operativo
    public enum OperatingSystemType {
        WINDOWS,
        MACOS,
        LINUX,
        UNKNOW
    }

    /**
     * Determina e restituisce il tipo di sistema operativo in uso
     * @author @francescoceliento@github.com
     *
     * @return OperatingSystemType
     */
    public static OperatingSystemType getOperatingSystemType() {
        // Recupera la proprietà di sistema 'os.name' che contiene il nome del SO
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return OperatingSystemType.WINDOWS;
        } else if (osName.contains("mac")) {
            return OperatingSystemType.MACOS;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            // Include Linux, Unix e AIX
            return OperatingSystemType.LINUX;
        } else {
            return OperatingSystemType.UNKNOW;
        }
    }

    
}

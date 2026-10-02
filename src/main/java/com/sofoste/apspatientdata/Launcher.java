package com.sofoste.apspatientdata;

/**
 * Keeps the executable JAR independent from JavaFX's special launcher logic.
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        if (args.length == 1 && "--version".equals(args[0])) {
            System.out.println("APS Patient Data Manager 1.0.0");
            return;
        }
        PatientFormApp.main(args);
    }
}

package com.sofoste.apspatientdata;

/**
 * Keeps the executable JAR independent from JavaFX's special launcher logic.
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        if (args.length == 1 && "--version".equals(args[0])) {
            System.out.println("APS Patient Data Manager 2.0.0 · Neptune");
            return;
        }
        if (args.length == 2 && "--screenshot".equals(args[0])) {
            PatientFormApp.setScreenshotPath(args[1]);
        }
        PatientFormApp.main(args);
    }
}

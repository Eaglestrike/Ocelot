package org.team114.ocelot.auto;

public class AutoModeExecutor {
    private AutoModeBase autoMode;
    private Thread thread;

    public void setAutoMode(AutoModeBase autoMode) {
        this.autoMode = autoMode;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(() -> {
                if (autoMode != null) {
                    autoMode.run();
                }
            });
            thread.start();
        }
    }

    public void stop() {
        if (autoMode != null) {
            autoMode.stop();
        }
        thread = null;
    }
}

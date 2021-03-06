/**
 *  SIWECOS-Host-Validator - A Webservice for the Siwecos Infrastructure to validate user provided hosts
 *
 *  Copyright 2019 Ruhr University Bochum / Hackmanit GmbH
 *
 *  Licensed under Apache License 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package de.rub.nds.siwecos.validator.config;

/**
 *
 * @author robert
 */
public class WsConfig {

    private int probeThreads;
    private int parallelProbeThreads;
    private int parallelScanJobs;
    private boolean debugMode;

    public WsConfig() {
        this.probeThreads = probeThreads;
        this.parallelProbeThreads = parallelProbeThreads;
        this.parallelScanJobs = parallelScanJobs;
        this.debugMode = debugMode;
    }

    public int getProbeThreads() {
        return probeThreads;
    }

    public void setProbeThreads(int probeThreads) {
        this.probeThreads = probeThreads;
    }

    public int getParallelProbeThreads() {
        return parallelProbeThreads;
    }

    public void setParallelProbeThreads(int parallelProbeThreads) {
        this.parallelProbeThreads = parallelProbeThreads;
    }

    public int getParallelScanJobs() {
        return parallelScanJobs;
    }

    public void setParallelScanJobs(int parallelScanJobs) {
        this.parallelScanJobs = parallelScanJobs;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

}

package it.polimi.ingsw.am32.Utilities;

import java.util.concurrent.ExecutorService;

public class Configuration {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private static final String JSON_CONFIG_FILE = "src/main/resources/it/polimi/ingsw/am32/Utilities/Config.json";
    private static Configuration instance;
    private int rmiPort;
    private int socketPort;
    private int pingTimePeriod;
    private int maxPingCount;
    private String serverIp;
    private ExecutorService executorService;


    //---------------------------------------------------------------------------------------------
    // Constructor

    private Configuration(String[] args)  {}


    //---------------------------------------------------------------------------------------------
    // Getters

    public static Configuration getInstance() {
        if (instance == null) instance = new Configuration(new String[0]);
        return instance;
    }

    public static Configuration createInstance(String[] args) {
        if (instance == null) instance = new Configuration(args);
        return instance;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public int getPingTimePeriod() {
        return pingTimePeriod;
    }

    public int getMaxPingCount() {
        return maxPingCount;
    }

    public String getServerIp() {
        return serverIp;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
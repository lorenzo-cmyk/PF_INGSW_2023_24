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
    // Methods

    private int portValidator(int newPort, int defaultPort) {
        if(newPort > 1023 && newPort < 65536) return newPort;
        return defaultPort;
    }

    private String serverIpValidator(String ipToValidate) {

        char[] workingIp = ipToValidate.toCharArray();
        int prevIndex = 0;
        int counter = 0;
        int tmp;
        int workingLenght = 15;

        if(workingIp.length < workingLenght)
            workingLenght = workingIp.length;

        for(int i = 0; i < workingLenght; i++) {

            if(workingIp[i] == '.') {
                counter++;

                if(counter > 3)
                    return serverIp;

                try {
                    tmp = Integer.parseInt(ipToValidate, prevIndex, i, 10);
                }catch (Exception e){
                    return serverIp;
                }

                if(tmp < 0 || tmp > 255)
                    return serverIp;

                prevIndex = i+1;
            }
        }

        if(counter < 3)
            return serverIp;

        try {
            tmp = Integer.parseInt(ipToValidate, prevIndex, workingIp.length, 10);
        }catch (Exception e){
            return serverIp;
        }

        if(tmp < 0 || tmp > 255)
            return serverIp;

        return ipToValidate;
    }


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
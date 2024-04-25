package it.polimi.ingsw.am32.Utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private Configuration(String[] args) {

        // standard config values

        socketPort = 30000;
        rmiPort = 30001;
        pingTimePeriod = 5000;
        maxPingCount = 3;
        serverIp = "127.0.0.1";
        executorService = Executors.newCachedThreadPool();

        // temporary values

        int rmiPortFile = -1;
        int rmiPortArgs = -2;
        int socketPortFile = -3;
        int socketPortArgs = -4;

        // overwrite server parameters with data from config file

        JsonNode jsonNode = null;
        boolean valid;

        try {
            String configFileContent = new String(Files.readAllBytes(Paths.get(JSON_CONFIG_FILE)));

            ObjectMapper objectMapper = new ObjectMapper();

            jsonNode = objectMapper.readTree(configFileContent);

            valid = true;

        } catch (IOException e) {
            valid = false;
        }

        if(valid){
            try {
                socketPortFile = portValidator(jsonNode.get("socketPort").asInt(), socketPortFile);
            } catch (Exception ignored){}

            try {
                rmiPortFile = portValidator(jsonNode.get("rmiPort").asInt(), rmiPortFile);
            } catch (Exception ignored){}

            try {
                pingTimePeriod = jsonNode.get("pingTimePeriod").asInt();
            } catch (Exception ignored){}

            try {
                maxPingCount = jsonNode.get("maxPingCount").asInt();
            } catch (Exception ignored){}

            try {
                serverIp = serverIpValidator(jsonNode.get("serverIp").asText());
            } catch (Exception ignored){}
        }

        // overwrite server parameters with data from startup arguments

        int i = 0;
        while (i < args.length) {

            if(i+1 >= args.length) {
                i++;
                continue;
            }

            if(!args[i].startsWith("-") || args[i+1].startsWith("-")) {
                i++;
                continue;
            }

            String arg = args[i].toLowerCase();

            try {
                switch (arg) {
                    case "-sp" -> socketPortArgs = portValidator(Integer.parseInt(args[i + 1]), socketPortArgs);
                    case "-rp" -> rmiPortArgs = portValidator(Integer.parseInt(args[i + 1]), rmiPortArgs);
                    case "-ptp" -> pingTimePeriod = Integer.parseInt(args[i + 1]);
                    case "-mpc" -> maxPingCount = Integer.parseInt(args[i + 1]);
                    case "-si" -> serverIp = serverIpValidator(args[i + 1]);
                }
            } catch (NumberFormatException ignored) {}

            i += 2;
        }

        // port incompatibility resolution

        int[] tmpSocket = new int[]{socketPort, socketPortFile, socketPortArgs};
        int[] tmpRmi = new int[]{rmiPort, rmiPortFile, rmiPortArgs};

        for(int j = 1; j < 3; j++){
            if(tmpSocket[j] == tmpRmi[j]){}
        }

        if(socketPortArgs > 0) {
            socketPort = socketPortArgs;
            if (rmiPortArgs > 0 && rmiPortArgs != socketPortArgs)
                rmiPort = rmiPortArgs;
            else if(rmiPortFile > 0 && rmiPortFile != socketPortArgs)
                rmiPort = rmiPortFile;
        } else if(rmiPortArgs > 0) {
            rmiPort = rmiPortArgs;
            if(socketPortFile > 0 && socketPortFile != rmiPortArgs)
                socketPort = socketPortFile;
        } else {
            if(socketPortFile > 0)
                socketPort = socketPortFile;
            if(rmiPortFile > 0 && rmiPortFile != socketPortFile)
                rmiPort = rmiPortFile;
        }

        System.out.println("Socket port: " + socketPort);
        System.out.println("RMI port: " + rmiPort);
        System.out.println("Ping time period: " + pingTimePeriod);
        System.out.println("Max Ping count: " + maxPingCount);
        System.out.println("Server ip: " + serverIp);
    }


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
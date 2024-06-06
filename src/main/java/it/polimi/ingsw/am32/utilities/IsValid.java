package it.polimi.ingsw.am32.utilities;

public class IsValid {
    /**
     * This method checks if the provided IP address is valid.
     * An IP address is considered valid if it has 4 parts separated by dots,
     * each part is a number between 0 and 255, and the first part is not 0.
     *
     * @param ip The IP address to be checked.
     * @return true if the IP address is valid, false otherwise.
     */
    public boolean isIpValid(String ip){
        String[] parts = ip.split("\\.");
        int i;
        // Check if the IP address has 4 parts
        if (parts.length != 4)
            return false;
        // Check if each part is a number between 0 and 255
        for (String s : parts) {
            try{
                i = Integer.parseInt(s);
            } catch (NumberFormatException e){
                return false;
            }
            if ((i < 0) || (i > 255))
                return false;
        }
        // Since this is a client-side method, an IP must not start with 0
        return Integer.parseInt(parts[0]) != 0;
    }

    /**
     * This method checks if the provided port number is valid.
     * A port number is considered valid if it is greater than 0 and less than or equal to 65535.
     *
     * @param port The port number to be checked.
     * @return true if the port number is valid, false otherwise.
     */
    public boolean isPortValid(int port){
        return port > 0 && port <= 65535;
    }
}

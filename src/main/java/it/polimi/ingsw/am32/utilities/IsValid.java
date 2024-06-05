package it.polimi.ingsw.am32.utilities;

public class IsValid {
    public boolean isIpValid(String ip){
        String[] parts = ip.split("\\.");
        int i;
        if (parts.length != 4)
            return false;
        for (String s : parts) {
            try{
                i = Integer.parseInt(s);
            } catch (NumberFormatException e){
                return false;
            }
            if ((i < 0) || (i > 255))
                return false;
        }
        return true;
    }
    public boolean isPortValid(int port){
        return port >=0 && port <= 65535;
    }
}

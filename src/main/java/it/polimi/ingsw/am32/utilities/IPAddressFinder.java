package it.polimi.ingsw.am32.utilities;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to find all IPv4 addresses of the current machine.
 */
public class IPAddressFinder {

    /**
     * This method returns a list of all IPv4 addresses of the current machine.
     * It iterates over all network interfaces and their associated addresses.
     * If an address is an instance of Inet4Address, it is added to the list.
     *
     * @return A list of strings representing the IPv4 addresses.
     */
    public static List<String> getIPv4Addresses() {
        // Create an empty list of strings to store the IPv4 addresses.
        List<String> ipv4Addresses = new ArrayList<>();
        try {
            // Iterate over all network interfaces.
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                // Iterate over all addresses associated with the current network interface.
                for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                    // If the address is an instance of Inet4Address, add it to the list.
                    if (inetAddress instanceof Inet4Address) {
                        ipv4Addresses.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException ignored) {
            // If an exception occurs, the list of addresses will be empty.
        }
        return ipv4Addresses;
    }

}

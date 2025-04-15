package com.diret.calculdora_vlsm;

public class Subnet {
    private String binAddress;
    private String address;
    private int prefix;
    private int bitsHost;
    private int numRequiredHosts;
    private int allocatedHosts;
    private String minHost;
    private String maxHost;
    private String broadcast;

    // Constructor
    public Subnet(String binAddress, String address, int prefix, int bitsHost, int numRequiredHosts,
                  int allocatedHosts, String minHost, String maxHost, String broadcast) {
        this.binAddress = binAddress;
        this.address = address;
        this.prefix = prefix;
        this.bitsHost = bitsHost;
        this.numRequiredHosts = numRequiredHosts;
        this.allocatedHosts = allocatedHosts;
        this.minHost = minHost;
        this.maxHost = maxHost;
        this.broadcast = broadcast;
    }

    // Getters and Setters for the attributes
    public String getBinAddress() {
        return binAddress;
    }

    public void setBinAddress(String binAddress) {
        this.binAddress = binAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public int getBitsHost() {
        return bitsHost;
    }

    public void setBitsHost(int bitsHost) {
        this.bitsHost = bitsHost;
    }

    public int getNumRequiredHosts() {
        return numRequiredHosts;
    }

    public void setNumRequiredHosts(int numRequiredHosts) {
        this.numRequiredHosts = numRequiredHosts;
    }

    public int getAllocatedHosts() {
        return allocatedHosts;
    }

    public void setAllocatedHosts(int allocatedHosts) {
        this.allocatedHosts = allocatedHosts;
    }

    public String getMinHost() {
        return minHost;
    }

    public void setMinHost(String minHost) {
        this.minHost = minHost;
    }

    public String getMaxHost() {
        return maxHost;
    }

    public void setMaxHost(String maxHost) {
        this.maxHost = maxHost;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    // toString method to display subnet details
    @Override
    public String toString() {
        return "Address: " + address + " | Mask: /" + prefix;
    }

    // Method to validate the network format
    public static boolean isValidNetworkFormat(String network) {
        // Regex to check the general format of an IPv4 address
        String networkFormatRegex = "^((\\d{1,3})\\.){3}(\\d{1,3})$";
        String[] octets = network.split("\\.");
        boolean isValid = false;

        if (network.matches(networkFormatRegex)) {
            isValid = true;

            // Check if each octet is a valid number between 0 and 255
            for (String octet : octets) {
                int numericValue = Integer.parseInt(octet);
                if (numericValue < 0 || numericValue > 255) {
                    isValid = false;
                    System.out.println("Each octet should be a number between 0 and 255.");
                    return false;
                }
            }
        } else {
            isValid = false;
            System.out.println("Invalid network format. Please use a valid IPv4 address format.");
        }

        return isValid;
    }
}

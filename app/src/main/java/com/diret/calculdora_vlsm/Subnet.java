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
    private String name;

    // Constructor
    public Subnet(String binAddress, String address, int prefix, int bitsHost, int numRequiredHosts,
                  int allocatedHosts, String minHost, String maxHost, String broadcast, String name) {
        this.binAddress = binAddress;
        this.address = address;
        this.prefix = prefix;
        this.bitsHost = bitsHost;
        this.numRequiredHosts = numRequiredHosts;
        this.allocatedHosts = allocatedHosts;
        this.minHost = minHost;
        this.maxHost = maxHost;
        this.broadcast = broadcast;
        this.name = name;
    }

    public Subnet() {
        this.binAddress = "";
        this.address = "";
        this.prefix = 0;
        this.bitsHost = 0;
        this.numRequiredHosts = 0;
        this.allocatedHosts = 0;
        this.minHost = "";
        this.maxHost = "";
        this.broadcast = "";
        this.name = "";
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

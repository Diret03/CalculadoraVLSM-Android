package com.diret.calculdora_vlsm;

import java.util.ArrayList;

public class Calculator {

    // Helper function to calculate host bits
    public static int hostBits(int hosts) {
        int exp = 0;
        while (Math.pow(2, exp) - 2 < hosts) {
            exp++;
        }
        return exp;
    }


    // Helper function to generate binary combinations
    public static ArrayList<String> generateBinaryCombinations(int n) {
        ArrayList<String> arr = new ArrayList<>();
        generateBinaryCombinationsHelper(n, "", arr);
        return arr;
    }

    public static void generateBinaryCombinationsHelper(int n, String prefix, ArrayList<String> arr) {
        if (n == 0) {
            arr.add(prefix);
        } else {
            generateBinaryCombinationsHelper(n - 1, prefix + "0", arr);
            generateBinaryCombinationsHelper(n - 1, prefix + "1", arr);
        }
    }

    // Helper function to replace a substring within a string
    public static String replaceSubstring(String base, String newString, int index) {
        StringBuilder baseSb = new StringBuilder(base);
        StringBuilder newSb = new StringBuilder(newString);

        int newStringIndex = 0;
        for (int i = 0; i < newString.length(); ) {
            if (baseSb.charAt(index + i) != '.') {
                baseSb.setCharAt(index + i, newSb.charAt(newStringIndex++));
                i++;
            } else {
                index++;
            }
        }
        return baseSb.toString();
    }

    // Helper function to replace a binary combination within a binary IP address
    public static String replaceCombination(String ipAddress, String newString, int prefixLength) {
        String strNoDots = ipAddress.replace(".", "");
        String ans = replaceSubstring(strNoDots, newString, prefixLength - 1);

        StringBuilder ipAddressWithDots = new StringBuilder();
        for (int i = 0; i < ans.length(); i += 8) {
            ipAddressWithDots.append(ans.substring(i, Math.min(i + 8, ans.length())));
            if (i + 8 < ans.length()) {
                ipAddressWithDots.append(".");
            }
        }
        return ipAddressWithDots.toString();
    }

    // Function to generate subnets based on a decimal address, prefix, and required hosts
    public static ArrayList<Subnet> generateSubnets(String majorAdd, int prefix, int hosts) {
        int hostBitsValue = hostBits(hosts);
        int netBits = 32 - prefix - hostBitsValue;

        if (netBits < 0) {
            return null; // Indicate failure
        } else {
            ArrayList<String> bitCombinations = generateBinaryCombinations(netBits);
            ArrayList<Subnet> subnets = new ArrayList<>();
            int bitToStart = prefix + 1;
            prefix += netBits;

            for (int i = 0; i < Math.pow(2, netBits); i++) {
                String ipAddress = SubnetUtils.addressToBinary(majorAdd); // Use your SubnetUtils
                Subnet sub = new Subnet();
                sub.setBinAddress(replaceCombination(ipAddress, bitCombinations.get(i), bitToStart));
                sub.setAddress(SubnetUtils.binaryToDecimalAddress(sub.getBinAddress())); // Use your SubnetUtils
                sub.setPrefix(prefix);
                sub.setBitsHost(hostBitsValue);
                sub.setNumRequiredHosts(hosts);
                sub.setAllocatedHosts((int) Math.pow(2, hostBitsValue) - 2);

                ArrayList<String> hostCombinations = generateBinaryCombinations(hostBitsValue);
                sub.setMinHost(SubnetUtils.binaryToDecimalAddress(replaceCombination(sub.getBinAddress(), hostCombinations.get(1), prefix + 1))); // Use your SubnetUtils
                sub.setMaxHost(SubnetUtils.binaryToDecimalAddress(replaceCombination(sub.getBinAddress(), hostCombinations.get(hostCombinations.size() - 2), prefix + 1))); // Use your SubnetUtils
                sub.setBroadcast(SubnetUtils.binaryToDecimalAddress(replaceCombination(sub.getBinAddress(), hostCombinations.get(hostCombinations.size() - 1), prefix + 1))); // Use your SubnetUtils

                subnets.add(sub);
            }
            return subnets;
        }
    }

    // Function to generate final subnets based on a decimal address, prefix, and sorted host array
    public static ArrayList<Subnet> finalSubnets(String majorAdd, int prefix, ArrayList<Host> hosts) {
        int index = 0;
        ArrayList<Subnet> results = new ArrayList<>();
        ArrayList<Subnet> unasignedSubnets = new ArrayList<>();

        while (results.size() < hosts.size()) {
            ArrayList<Subnet> subnetting = null;
            int numHosts = hosts.get(index).getNumHosts();

            if (results.isEmpty()) {
                subnetting = generateSubnets(majorAdd, prefix, numHosts);
                if (subnetting == null) {
                    return null; // Indicate failure
                }
            } else {
                if (!unasignedSubnets.isEmpty()) {
                    Subnet auxSubnet = unasignedSubnets.get(0);
                    subnetting = generateSubnets(auxSubnet.getAddress(), auxSubnet.getPrefix(), numHosts);
                    unasignedSubnets.remove(0);
                } else {
                    results.add(null); // Or handle this case appropriately
                    break;
                }
            }

            for (Subnet current : subnetting) {
                int numBitsHosts = current.getBitsHost();

                if (index < hosts.size() && Math.pow(2, numBitsHosts) - 2 >= hosts.get(index).getNumHosts()) {
                    boolean wasNumBitsChanged = false;

                    while (Math.pow(2, numBitsHosts) - 2 > hosts.get(index).getNumHosts()) {
                        if (Math.pow(2, numBitsHosts - 1) - 2 > hosts.get(index).getNumHosts()) {
                            numBitsHosts--;
                            wasNumBitsChanged = true;
                        } else {
                            break;
                        }
                    }

                    if (!wasNumBitsChanged) {
                        current.setName(hosts.get(index).getName());
                        results.add(current);
                        index++;
                    } else {
                        if (unasignedSubnets.isEmpty()) {
                            unasignedSubnets.addAll(subnetting.subList(subnetting.indexOf(current), subnetting.size()));
                        } else {
                            unasignedSubnets.add(0, current);
                        }
                        break;
                    }
                }
            }
        }
        return results;
    }

}

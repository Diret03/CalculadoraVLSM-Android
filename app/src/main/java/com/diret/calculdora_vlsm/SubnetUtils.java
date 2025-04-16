package com.diret.calculdora_vlsm;

public class SubnetUtils {

    // Convert decimal number to binary (8 bits)
    public static String decimalToBinary(String decimalStr) {
        int num = Integer.parseInt(decimalStr, 10);
        StringBuilder binaryStr = new StringBuilder();

        // Create the binary string, making sure it's 8 bits
        for (int i = 7; i >= 0; i--) {
            if (num >= Math.pow(2, i)) {
                binaryStr.append('1');
                num -= Math.pow(2, i);
            } else {
                binaryStr.append('0');
            }
        }

        return binaryStr.toString();
    }

    // Convert binary to decimal
    public static String binaryToDecimal(String binary) {
        int result = 0;
        for (int i = 0; i < binary.length(); i++) {
            char bit = binary.charAt(i);
            if (bit == '1') {
                result += Math.pow(2, binary.length() - 1 - i);
            }
        }
        return String.valueOf(result);
    }

    // Convert an IP address from decimal to binary
    public static String addressToBinary(String address) {
        String[] octets = address.split("\\.");
        StringBuilder binaryAddress = new StringBuilder();

        for (String octet : octets) {
            binaryAddress.append(decimalToBinary(octet)).append(".");
        }

        return binaryAddress.substring(0, binaryAddress.length() - 1); // Remove trailing dot
    }

    // Convert a binary IP address to decimal format
    public static String binaryToDecimalAddress(String binaryAddress) {
        String[] octets = binaryAddress.split("\\.");
        StringBuilder decimalAddress = new StringBuilder();

        for (String octet : octets) {
            decimalAddress.append(binaryToDecimal(octet)).append(".");
        }

        return decimalAddress.substring(0, decimalAddress.length() - 1); // Remove trailing dot
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
                    System.out.println("Cada octeto debe ser un un número entre 0 y 255");
                    return false;
                }
            }
        } else {
            isValid = false;
            System.out.println("Formato inválido de red. Por favor use el formato decimal IPv4.");
        }

        return isValid;
    }
}

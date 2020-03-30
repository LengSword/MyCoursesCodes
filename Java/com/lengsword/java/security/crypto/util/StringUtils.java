package com.lengsword.java.security.crypto.util;

/**
 * StringUtils
 *
 * @author LengSword
 */
public class StringUtils {


    /**
     * Transform the {@code segmentCount} length binary cipher into a hexadecimal cipher
     *
     * @param binaryCipher the {@code segmentCount} length binary cipher
     * @param segmentCount the segments count of binary cipher
     *
     * @return hexadecimal cipher
     */
    public static String binToHexCipher(String binaryCipher, int segmentCount) {
        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < segmentCount; i++) {
            String binaryCipherBlock = binaryCipher.substring(i * 8, (i + 1) * 8);
            cipher.append(paddingBinaryZeros(Integer.toHexString(binaryToDecimal(binaryCipherBlock)), 2));
        }
        return cipher.toString();
    }

    public static String binToHexCipher(StringBuilder binaryCipher, int segmentCount) {
        return binToHexCipher(binaryCipher.toString(), segmentCount);
    }

    /**
     * Transform the {@code bytesCount} length hexadecimal cipher into a plain cipher
     *
     * @param hexCipher    the {@code segmentCount} length hexadecimal cipher
     * @param segmentCount the segments count of hexadecimal cipher
     *
     * @return plain cipher
     */
    public static String hexToPlainCipher(String hexCipher, int segmentCount) {
        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < segmentCount; i++) {
            String hexCipherBlock = hexCipher.substring(i * 2, (i + 1) * 2);
            cipher.append((char) Integer.parseInt(hexCipherBlock, 16));
        }
        return cipher.toString();
    }

    public static String hexToPlainCipher(StringBuilder hexCipher, int segmentCount) {
        return hexToPlainCipher(hexCipher.toString(), segmentCount);
    }

    public static String plainToHex(String plaintext) {
        StringBuilder hexString = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            hexString.append(paddingBinaryZeros(Integer.toHexString(c), 2));
        }
        return hexString.toString();
    }

    public static String plainToHex(StringBuilder plaintext) {
        return plainToHex(plaintext.toString());
    }

    /**
     * Transform the 8 chars length plaintext into the 64-bit binary string
     *
     * @param plaintext the 8 chars length plaintext
     *
     * @return a 64-bit binary string
     */
    public static String plainBlockToBinary(String plaintext) {
        StringBuilder binaryPlaintext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            binaryPlaintext.append(getWholeBinary(plaintext.charAt(i), 8));
        }
        return paddingBinaryZeros(binaryPlaintext.toString(), 64).toString();
    }

    /**
     * Convert the number into binary and fill the binary with zeros
     *
     * @param number a number or a char
     * @param length the length of whole binary string
     *
     * @return whole binary string
     */
    public static String getWholeBinary(int number, int length) {
        String wholeBinary = Integer.toBinaryString(number);
        return paddingBinaryZeros(wholeBinary, length);
    }

    public static String paddingBinaryZeros(String binaryString, int targetLength) {
        StringBuilder result = new StringBuilder(binaryString);
        while (result.length() < targetLength) {
            result.insert(0, 0);
        }
        return result.toString();
    }

    public static int binaryToDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }

    /**
     * Reindex the binary string by reindexingTable
     *
     * @param binaryString    binary string
     * @param reindexingTable the table for reindexing binary string
     *
     * @return reindexed binary string
     */
    public static StringBuilder reindexBinary(String binaryString, int[] reindexingTable) {
        StringBuilder result = new StringBuilder();
        for (int newIndex : reindexingTable) {
            result.append(binaryString.charAt(newIndex - 1));
        }
        return result;
    }

    public static StringBuilder leftShiftBinary(StringBuilder src, int length) {
        StringBuilder result = new StringBuilder(src);
        for (int i = 0; i < length; i++) {
            char firstChar = result.charAt(0);
            result.deleteCharAt(0);
            result.append(firstChar);
        }
        return result;
    }

    public static StringBuilder xorBinary(StringBuilder s1, StringBuilder s2, int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                result.append("0");
            } else {
                result.append("1");
            }
        }
        return result;
    }

    private static String segmentMessage(String message, int segmentLength) {
        StringBuilder segmentBinary = new StringBuilder();

        int messageLength = message.length();
        for (int i = 1; i <= messageLength; i++) {
            segmentBinary.append(message.charAt(i - 1));
            if (i % segmentLength == 0) {
                segmentBinary.append(" ");
            }
        }
        return segmentBinary.toString();
    }

    private static String segmentBinaryMessage(String message) {
        return segmentMessage(message, 8);
    }

    private static String segmentHexMessage(String message) {
        return segmentMessage(message, 2);
    }

    public static void printFormattedBinaryDataWithIndex(int index, String prefix, String message) {
        String result = String.format("%-30s: %s => [%d]", prefix, segmentBinaryMessage(message), index);
        System.out.println(result);
    }

    public static void printFormattedBinaryDataWithIndex(int index, String prefix, StringBuilder message) {
        printFormattedBinaryDataWithIndex(index, prefix, message.toString());
    }

    public static void printFormattedBinaryData(String prefix, String message) {
        String result = String.format("%-30s: %s", prefix, segmentBinaryMessage(message));
        System.out.println(result);
    }

    public static void printFormattedBinaryData(String prefix, StringBuilder message) {
        printFormattedBinaryData(prefix, message.toString());
    }

    public static void printFormattedHexData(String prefix, String message) {
        String result = String.format("%-30s: %s", prefix, segmentHexMessage(message));
        System.out.println(result);
    }

    public static void printFormattedHexData(String prefix, StringBuilder message) {
        printFormattedHexData(prefix, message.toString());
    }
}

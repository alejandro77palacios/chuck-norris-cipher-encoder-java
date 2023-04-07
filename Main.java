package chucknorris;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please input operation (encode/decode/exit):");
            String operation = scanner.nextLine();
            switch (operation) {
                case "encode":
                    System.out.println("Input string:");
                    String input = scanner.nextLine();
                    encodeNorris(input);
                    break;
                case "decode":
                    System.out.println("Input encoded string:");
                    String encoded = scanner.nextLine();
                    try {
                        if (invalidChars(encoded)) {
                            System.out.println("Encoded string is not valid.");
                        } else {
                            decodeNorris(encoded);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "exit":
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.printf("There is no '%s' operation%n", operation);
                    break;

            }
            System.out.println();
        }
    }


    public static void decodeNorris(String encoded) {
        String decodedSequence = decodeSequence(encoded);
        String[] blocks7 = splitBlocks7(decodedSequence);
        System.out.println("Decoded string:");
        System.out.println(decodeBlocks(blocks7));
    }

    public static String decodeBlocks(String[] blocks) {
        char[] decoded = new char[blocks.length];
        for (int i = 0; i < decoded.length; i++) {
            decoded[i] = (char) Integer.parseInt(blocks[i], 2);
        }
        /*if (String.valueOf(decoded).length() % 7 != 0){
            throw new IllegalArgumentException("Encoded string is not valid.");
        }*/
        return String.valueOf(decoded);
    }

    public static String[] splitBlocks7(String sequence) {
        int numBlocks = sequence.length() / 7;
        if (numBlocks % 2 != 0){
            throw new IllegalArgumentException("Encoded string is not valid.");
        }
        String[] blocks = new String[numBlocks];
        for (int i = 0; i < numBlocks; i++) {
            blocks[i] = sequence.substring(i * 7, (1 + i) * 7);
        }
        return blocks;
    }

    public static String decodeSequence(String sequence) {
        String[][] pairs = getPairs(sequence);
        String decoded = "";
        for (String[] pair : pairs) {
            decoded += decodePair(pair);
        }
        return decoded;
    }

    public static String decodePair(String[] pair) {
        if (!(pair[0].equals("0") || pair[0].equals("00"))){
            throw new IllegalArgumentException("Encoded string is not valid.");
        }
        String first = pair[0].length() == 1 ? "1" : "0";
        String decoded = "";
        for (int i = 0; i < pair[1].length(); i++) {
            decoded += first;
        }
        return decoded;

    }

    public static String[][] getPairs(String sequence) {
        String[] individuals = sequence.split(" ");
        String[][] pairs = new String[individuals.length / 2][2];
        for (int i = 0; i < individuals.length; i += 2) {
            for (int j = 0; j < pairs.length; j++) {
                if (pairs[j][0] == null) {
                    pairs[j][0] = individuals[i];
                    pairs[j][1] = individuals[i + 1];
                    break;
                }
            }
        }
        return pairs;
    }

    private static void encodeNorris(String text) {
        String binaryString = stringToBinary(text);
        System.out.println("Encoded string:");
        while (!binaryString.isEmpty()) {
            String sequence = extractSequence(binaryString);
            binaryString = binaryString.substring(sequence.length());
            encodeSequence(sequence);
            System.out.print(" ");
        }
    }

    private static String stringToBinary(String str) {
        String binaryString = "";
        for (int i = 0; i < str.length(); i++) {
            char character = str.charAt(i);
            binaryString += formatBinary(character);
        }
        return binaryString;
    }


    public static String formatBinary(char character) {
        String binary = Integer.toBinaryString(character);
        return "%7s".formatted(binary).replace(' ', '0');
    }

    public static void encodeSequence(String sequence) {
        char character = sequence.charAt(0);
        if (character == '1') {
            System.out.print("0");
        } else {
            System.out.print("00");
        }
        System.out.print(" ");
        for (int i = 0; i < sequence.length(); i++) {
            System.out.print("0");
        }
    }

    public static String extractSequence(String text) {
        char first = text.charAt(0);
        String sequence = "";
        for (int i = 0; i < text.length(); i++) {
            char nextChar = text.charAt(i);
            if (nextChar == first) {
                sequence += String.valueOf(nextChar);
            } else {
                break;
            }
        }
        return sequence;
    }

    static boolean invalidChars(String text) {
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character != '0' && character != ' ') {
                return true;
            }
        }
        return false;
    }
}
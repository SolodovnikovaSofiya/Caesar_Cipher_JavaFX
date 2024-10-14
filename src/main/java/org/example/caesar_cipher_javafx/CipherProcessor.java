package org.example.caesar_cipher_javafx;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CipherProcessor {
    private String filePath;
    private int key;
    private static final char[] ALPHABET_ENG = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public CipherProcessor(String filePath, int key) {
        this.filePath = filePath;
        this.key = key;
    }

    public void encryptFile() {
        processFile(filePath, filePath + ".encrypted", this::encrypt);
    }

    public void decryptFile() {
        processFile(filePath, filePath + ".decrypted", this::decrypt);
    }

    public void bruteForceDecrypt() {
        String encryptedText = readFile(filePath);
        for (int key = 1; key < ALPHABET_ENG.length; key++) {
            String decryptedText = decrypt(encryptedText, key);
            if (isTextMeaningful(decryptedText)) {
                System.out.println("Найден правильный ключ: " + key);
                System.out.println("Расшифрованный текст:");
                System.out.println(decryptedText);

                // Запись результата в файл
                String outputFilePath = filePath + ".brute_force_decrypted";
                writeFile(outputFilePath, decryptedText);
                return;
            }
        }
        System.out.println("Не удалось найти правильный ключ.");
    }

    public void statisticalAnalysisDecrypt() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder encryptedText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                encryptedText.append(line).append("\n");
            }

            StatisticalAnalyzer analyzer = new StatisticalAnalyzer(encryptedText.toString(), new String(ALPHABET_ENG));
            int bestKey = analyzer.findBestKey();
            String decryptedText = decrypt(encryptedText.toString(), bestKey);

            System.out.println("Найден правильный ключ: " + bestKey);
            System.out.println("Расшифрованный текст:");
            System.out.println(decryptedText);

            // Запись результата в файл
            String outputFilePath = filePath + ".statistical_analysis_decrypted";

            writeFile(outputFilePath, decryptedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processFile(String inputFilePath, String outputFilePath, CipherFunction cipherFunction) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(cipherFunction.apply(line));
                writer.newLine();
            }
            System.out.println("Результат записан в файл: " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String encrypt(String text) {
        return encrypt(text, key);
    }

    private String decrypt(String text) {
        return decrypt(text, key);
    }

    private String decrypt(String text, int key) {
        StringBuilder decryptedText = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = new String(ALPHABET_ENG).indexOf(Character.toLowerCase(c));
            if (index != -1) {
                int newIndex = (index - key + ALPHABET_ENG.length) % ALPHABET_ENG.length;
                char newChar = ALPHABET_ENG[newIndex];
                decryptedText.append(Character.isUpperCase(c) ? Character.toUpperCase(newChar) : newChar);
            } else {
                decryptedText.append(c);
            }
        }

        return decryptedText.toString();
    }

    private String encrypt(String text, int key) {
        StringBuilder encryptedText = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = new String(ALPHABET_ENG).indexOf(Character.toLowerCase(c));
            if (index != -1) {
                int newIndex = (index + key) % ALPHABET_ENG.length;
                char newChar = ALPHABET_ENG[newIndex];
                encryptedText.append(Character.isUpperCase(c) ? Character.toUpperCase(newChar) : newChar);
            } else {
                encryptedText.append(c);
            }
        }

        return encryptedText.toString();
    }

    private String readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void writeFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("Результат записан в файл: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isTextMeaningful(String text) {
        String[] commonWords = {"the", "and", "is", "in", "it", "of", "to", "that", "was", "for"};
        for (String word : commonWords) {
            if (text.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    interface CipherFunction {
        String apply(String line);
    }
}

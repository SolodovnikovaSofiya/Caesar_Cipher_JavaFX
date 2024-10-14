package org.example.caesar_cipher_javafx;

import java.util.HashMap;
import java.util.Map;

public class StatisticalAnalyzer {
    private String text;
    private String alphabet;

    public StatisticalAnalyzer(String text, String alphabet) {
        this.text = text;
        this.alphabet = alphabet;
    }

    public int findBestKey() {
        Map<Character, Integer> frequencyMap = new HashMap<>();

        // Подсчет частоты встречаемости букв
        for (char c : text.toCharArray()) {
            if (alphabet.indexOf(Character.toLowerCase(c)) != -1) {
                frequencyMap.put(Character.toLowerCase(c), frequencyMap.getOrDefault(Character.toLowerCase(c), 0) + 1);
            }
        }

        // Поиск наиболее часто встречающейся буквы
        char mostFrequentChar = ' ';
        int maxFrequency = 0;

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentChar = entry.getKey();
            }
        }

        // Предполагаем, что наиболее часто встречающаяся буква в зашифрованном тексте соответствует 'e'
        int key = (alphabet.indexOf(mostFrequentChar) - alphabet.indexOf('e') + alphabet.length()) % alphabet.length();

        return key;
    }
}

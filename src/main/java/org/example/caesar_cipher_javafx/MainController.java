package org.example.caesar_cipher_javafx;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController {

    @FXML
    private TextField filePathField;

    @FXML
    private Button browseButton;

    @FXML
    private RadioButton encryptMode;

    @FXML
    private RadioButton decryptMode;

    @FXML
    private RadioButton bruteForceMode;

    @FXML
    private RadioButton statisticalAnalysisMode;

    @FXML
    private TextField keyField;

    @FXML
    private Button processButton;

    @FXML
    private TextArea outputArea;

    private String filePath;
    private int mode;
    private int key;

    @FXML
    private void initialize() {
        ToggleGroup modeGroup = new ToggleGroup();
        encryptMode.setToggleGroup(modeGroup);
        decryptMode.setToggleGroup(modeGroup);
        bruteForceMode.setToggleGroup(modeGroup);
        statisticalAnalysisMode.setToggleGroup(modeGroup);

        encryptMode.setSelected(true);
        handleModeChange();
    }

    @FXML
    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовый файл ", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            filePathField.setText(filePath);
        }
    }

    @FXML
    private void handleModeChange() {
        boolean needKey = encryptMode.isSelected() || decryptMode.isSelected();
        keyField.setVisible(needKey);
    }

    @FXML
    private void handleProcess() {
        if (filePath == null || filePath.isEmpty()) {
            outputArea.setText("Выберите файл.");
            return;
        }

        if (encryptMode.isSelected()) {
            mode = 1;
        } else if (decryptMode.isSelected()) {
            mode = 2;
        } else if (bruteForceMode.isSelected()) {
            mode = 3;
        } else if (statisticalAnalysisMode.isSelected()) {
            mode = 4;
        }

        if (keyField.isVisible()) {
            try {
                key = Integer.parseInt(keyField.getText());
            } catch (NumberFormatException e) {
                outputArea.setText("Неверный ключ. Введите целое число.");
                return;
            }
        }

        CipherProcessor processor = new CipherProcessor(filePath, key);

        switch (mode) {
            case 1:
                processor.encryptFile();
                outputArea.setText("Файл зашифрован и сохранен как " + filePath + ".encrypted");
                break;
            case 2:
                processor.decryptFile();
                outputArea.setText("Файл расшифрован и сохранен как " + filePath + ".decrypted");
                break;
            case 3:
                processor.bruteForceDecrypt();
                outputArea.setText("Расшифровка методом перебора завершена. Проверьте выходной файл.");
                break;
            case 4:
                processor.statisticalAnalysisDecrypt();
                outputArea.setText("Расшифровка статистического анализа завершена. Проверьте выходной файл.");
                break;
            default:
                outputArea.setText("Недопустимый режим.");
        }
    }
}
package notepad;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import javafx.stage.FileChooser;

public class JavaApplication1 {
    private TextArea textArea;
    private File currentFile;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Notepad");

        textArea = new TextArea();
        textArea.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> {
            textArea.clear();
            currentFile = null;
        });

        MenuItem openItem = new MenuItem("Open...");
        openItem.setOnAction(e -> openFile(primaryStage));

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveItem.setOnAction(e -> saveFile());

        MenuItem saveAsItem = new MenuItem("Save As...");
        saveAsItem.setOnAction(e -> saveFileAs(primaryStage));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> primaryStage.close());

        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);

        Menu editMenu = new Menu("Edit");
        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setOnAction(e -> textArea.cut());

        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(e -> textArea.copy());

        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setOnAction(e -> textArea.paste());

        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setOnAction(e -> textArea.undo());

        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setOnAction(e -> textArea.redo());

        editMenu.getItems().addAll(cutItem, copyItem, pasteItem, new SeparatorMenuItem(), undoItem, redoItem);
        menuBar.getMenus().addAll(fileMenu, editMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(textArea);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.appendText(line + "\n");
                }
                currentFile = file;
            } catch (IOException e) {
                showAlert("Error", "Cannot open file.");
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            writeFile(currentFile);
        } else {
            saveFileAs(new Stage());
        }
    }

    private void saveFileAs(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("Untitled.txt");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            writeFile(file);
            currentFile = file;
        }
    }

    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
        } catch (IOException e) {
            showAlert("Error", "Cannot save file.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
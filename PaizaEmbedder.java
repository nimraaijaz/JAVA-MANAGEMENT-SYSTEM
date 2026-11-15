package console;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.*;
import javafx.geometry.Insets;

public class PaizaEmbedder {
    private TextArea codeArea;
    private TextArea outputArea;

    public void start(Stage primaryStage) {
        codeArea = new TextArea();
        codeArea.setPrefHeight(300);
        codeArea.setText(
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Hello, JavaFX Code Runner!\");\n" +
            "    }\n" +
            "}"
        );

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(200);

        Button runButton = new Button("Run Code");
        runButton.setOnAction(e -> runCode());

        VBox root = new VBox(10, new Label("Write Java Code:"), codeArea, runButton, 
                          new Label("Output:"), outputArea);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("JavaFX Code Runner");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runCode() {
        try {
            Path tempDir = Files.createTempDirectory("javafx-code");
            Path javaFile = tempDir.resolve("main.java");

            BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile.toFile()));
            writer.write(codeArea.getText());
            writer.close();

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                outputArea.setText("Error: Java Compiler not found. Make sure you're using a JDK, not a JRE.");
                return;
            }

            int result = compiler.run(null, null, null, javaFile.toString());
            if (result != 0) {
                outputArea.setText("Compilation Error");
                return;
            }

            ProcessBuilder pb = new ProcessBuilder("java", "-cp", tempDir.toString(), "Main");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            outputArea.setText(output.toString());

        } catch (IOException e) {
            outputArea.setText("Runtime Error: " + e.getMessage());
        }
    }
}
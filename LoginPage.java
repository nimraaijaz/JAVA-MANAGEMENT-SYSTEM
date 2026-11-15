package myapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import quiz.JavaManagementSystem;

public class LoginPage extends Application {
    private JavaManagementSystem mainApp;
    
    public void start(Stage primaryStage, JavaManagementSystem mainApp) {
        this.mainApp = mainApp;
        setupLoginUI(primaryStage);
    }

    private void setupLoginUI(Stage primaryStage) {
        Label lblUsername = new Label("Username (First Last):");
        TextField txtUsername = new TextField();

        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();

        Button btnLogin = new Button("Login");
        Label lblMessage = new Label();

        btnLogin.setOnAction(e -> {
            String enteredUsername = txtUsername.getText();
            String enteredPassword = txtPassword.getText();
            boolean isValid = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2 && enteredUsername.equals(parts[0]) && enteredPassword.equals(parts[1])) {
                        isValid = true;
                    }
                }
            } catch (IOException ex) {
                lblMessage.setText("Error reading data.");
                lblMessage.setStyle("-fx-text-fill: red;");
                return;
            }

            if (isValid) {
                lblMessage.setText("Login successful! Closing...");
                lblMessage.setStyle("-fx-text-fill: green;");
                
                // Close after delay and notify main app
                new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                mainApp.handleSuccessfulLogin(enteredUsername);
                                primaryStage.close();
                            });
                        }
                    },
                    1000
                );
            } else {
                lblMessage.setText("Invalid username or password!");
                lblMessage.setStyle("-fx-text-fill: red;");
            }
        });

        VBox root = new VBox(10, lblUsername, txtUsername, lblPassword, txtPassword, btnLogin, lblMessage);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
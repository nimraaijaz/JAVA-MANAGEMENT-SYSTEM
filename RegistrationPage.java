package myapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import quiz.JavaManagementSystem;

public class RegistrationPage extends Application {
    private JavaManagementSystem mainApp;
    
    public void start(Stage primaryStage, JavaManagementSystem mainApp) {
        this.mainApp = mainApp;
        setupRegistrationUI(primaryStage);
    }

    private void setupRegistrationUI(Stage primaryStage) {
        Label lblFirstName = new Label("First Name:");
        TextField txtFirstName = new TextField();

        Label lblLastName = new Label("Last Name:");
        TextField txtLastName = new TextField();

        Label lblGender = new Label("Gender:");
        ComboBox<String> cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("Male", "Female", "Other");

        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();

        Button btnRegister = new Button("Register");
        Label lblMessage = new Label();

        btnRegister.setOnAction(e -> {
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String gender = cmbGender.getValue();
            String password = txtPassword.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || gender == null || password.isEmpty()) {
                lblMessage.setText("Please fill all fields.");
                lblMessage.setStyle("-fx-text-fill: red;");
            } else {
                try (FileWriter writer = new FileWriter("UserData.txt")) {
                    writer.write(firstName + " " + lastName + "," + password);
                    lblMessage.setText("Registration successful! Switching to login...");
                    lblMessage.setStyle("-fx-text-fill: green;");
                    
                    // Switch to login after delay
                    new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                javafx.application.Platform.runLater(() -> {
                                    primaryStage.close();
                                    mainApp.showLoginPage();
                                });
                            }
                        },
                        1000
                    );
                } catch (IOException ex) {
                    lblMessage.setText("Error saving data.");
                    lblMessage.setStyle("-fx-text-fill: red;");
                }
            }
        });

        VBox root = new VBox(10, lblFirstName, txtFirstName, lblLastName, txtLastName,
                lblGender, cmbGender, lblPassword, txtPassword, btnRegister, lblMessage);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Registration Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
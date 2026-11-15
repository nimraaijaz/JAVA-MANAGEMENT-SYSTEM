package quiz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.*;
import javafx.event.ActionEvent;
import myapp.LoginPage;
import myapp.RegistrationPage;
import console.PaizaEmbedder;
import notepad.JavaApplication1;

public class JavaManagementSystem extends Application {
    private BorderPane root;
    private VBox navBar;
    private Pane contentPane;
    private boolean isLoggedIn = false;
    private String currentUsername = "";
    
    // Data structures for topics and quiz
    private final Map<String, Topic> topicsMap = new LinkedHashMap<>();
    private final List<QuizQuestion> quizQuestionsList = new ArrayList<>();
    private int quizTotalQuestions = 0;
    private int quizCurrentIndex = 0;
    private int quizScore = 0;
    private String quizLevel = "Easy";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java Management System");
        root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6f8;");

        initTopicsData();
        navBar = createNavBar();
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(20));

        root.setLeft(navBar);
        root.setCenter(contentPane);
        showHomePage();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

private VBox createNavBar() {
    VBox nav = new VBox();
    nav.setPadding(new Insets(20));
    nav.setSpacing(15);
    nav.setStyle("-fx-background-color: #2c3e50;");

    Label title = new Label("Java Management");
    title.setFont(Font.font("Arial", 22));
    title.setTextFill(Color.WHITE);
    title.setPadding(new Insets(10, 0, 20, 0));

    Button btnHome = makeNavButton("Home");
    Button btnQuiz = makeNavButton("Quiz");
    Button btnConsole = makeNavButton("Console");
    Button btnNotes = makeNavButton("Notes");
    Button btnTopics = makeNavButton("Topics");
    Button btnLogin = makeNavButton("Login");
    Button btnRegister = makeNavButton("Register");

    // Disable features until login
    btnQuiz.setDisable(true);
    btnConsole.setDisable(true);
    btnNotes.setDisable(true);
    btnTopics.setDisable(true);

    btnHome.setOnAction(e -> showHomePage());
    btnQuiz.setOnAction(e -> showQuizSetupPage());
    
    // Fixed Console button action
    btnConsole.setOnAction(e -> {
        if (isLoggedIn) {
            Stage consoleStage = new Stage();
            new PaizaEmbedder().start(consoleStage);
        } else {
            showAlert("Login Required", "Please login to access the Console.");
        }
    });
    
    btnNotes.setOnAction(e -> showNotesPage());
    btnTopics.setOnAction(e -> showTopicsPage());
    btnLogin.setOnAction(e -> showLoginPage());
    btnRegister.setOnAction(e -> showRegistrationPage());

    nav.getChildren().addAll(title, btnHome, btnQuiz, btnConsole, btnNotes, btnTopics, 
                          new Separator(), btnLogin, btnRegister);
    return nav;
}



    // ... [rest of the existing methods remain unchanged]
    private Button makeNavButton(String text) {
        Button b = new Button(text);
        b.setPrefWidth(140);
        b.setFont(Font.font("Arial", 16));
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        return b;
    }

    public void showLoginPage() {
        Stage loginStage = new Stage();
        LoginPage loginPage = new LoginPage();
        loginPage.start(loginStage, this);
    }

    private void showRegistrationPage() {
        Stage regStage = new Stage();
        RegistrationPage regPage = new RegistrationPage();
        regPage.start(regStage, this);
    }

    public void handleSuccessfulLogin(String username) {
        this.isLoggedIn = true;
        this.currentUsername = username;
        
        // Enable all features
        navBar.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (!btn.getText().equals("Login") && !btn.getText().equals("Register")) {
                    btn.setDisable(false);
                }
            }
        });
        
        showHomePage();
    }
    private void initTopicsData() {
        topicsMap.put("Variables",
                new Topic("Variables",
                        "Variables are containers that hold data values. In Java, each variable must be declared with a data type.",
                        "int x = 5;\nString name = \"Java\";",
                        "What is the correct way to declare a variable in Java?",
                        new String[]{"var x = 5;", "int x = 5;", "x = 5;", "declare x = 5;"},
                        1,
                        "Easy"
                ));

        topicsMap.put("Loops",
                new Topic("Loops",
                        "Loops are used to repeat a block of code multiple times. Common loops: for, while, do-while.",
                        "for(int i=0; i<5; i++) {\n    System.out.println(i);\n}",
                        "Which loop will always execute at least once?",
                        new String[]{"for loop", "while loop", "do-while loop", "none of these"},
                        2,
                        "Easy"
                ));

        topicsMap.put("Classes and Objects",
                new Topic("Classes and Objects",
                        "Classes are blueprints for objects. Objects are instances of classes in Java.",
                        "public class Car {\n    String model;\n    public Car(String model) {\n        this.model = model;\n    }\n}",
                        "What keyword is used to create an object in Java?",
                        new String[]{"class", "new", "object", "create"},
                        1,
                        "Easy"
                ));

        topicsMap.put("Inheritance",
                new Topic("Inheritance",
                        "Inheritance allows a class to inherit properties and methods from another class.",
                        "public class Animal {}\npublic class Dog extends Animal {}",
                        "Which keyword is used to inherit a class?",
                        new String[]{"super", "extends", "inherits", "implements"},
                        1,
                        "Medium"
                ));

        topicsMap.put("Interfaces",
                new Topic("Interfaces",
                        "Interfaces provide a way to achieve abstraction in Java. A class implements an interface.",
                        "public interface Drawable {\n    void draw();\n}",
                        "How does a class implement an interface?",
                        new String[]{"using extends keyword", "using implements keyword", "using inherits keyword", "using interface keyword"},
                        1,
                        "Medium"
                ));

        // Additional OOP topics - Medium and Hard
        topicsMap.put("Polymorphism",
                new Topic("Polymorphism",
                        "Polymorphism allows methods to do different things based on the object it is acting upon, primarily achieved via method overriding and overloading.",
                        "Animal a = new Dog();\na.speak(); // Calls Dog's speak method",
                        "What is runtime polymorphism in Java?",
                        new String[]{"Method Overloading", "Method Overriding", "Operator Overloading", "Constructor Overloading"},
                        1,
                        "Medium"
                ));

        topicsMap.put("Encapsulation",
                new Topic("Encapsulation",
                        "Encapsulation is the bundling of data and methods that operate on the data within one unit, usually a class, and restricting access using access modifiers.",
                        "private int age;\npublic int getAge() { return age; }",
                        "Which access modifier allows access only within the same class?",
                        new String[]{"public", "private", "protected", "default"},
                        1,
                        "Easy"
                ));

        topicsMap.put("Abstraction",
                new Topic("Abstraction",
                        "Abstraction is the concept of hiding the complex reality while exposing only the necessary parts. In Java, achieved via abstract classes and interfaces.",
                        "abstract class Animal { abstract void sound(); }",
                        "Which keyword is used to declare an abstract class in Java?",
                        new String[]{"abstract", "interface", "final", "static"},
                        0,
                        "Medium"
                ));

        topicsMap.put("Constructors",
                new Topic("Constructors",
                        "Constructors are special methods called when an object is instantiated to initialize it.",
                        "public class Car {\n    public Car() { System.out.println(\"Car created\"); }\n}",
                        "What is true about constructors in Java?",
                        new String[]{"They have no return type", "They must have a void return type", "They return int", "They can be static"},
                        0,
                        "Easy"
                ));

        topicsMap.put("Method Overloading",
                new Topic("Method Overloading",
                        "Method overloading is having multiple methods in the same class with the same name but different parameters.",
                        "void print(int a) {}\nvoid print(String b) {}",
                        "Which of the following best describes method overloading?",
                        new String[]{"Same method name with same parameters", "Same method name with different parameters", "Different method name with same parameters", "Different class"},
                        1,
                        "Easy"
                ));

        topicsMap.put("Method Overriding",
                new Topic("Method Overriding",
                        "Method overriding means defining a method in a subclass that already exists in the parent class with the same signature.",
                        "@Override\npublic void speak() { System.out.println(\"Dog barks\"); }",
                        "Which annotation is used to indicate method overriding in Java?",
                        new String[]{"@Override", "@Overload", "@Implemented", "@Overridden"},
                        0,
                        "Medium"
                ));

        topicsMap.put("Access Modifiers",
                new Topic("Access Modifiers",
                        "Access modifiers set the accessibility of classes, methods, and variables: public, private, protected, and default.",
                        "public int x;\nprivate int y;\nprotected int z;",
                        "Which access modifier allows access within the same package and subclasses?",
                        new String[]{"public", "private", "protected", "default"},
                        2,
                        "Easy"
                ));

        topicsMap.put("Packages",
                new Topic("Packages",
                        "Packages are used to group related classes and interfaces which helps to avoid name conflicts and to control access.",
                        "package com.example.java;",
                        "Which keyword is used to import a package in Java?",
                        new String[]{"package", "import", "include", "namespace"},
                        1,
                        "Easy"
                ));

        topicsMap.put("Exception Handling",
                new Topic("Exception Handling",
                        "Exception handling allows a program to handle runtime errors via try, catch and finally blocks.",
                        "try { int a = 5/0; } catch (ArithmeticException e) { e.printStackTrace(); }",
                        "Which block is used to handle exceptions?",
                        new String[]{"try", "catch", "finally", "throw"},
                        1,
                        "Hard"
                ));

        topicsMap.put("Generics",
                new Topic("Generics",
                        "Generics enable types (classes and interfaces) to be parameters when defining classes, interfaces and methods.",
                        "public class Box<T> {\n    private T t;\n    public void set(T t) { this.t = t; }\n    public T get() { return t; }\n}",
                        "What does T represent in generics?",
                        new String[]{"Type parameter", "Method", "Variable", "Class"},
                        0,
                        "Hard"
                ));
    }
  private void showQuizSetupPage() {
        VBox setupBox = new VBox();
        setupBox.setAlignment(Pos.CENTER);
        setupBox.setSpacing(20);

        Label title = new Label("Java Quiz Setup");
        title.setFont(Font.font("Arial", 28));
        title.setTextFill(Color.web("#34495e"));

        Label numberLabel = new Label("How many questions do you want to answer?");
        numberLabel.setFont(Font.font("Arial", 16));

        TextField numberInput = new TextField();
        numberInput.setPromptText("Enter a number from 1 to " + topicsMap.size());
        numberInput.setMaxWidth(200);

        Label levelLabel = new Label("Select difficulty level:");
        levelLabel.setFont(Font.font("Arial", 16));

        ComboBox<String> levelCombo = new ComboBox<>();
        levelCombo.getItems().addAll("Easy", "Medium", "Hard");
        levelCombo.setValue("Easy");

        Button startBtn = new Button("Start Quiz");
        startBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        startBtn.setOnMouseEntered(e -> startBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;"));
        startBtn.setOnMouseExited(e -> startBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;"));

        final Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#e74c3c"));
        errorLabel.setFont(Font.font("Arial", 14));

        startBtn.setOnAction(e -> {
            String numText = numberInput.getText();
            if (numText == null || numText.trim().isEmpty()) {
                errorLabel.setText("Please enter the number of questions.");
                return;
            }
            int num;
            try {
                num = Integer.parseInt(numText.trim());
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid number format.");
                return;
            }
            if (num < 1 || num > topicsMap.size()) {
                errorLabel.setText("Number must be between 1 and " + topicsMap.size());
                return;
            }
            quizTotalQuestions = num;
            quizLevel = levelCombo.getValue();
            prepareQuizQuestions();
            showQuizQuestionPage();
        });

        setupBox.getChildren().addAll(title, numberLabel, numberInput, levelLabel, levelCombo, startBtn, errorLabel);
        setContent(setupBox);
    }

    private void prepareQuizQuestions() {
        quizQuestionsList.clear();
        quizScore = 0;
        quizCurrentIndex = 0;

        List<Topic> filteredTopics = new ArrayList<>();
        topicsMap.values().stream().filter((t) -> (compareDifficulty(t.getLevel(), quizLevel) <= 0)).forEachOrdered((t) -> {
            filteredTopics.add(t);
        });
        Collections.shuffle(filteredTopics);

        int limit = Math.min(quizTotalQuestions, filteredTopics.size());

        for (int i = 0; i < limit; i++) {
            Topic t = filteredTopics.get(i);
            quizQuestionsList.add(new QuizQuestion(t.getMcqQuestion(), t.getMcqOptions(), t.getMcqCorrectAnswerIndex()));
        }
    }

    private int compareDifficulty(String d1, String d2) {
        List<String> levels = Arrays.asList("Easy", "Medium", "Hard");
        return levels.indexOf(d1) - levels.indexOf(d2);
    }

    private void showQuizQuestionPage() {
        if (quizCurrentIndex >= quizQuestionsList.size()) {
            showQuizResultPage();
            return;
        }

        QuizQuestion currentQ = quizQuestionsList.get(quizCurrentIndex);

        VBox quizBox = new VBox();
        quizBox.setAlignment(Pos.TOP_LEFT);
        quizBox.setPadding(new Insets(20));
        quizBox.setSpacing(15);

        Label questionLabel = new Label("Question " + (quizCurrentIndex + 1) + " of " + quizQuestionsList.size());
        questionLabel.setFont(Font.font("Arial", 18));
        questionLabel.setTextFill(Color.web("#34495e"));

        Label questionText = new Label(currentQ.getQuestion());
        questionText.setFont(Font.font("Arial", 20));
        questionText.setWrapText(true);

        ToggleGroup optionsGroup = new ToggleGroup();
        VBox optionsBox = new VBox();
        optionsBox.setSpacing(10);

        for (String option : currentQ.getOptions()) {
            RadioButton rb = new RadioButton(option);
            rb.setFont(Font.font("Arial", 16));
            rb.setToggleGroup(optionsGroup);
            optionsBox.getChildren().add(rb);
        }

        Label feedbackLabel = new Label("");
        feedbackLabel.setFont(Font.font("Arial", 16));

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(20);

        Button submitBtn = new Button("Submit Answer");
        submitBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Button skipBtn = new Button("Skip");
        skipBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");

        buttonBox.getChildren().addAll(submitBtn, skipBtn);

        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;"));
        skipBtn.setOnMouseEntered(e -> skipBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white;"));
        skipBtn.setOnMouseExited(e -> skipBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;"));

        submitBtn.setOnAction((ActionEvent e) -> {
            RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
            if (selected == null) {
                feedbackLabel.setTextFill(Color.web("#e74c3c"));
                feedbackLabel.setText("Please select an answer before submitting.");
                return;
            }
            int selectedIndex = -1;
            for (int i = 0; i < optionsBox.getChildren().size(); i++) {
                if (optionsBox.getChildren().get(i) == selected) {
                    selectedIndex = i;
                    break;
                }
            }
            if (selectedIndex == currentQ.getCorrectAnswerIndex()) {
                feedbackLabel.setTextFill(Color.web("#27ae60"));
                feedbackLabel.setText("Correct!");
                quizScore++;
            } else {
                feedbackLabel.setTextFill(Color.web("#e74c3c"));
                feedbackLabel.setText("Wrong answer.");
            }
            optionsBox.getChildren().forEach((node) -> {
                node.setDisable(true);
            });
            submitBtn.setDisable(true);
            skipBtn.setDisable(true);
            
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                quizCurrentIndex++;
                showQuizQuestionPage();
            });
            pause.play();
        });

        skipBtn.setOnAction(e -> {
            quizCurrentIndex++;
            showQuizQuestionPage();
        });

        quizBox.getChildren().addAll(questionLabel, questionText, optionsBox, feedbackLabel, buttonBox);

        setContent(quizBox);
    }

    private void showQuizResultPage() {
        VBox resultBox = new VBox();
        resultBox.setAlignment(Pos.CENTER);
        resultBox.setSpacing(20);

        Label resultLabel = new Label("Quiz Completed!");
        resultLabel.setFont(Font.font("Arial", 28));
        resultLabel.setTextFill(Color.web("#34495e"));

        Label scoreLabel = new Label("Your score: " + quizScore + " out of " + quizQuestionsList.size());
        scoreLabel.setFont(Font.font("Arial", 22));
        scoreLabel.setTextFill(Color.web("#27ae60"));

        Button retryBtn = new Button("Retry Quiz");
        retryBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Button homeBtn = new Button("Back to Home");
        homeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");

        retryBtn.setOnMouseEntered(e -> retryBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
        retryBtn.setOnMouseExited(e -> retryBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;"));

        homeBtn.setOnMouseEntered(e -> homeBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white;"));
        homeBtn.setOnMouseExited(e -> homeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;"));

        retryBtn.setOnAction(e -> showQuizSetupPage());
        homeBtn.setOnAction(e -> showHomePage());

        resultBox.getChildren().addAll(resultLabel, scoreLabel, retryBtn, homeBtn);

        setContent(resultBox);
    }

    private void showHomePage() {
        VBox homeBox = new VBox();
        homeBox.setAlignment(Pos.CENTER);
        homeBox.setSpacing(20);

        Label welcome = new Label("Welcome to Java Management System");
        welcome.setFont(Font.font("Arial", 28));
        welcome.setTextFill(Color.web("#34495e"));

        Label instruction = new Label("Use the navigation bar to explore Topics, Quiz, Console, and Notes.");
        instruction.setFont(Font.font("Arial", 16));
        instruction.setTextFill(Color.web("#7f8c8d"));

        homeBox.getChildren().addAll(welcome, instruction);

        setContent(homeBox);
    }

private void showConsolePage() {
    if (isLoggedIn) {
        Stage consoleStage = new Stage();
        new PaizaEmbedder().start(consoleStage);
    } else {
        showAlert("Login Required", "Please login to access the Console.");
    }
}

 private void showNotesPage() {
    if (isLoggedIn) {
        Stage notesStage = new Stage();
        new JavaApplication1().start(notesStage);
    } else {
        showAlert("Login Required", "Please login to access the Notes.");
    }
}

private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    private void showTopicsPage() {
        VBox topicsBox = new VBox();
        topicsBox.setAlignment(Pos.TOP_LEFT);
        topicsBox.setSpacing(10);

        Label title = new Label("Java Topics");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.web("#2c3e50"));

        topicsBox.getChildren().add(title);

        topicsMap.keySet().stream().map((key) -> {
            Button topicBtn = new Button(key);
            topicBtn.setFont(Font.font("Arial", 16));
            topicBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
            topicBtn.setMinWidth(200);
            topicBtn.setOnMouseEntered(e -> topicBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
            topicBtn.setOnMouseExited(e -> topicBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;"));
            topicBtn.setOnAction(e -> showTopicDetailPage(topicsMap.get(key)));
            return topicBtn;
        }).forEachOrdered((topicBtn) -> {
            topicsBox.getChildren().add(topicBtn);
        });

        ScrollPane scrollPane = new ScrollPane(topicsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        setContent(scrollPane);
    }

    private void showTopicDetailPage(Topic topic) {
        VBox detailBox = new VBox();
        detailBox.setSpacing(15);
        detailBox.setPadding(new Insets(10));
        detailBox.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(topic.getTitle());
        title.setFont(Font.font("Arial", 26));
        title.setTextFill(Color.web("#2c3e50"));

        Label desc = new Label(topic.getDescription());
        desc.setFont(Font.font("Arial", 16));
        desc.setWrapText(true);

        TextArea codeArea = new TextArea(topic.getCode());
        codeArea.setFont(Font.font("Consolas", 14));
        codeArea.setEditable(false);
        codeArea.setStyle("-fx-control-inner-background:#ecf0f1;");
        codeArea.setWrapText(true);
        codeArea.setPrefHeight(120);

        Label mcqLabel = new Label("Quiz: " + topic.getMcqQuestion());
        mcqLabel.setFont(Font.font("Arial", 18));
        mcqLabel.setTextFill(Color.web("#34495e"));

        ToggleGroup answersGroup = new ToggleGroup();
        VBox answersBox = new VBox();
        answersBox.setSpacing(8);

        for (String mcqOption : topic.getMcqOptions()) {
            RadioButton rb = new RadioButton(mcqOption);
            rb.setFont(Font.font("Arial", 15));
            rb.setToggleGroup(answersGroup);
            answersBox.getChildren().add(rb);
        }

        Label answerResultLabel = new Label("");
        answerResultLabel.setFont(Font.font("Arial", 16));
        answerResultLabel.setTextFill(Color.web("#27ae60"));

        Button checkAnswerBtn = new Button("Check Answer");
        checkAnswerBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        checkAnswerBtn.setOnMouseEntered(e -> checkAnswerBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;"));
        checkAnswerBtn.setOnMouseExited(e -> checkAnswerBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;"));

        checkAnswerBtn.setOnAction(e -> {
            RadioButton selected = (RadioButton) answersGroup.getSelectedToggle();
            if (selected == null) {
                answerResultLabel.setTextFill(Color.web("#e74c3c"));
                answerResultLabel.setText("Please select an answer.");
            } else {
                int selectedIndex = -1;
                for (int i = 0; i < answersBox.getChildren().size(); i++) {
                    if (answersBox.getChildren().get(i) == selected) {
                        selectedIndex = i;
                        break;
                    }
                }
                if (selectedIndex == topic.getMcqCorrectAnswerIndex()) {
                    answerResultLabel.setTextFill(Color.web("#27ae60"));
                    answerResultLabel.setText("Correct! Well done.");
                } else {
                    answerResultLabel.setTextFill(Color.web("#e74c3c"));
                    answerResultLabel.setText("Wrong answer. Try again.");
                }
            }
        });

        Button backBtn = new Button("Back to Topics");
        backBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;"));
        backBtn.setOnAction(e -> showTopicsPage());

        detailBox.getChildren().addAll(title, desc, codeArea, mcqLabel, answersBox, checkAnswerBtn, answerResultLabel, backBtn);

        ScrollPane scrollPane = new ScrollPane(detailBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        setContent(scrollPane);
    }

    private void setContent(javafx.scene.Node node) {
        contentPane.getChildren().clear();
        if (node instanceof Pane) {
            contentPane = (Pane) node;
            root.setCenter(contentPane);
        } else {
            StackPane stack = new StackPane();
            stack.getChildren().add(node);
            root.setCenter(stack);
            contentPane = stack;
        }
    }

    // Topic class for detailed topics
    private static class Topic {
        private final String title;
        private final String description;
        private final String code;
        private final String mcqQuestion;
        private final String[] mcqOptions;
        private final int mcqCorrectAnswerIndex;
        private final String level;

        public Topic(String title, String description, String code,
                     String mcqQuestion, String[] mcqOptions,
                     int mcqCorrectAnswerIndex, String level) {
            this.title = title;
            this.description = description;
            this.code = code;
            this.mcqQuestion = mcqQuestion;
            this.mcqOptions = mcqOptions;
            this.mcqCorrectAnswerIndex = mcqCorrectAnswerIndex;
            this.level = level;
        }

        public String getTitle() {
            return title;
        }
        public String getDescription() {
            return description;
        }
        public String getCode() {
            return code;
        }
        public String getMcqQuestion() {
            return mcqQuestion;
        }
        public String[] getMcqOptions() {
            return mcqOptions;
        }
        public int getMcqCorrectAnswerIndex() {
            return mcqCorrectAnswerIndex;
        }
        public String getLevel() {
            return level;
        }
    }

    // QuizQuestion class for quiz section
    private static class QuizQuestion {
        private final String question;
        private final String[] options;
        private final int correctAnswerIndex;

        public QuizQuestion(String question, String[] options, int correctAnswerIndex) {
            this.question = question;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public String getQuestion() {
            return question;
        }
        public String[] getOptions() {
            return options;
        }
        public int getCorrectAnswerIndex() {
            return correctAnswerIndex;
        }
    }
}
package UI;

import Users.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class StartUI extends UI {
    private AdminUI adminUI;
    private StudentUI studentUI;
    private SuperAdminUI superAdminUI;
    private TeacherUI teacherUI;

    //Business logic
    protected User user;

    public static void main(String[] args) {
        Application.launch(StartUI.class, args);
    }


    private void setPrincipalSceneAsStudent(){
        studentUI = new StudentUI(primaryStage, login, userID);
        primaryStage.setScene(studentUI.createPrincipalStudentScene());
    }

    private void setPrincipalSceneAsTeacher(){
        teacherUI = new TeacherUI(primaryStage, login, userID);
        primaryStage.setScene(teacherUI.createPrincipalTeacherScene());
    }

    private void setPrincipalSceneAsAdmin(){
        adminUI = new AdminUI(primaryStage, login, userID);
        primaryStage.setScene(adminUI.createPrincipalAdminScene());
    }

    private void setPrincipalSceneAsSuperAdmin(){
        superAdminUI = new SuperAdminUI(primaryStage, login, userID);
        primaryStage.setScene(superAdminUI.createPrincipalSuperAdminScene());
    }


    /**
     * Creates the instance of the User, for the business logic relative to it
     * @throws IOException
     */
    private void createUser() throws IOException{
        if(user==null){
            String host = "localhost";
            int port = 5555;
            user = new User(host, port, this);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupListeners();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Learn Together - Connection");
        primaryStage.setAlwaysOnTop(true);
        // Scene 2
        // Create the registration form grid pane
        root = createPrincipalPane();
        // Create a scene with registration form grid pane as the root node
        // Scene 1
        // Create the registration form grid pane
        GridPane gridPaneConnection = createConnectionFormPane();
        // Add UI controls to the registration form grid pane
        addUIControlsConnectionPane(gridPaneConnection);
        // Create a scene with registration form grid pane as the root node
        this.connectionScene = new Scene(gridPaneConnection, 800, 500);
        // Set the scene in primary stage
        this.primaryStage.setScene(connectionScene);
        // Set the stage visible
        this.primaryStage.show();

        GridPane gridPaneFirstConnection = createConnectionFormPane();
        addUIControlsFirstConnectionPane(gridPaneFirstConnection);
        firstConnectionScene = new Scene(gridPaneFirstConnection, 800, 500);

        BorderPane borderPaneWaiting = createWaitingPane();
        addUIControlsWaitingPane(borderPaneWaiting);
        waitingScene = new Scene(borderPaneWaiting, 800, 500);


    }

    @Override
    protected void setupListeners(){
        connectionStatus.addListener((observable, oldValue, newValue) -> {
            if(newValue.equalsIgnoreCase("STUDENT")){
                Platform.runLater(() -> showAlert(Alert.AlertType.CONFIRMATION, null, "Success", "Connected."));
                Platform.runLater(this::setPrincipalSceneAsStudent);
            }
            else if(newValue.equalsIgnoreCase("TEACHER")){
                Platform.runLater(this::setPrincipalSceneAsTeacher);
            }
            else if(newValue.equalsIgnoreCase("ADMIN")){
                Platform.runLater(this::setPrincipalSceneAsAdmin);
            }
            else if(newValue.equalsIgnoreCase("SUPER ADMIN")){
                Platform.runLater(this::setPrincipalSceneAsSuperAdmin);
            }
            else if(newValue.equalsIgnoreCase("CONNECTION ERROR")){
                Platform.runLater(()->primaryStage.setScene(connectionScene));
                showAlert(Alert.AlertType.ERROR, null, "Failure", "Error: impossible to connect.");
            }
            else if(newValue.equalsIgnoreCase("WAITING")){
                Platform.runLater(() -> primaryStage.setScene(waitingScene));
            }

        });

        currentState.addListener((observable, oldValue, newValue) -> {
            if(newValue.equalsIgnoreCase("FC SUCCESS")){
                Platform.runLater(()-> showAlert(Alert.AlertType.CONFIRMATION, null, "Success", "This account has been activated. You can now log in properly."));
                Platform.runLater(()->primaryStage.setScene(connectionScene));
            }
            else if(newValue.equalsIgnoreCase("FC FAILURE")){
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, null, "Failure", "Error: impossible to activate your account. Is it already activated? Otherwise, please check later."));
                Platform.runLater(()->primaryStage.setScene(connectionScene));
            }
        });
    }

    /**
     * Create an instance of GridPane and apply format on it.
     *
     * @return GridPane Login pane.
     */
    protected GridPane createConnectionFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }


    /**
     * Design the primary scene with the connection interface and handle the events.
     *
     * @param gridPane Login pane.
     */
    protected void addUIControlsConnectionPane(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Connection");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Name Label
        Label nameLabel = new Label("Email address: ");
        gridPane.add(nameLabel, 0,1);

        // Add Name Text Field
        TextField nameField = new TextField("yvan.sanson@etu.umontpellier.fr");
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1,1);

        // Add Password Label
        Label passwordLabel = new Label("Password: ");
        gridPane.add(passwordLabel, 0, 2);

        // Add Password Text Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 2);

        // Add Login Button
        Button loginButton = new Button("Login");
        loginButton.setPrefHeight(40);
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(100);
        gridPane.add(loginButton, 0, 4, 1, 1);
        GridPane.setHalignment(loginButton, HPos.RIGHT);
        GridPane.setMargin(loginButton, new Insets(20, 0,20,0));
        //Add FirstConnection Button
        Button firstConnectionButton = new Button("First connection?");
        firstConnectionButton.setDefaultButton(false);
        firstConnectionButton.setPrefHeight(40);
        firstConnectionButton.setPrefWidth(200);
        gridPane.add(firstConnectionButton, 1, 4, 1, 1);
        GridPane.setHalignment(firstConnectionButton, HPos.RIGHT);
        GridPane.setMargin(firstConnectionButton, new Insets(20, 0, 20, 0));

        loginButton.setOnAction(event -> {
            if(nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your name");
                return;
            }
            if(passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your password");
                return;
            }

            login=nameField.getText();
            password = passwordField.getText();
            connectionStatus.setValue("WAITING");
            try {
                createUser();
                user.handleLogin(login, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        firstConnectionButton.setOnAction(event -> {
            Platform.runLater(()->primaryStage.setScene(firstConnectionScene));
        });
    }

    protected void addUIControlsFirstConnectionPane(GridPane pane){
        // Add Header
        Label headerLabel = new Label("First connection");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        pane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));
        Text howto = new Text("If it is your first connection, enter your email address and your new password in the form below. \nThis only work once, on the first connection.");
        pane.add(howto, 0, 1, 2, 2);
        GridPane.setHalignment(howto, HPos.CENTER);
        GridPane.setMargin(howto,  new Insets(20, 0,20,0));

        // Add Name Label
        Label nameLabel = new Label("Enter you email address: ");
        pane.add(nameLabel, 0,3);

        // Add Name Text Field
        TextField nameField = new TextField("yvan.sanson@etu.umontpellier.fr");
        nameField.setPrefHeight(40);
        pane.add(nameField, 1,3);

        // Add Password Label
        Label passwordLabel = new Label("New password: ");
        pane.add(passwordLabel, 0, 4);

        // Add Password Text Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        pane.add(passwordField, 1, 4);

        //Add Button
        Button loginButton = new Button("Connect");
        loginButton.setPrefHeight(40);
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(100);
        pane.add(loginButton, 0, 5, 2, 1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setMargin(loginButton, new Insets(20, 0,20,0));

        loginButton.setOnAction(event -> {
            if(nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), "Form Error!", "Please enter your name");
                return;
            }
            if(passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), "Form Error!", "Please enter your password");
                return;
            }

            login=nameField.getText();
            password = passwordField.getText();
            connectionStatus.setValue("WAITING");
            try {
                createUser();
                user.setFirstPassword(login, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showLogin(boolean isConnected, int id, String role){
        if(isConnected){
            userID = id;
            switch (role){
                case "STUDENT":
                    connectionStatus.setValue("STUDENT");
                    break;
                case "TEACHER":
                    connectionStatus.setValue("TEACHER");
                    break;
                case "ADMIN":
                    connectionStatus.setValue("ADMIN");
                    break;
                case "SUPER ADMIN":
                    connectionStatus.setValue("SUPER ADMIN");
                    break;
                default:
                    connectionStatus.setValue("CONNECTION ERROR");
                    break;
            }
        }
        else{
            connectionStatus.setValue("CONNECTION ERROR");
        }
    }

    @Override
    public void display(String message){

    }

}


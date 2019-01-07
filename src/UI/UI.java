package UI;

import java.io.IOException;
import java.util.Optional;

import Users.User;
import common.DisplayIF;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public abstract class UI extends Application implements DisplayIF {

    //Scenes and other UI elements
    StackPane root;
	Stage primaryStage;
    Scene connectionScene;
    Scene waitingScene;
    Scene firstConnectionScene;
    StringProperty connectionStatus = new SimpleStringProperty("NOT CONNECTED");
    StringProperty currentState = new SimpleStringProperty("UNDEFINED");


    //Information about the user and the general environment
    protected String login;
    protected String password;
    protected int userID;



	/**
	 * Create the window with the two scene and set the first scene as main.
	 * 
	 * @param primaryStage Frame window.
	 * @throws Exception
	 */
    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    protected void setupListeners(){

    }


    protected BorderPane createWaitingPane(){
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(40, 40, 40, 40));
        return borderPane;
    }

    protected void addUIControlsWaitingPane(BorderPane pane){
        Text waitingText = new Text("Connexion en cours... Veuillez patienter.");
        pane.setCenter(waitingText);
    }



    /**
     *  Create an instance of StackPane and apply format on it.
     *
     * @return StackPane Chat pane.
     */
    protected StackPane createPrincipalPane() {
        // Instantiate a new VBox
    	StackPane root = new StackPane();

        return root;
    }

    /**
     * Show personalized alert to user.
     *
     * @param alertType Format of the alert.
     * @param owner Window on which alert will be displayed.
     * @param title Title to be displayed.
     * @param message Message to be displayed.
     */
    protected void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if(owner != null)
            alert.initOwner(owner);
        else
            alert.initOwner(primaryStage);
        alert.initStyle(StageStyle.DECORATED);
        alert.setResizable(false);
        alert.show();
    }
    @Override
    public void display(String message){

    }
    @Override
    public void setState(String cmd){
        currentState.setValue(cmd);
    }
    public void showLogin(boolean isConnected, int id, String role){

    }

}

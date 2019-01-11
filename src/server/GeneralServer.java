package server;

import com.lloseng.ocsf.server.ConnectionToClient;
import com.lloseng.ocsf.server.OriginatorMessage;
import common.ChatIF;
import server.DBTypes.CourseType;
import server.DBTypes.PromotionType;
import server.DBTypes.RecordType;
import server.DBTypes.UserType;
import com.lloseng.ocsf.server.ObservableOriginatorServer;
import server.DAO.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class GeneralServer implements Observer {

    private ObservableOriginatorServer comm;
    private ChatIF display;
    final private static int DEFAULT_PORT = 5555;
    private Date currentDate;
    private SimpleDateFormat dateFormat;
    private AbstractDAOFactory dao;


    /**
     * The useful constructor
     * @param port: the server address
     * @param display: the server display
     * @throws IOException
     */
    public GeneralServer(int port, ChatIF display) throws IOException {
        comm = new ObservableOriginatorServer(port);
        comm.addObserver(this);
        comm.listen();
        currentDate = new Date();
        dateFormat = new SimpleDateFormat(" '['HH:mm:ss']'");
        this.display=display;
        dao = new SQLServerFactory();
        dao.createDAOUser();
        dao.createDAODepartment();
        display.display("Server is running on port " + port);
    }

    /**
     * This method is called whenever a client sends something to the server.
     * @param msg: the object the client sent.
     * @param client: the original client.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof String) {
            if (((String) msg).startsWith("#"))
                handleInstrFromClient(((String) msg).substring(1), client);
        }
    }

    /**
     * This method is used whenever a message sent by a client starts with '#'
     * @param instruction: what the client sent.
     * @param client: the original client.
     */
    public void handleInstrFromClient(String instruction, ConnectionToClient client) {
        if (instruction.startsWith("LOGIN")) {
            String[] ids = instruction.split(" ");
            handleLoginFromClient(ids[1], ids[2], client);
        }
        else if(instruction.startsWith("FIRSTCONN")){
            String[] creds = instruction.split(" ");
            handleFirstConnectionFromClient(creds[1], creds[2], client);
        }
        else if(instruction.startsWith("CREATEDEP")){
            String[] creds = instruction.split(" ");
            handleCreateDepartmentFromClient(creds[1], creds[2], creds[3], client);
        }
        else if(instruction.startsWith("UPDATEDEP")){
            String[] creds = instruction.split(" ");
            handleUpdateDepartmentFromClient(creds[1], creds[2], creds[3], client);
        }
        else if(instruction.startsWith("DELETEDEP")){
            String[] creds = instruction.split(" ");
            handleDeleteDepartmentFromClient(creds[1], client);
        }
    }


    /**
     * This method is used to send a client a response of a #LOGIN demand.
     * @param isConnected : true if the connection succeeded, false otherwise
     * @param id          : the user ID. value -1 if not connected.
     * @param role        : the user role. Value null if not connected.
     * @param client : the original client.
     */
    public void sendToClientLogin(boolean isConnected, int id, String role, ConnectionToClient client) {
        try {
            if (isConnected) {
                client.sendToClient("#LOGON TRUE " + id + " " + role);
            } else {
                client.sendToClient("#LOGON FALSE -1 null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to send a client a response of a #FIRSTCONN demand.
     * @param hasSucceeded: true id the first connection was a success, false otherwise.
     * @param client: the original client.
     */
    public void sendToClientFirstConn(boolean hasSucceeded, ConnectionToClient client){
        try{
            if(hasSucceeded){
                client.sendToClient("#FIRSTCONN SUCCESS");
            }else{
                client.sendToClient("#FIRSTCONN FAILURE");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method is used when a client wants to connect.
     * It needs to check in the database to see if credentials matches or not.
     * @param login : the email the client provided.
     * @param password: the password the client provided.
     * @param client: the original client.
     */
    public void handleLoginFromClient(String login, String password, ConnectionToClient client) {
        int userID = dao.readDAOUserByLogin(login, password);
        if (userID != -1) {
            checkLogin(userID, client);
        } else {
            sendToClientLogin(false, -1, null, client);
        }
    }

    /**
     * This method is used when a user wants to first connect to the application.
     * @param login: the email the client provided.
     * @param password: the (new) password the client provided.
     * @param client: the original client.
     */
    public void handleFirstConnectionFromClient(String login, String password, ConnectionToClient client){
        if(dao.isPdwNull(login))
            if (dao.setNewPwd(login, password)) {
                sendToClientFirstConn(true, client);
                return;
            }
        sendToClientFirstConn(false, client);
    }

    /**
     * This method is called when a user has the right credentials and connected.
     * It checks in the DB and grabs the user ID and its role.
     * It always sends true on the login, as it has already been checked.
     * @param id: the user ID
     * @param client: the client from which it originated.
     */
    public void checkLogin(int id, ConnectionToClient client) {
        UserType user = dao.readDAOUser(id);
        sendToClientLogin(true, user.getId(), user.getRole(), client);
    }

    /**
     * @param cred
     * @param cred1
     * @param cred2
     * @param client
     */
    public void handleCreateDepartmentFromClient(String cred, String cred1, String cred2, ConnectionToClient client) {
        //dao.createDAODepartment(cred,cred1,cred2);
    }

    /**
     * @param cred
     * @param cred1
     * @param cred2
     * @param client
     */
    public void handleUpdateDepartmentFromClient(String cred, String cred1, String cred2, ConnectionToClient client) {
        //dao.updateDAODepartment(cred,cred1,cred2);
    }

    /**
     * @param cred
     * @param client
     */
    public void handleDeleteDepartmentFromClient(String cred, ConnectionToClient client) {
        //dao.deleteDAODepartment(cred);
    }

    /**
     * @param message
     */
    public void checkSuccess(Boolean message) {
        // TODO implement here
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof OriginatorMessage) {
            if (((OriginatorMessage) arg).getOriginator() == null) {
                if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.SERVER_STARTED)) {
                    this.serverStarted();
                } else if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.SERVER_STOPPED))
                    this.serverStopped();
                else if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.SERVER_CLOSED))
                    this.serverClosed();
                else if (((OriginatorMessage) arg).getMessage().toString().contains(ObservableOriginatorServer.LISTENING_EXCEPTION))
                    this.listeningException(((OriginatorMessage) arg).getMessage());
            } else {
                if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.CLIENT_CONNECTED))
                    this.clientConnected(((OriginatorMessage) arg).getOriginator());
                else if (((OriginatorMessage) arg).getMessage().equals(ObservableOriginatorServer.CLIENT_DISCONNECTED))
                    this.clientDisconnected(((OriginatorMessage) arg).getOriginator());
                else if (((OriginatorMessage) arg).getMessage().toString().contains(ObservableOriginatorServer.CLIENT_EXCEPTION))
                    this.clientException(((OriginatorMessage) arg).getOriginator(), (Throwable) ((OriginatorMessage) arg).getMessage());
                else
                    this.handleMessageFromClient(((OriginatorMessage) arg).getMessage(), ((OriginatorMessage) arg).getOriginator());

            }

        }

    }

    private void clientException(ConnectionToClient originator, Throwable message) {
    }

    private void clientDisconnected(ConnectionToClient originator) {
    }
    private void clientConnected(ConnectionToClient originator) {
    }
    private void listeningException(Object message) {
    }

    private void serverStopped() {
    }

    private void serverStarted() {
    }
    private void serverClosed() {
    }


    public void handleMessageFromServerUI(String message) {
        display.display("No commands have been implemented yet.");
    }
}



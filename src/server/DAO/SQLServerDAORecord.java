package server.DAO;

import Types.RecordType;
import client.Courses.CourseServices;
import Types.UserType;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * This class instantiates the methods relative to the record in SQLServer data base
 * @author Yvan SANSON
 * @author Marie SALELLES
 */
public class SQLServerDAORecord extends AbstractDAORecords{

    /**
     * Default constructor
     */
    public SQLServerDAORecord() {
    }

    /**
     * This method creates the connection with the data base
     * @return : a connection
     */
    @Override
    public Connection getConnection() {
        {
            Connection connection = null;
            String hostName = "learntogether.database.windows.net"; // update me
            String dbName = "LearnTogether"; // update me
            String user = "ysanson"; // update me
            String password = "LearnTogether1"; // update me
            String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                    + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }

    /**
     * This method closes the connection with the data base
     */
    @Override
    public void closeConnection(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** This method creates in the data base a record
     * @param name : record name
     * @param year : exam year
     * @param courseID : record course
     * @param donatingUser : user id who give the record
     */
    public int createRecord(String name, int year, int courseID, int donatingUser) {
        Connection connection = getConnection();
        int result = 0;
        if(connection != null){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into Records(recordYear, idCourse, recordName, idUser) values (?,?,?,?)");
                preparedStatement.setInt(1, year);
                preparedStatement.setInt(2, courseID);
                preparedStatement.setString(3, name);
                preparedStatement.setInt(4, donatingUser);
                result = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * This method retrieves all the information related to a record stored in the database.
     * @param recordID the record ID in the database.
     * @return a RecordType instance containing all the record metadata.
     */
    @Override
    public RecordType getRecord(int recordID) {
        Connection connection = getConnection();
        RecordType record = null;
        if(connection != null){
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("select * from Records where idRecord=?");
                preparedStatement.setInt(1, recordID);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                record = new RecordType(resultSet.getInt(1),
                        resultSet.getString("recordName"),
                        resultSet.getInt("idCourse"),
                        resultSet.getInt("recordYear"),
                        resultSet.getInt("idUser"));
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return record;
    }

    /**
     * This method retrieves all the records metadata from the database.
     * @return : the record list
     */
    @Override
    public List<RecordType> searchAllRecords(){
        ArrayList<RecordType> records = new ArrayList();
        Connection connection = getConnection();
        if(connection != null){
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Records");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    records.add(new RecordType(resultSet.getInt("idRecord"),
                            resultSet.getString("recordName"),
                            resultSet.getInt("idCourse"),
                            resultSet.getInt("recordYear"),
                            resultSet.getInt("idUser")));
                }
            }catch (SQLException e){e.printStackTrace();}
            finally {
                closeConnection(connection);
            }
        }
        return records;
    }

    /**
     * This method retrieves all the records that a specific user uploaded.
     * @param id the user id
     * @return a list of records.
     */
    @Override
    public List<RecordType> searchRecordsByUser(int id) {
        ArrayList<RecordType> records = new ArrayList();
        Connection connection = getConnection();
        if(connection != null){
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Records WHERE idUser = ?");
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                records.add(new RecordType(0,"FOR USER",0,2000,0));
                while (resultSet.next()){
                    records.add(new RecordType(resultSet.getInt("idRecord"),
                            resultSet.getString("recordName"),
                            resultSet.getInt("idCourse"),
                            resultSet.getInt("recordYear"),
                            resultSet.getInt("idUser")));
                }
            }catch (SQLException e){e.printStackTrace();}
            finally {
                closeConnection(connection);
            }
        }
        return records;
    }

    /**
     * This method deletes a record from the database.
     * @param recordID the record ID.
     * @return 1 if the deletion was successful, 0 otherwise.
     */
    @Override
    public int deleteRecord(int recordID) {
        int result = 0;
        Connection connection = getConnection();
        if(connection!=null){
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("delete from Records where idRecord=?");
                preparedStatement.setInt(1, recordID);
                result = preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }
}
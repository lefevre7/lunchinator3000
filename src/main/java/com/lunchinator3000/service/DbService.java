package com.lunchinator3000.service;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.lunchinator3000.db.SheetsDb;
import com.lunchinator3000.dto.restaurant.RestaurantWinner;
import com.lunchinator3000.dto.vote.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class DbService {

    private SheetsDb sheetsDb;
    private static final String url = "jdbc:postgresql://localhost/lunchinator3000";
    private static final String user = "person";
    private static final String password = "123";

    @Autowired
    public DbService(SheetsDb sheetsDb) {
        this.sheetsDb = sheetsDb;
    }

    public void recordVote() {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        ArrayList<String> previousRestaurantIds = new ArrayList<>();

//        String sql = "INSERT INTO Lunchinator3000 (date, winnerId, voters) VALUES (\'11/28/18\', 2, [\'bob\', \'jim\'])"; //better
//        String sql = "INSERT INTO Lunchinator3000 (date, winnerId, voters) VALUES (\'11/28/18\', 2, [\'bob\'], [\'jim\'])"; //current
        String sql = "SELECT * FROM LUNCHINATOR3000";
        Object o = sheetsDb.execute(sql);
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) o;

        //code for restaurantSuggestion and restaurantWinner ^ :)
        try {
            arrayLists = (ArrayList<ArrayList<String>>) responseEntity.getBody();
            //ooo, for lunchinator4000, try to cast it as arrays of arrays and keep going till it errs, then take the one that didn't error
        } catch (Exception e) {
            //todo throw/log something.
            e.printStackTrace();
        }
        arrayLists.subList(arrayLists.size()-5, arrayLists.size());
        for (int i = 0; i < arrayLists.size(); i++) {
            previousRestaurantIds.add(arrayLists.get(i).get(1));
        }


    }

    public int insertWinner(RestaurantWinner restaurantWinner, ArrayList<Voter> voters) {

        /*int[] sqlReturn;

        //to start postressql server
        //pg_ctl -D /usr/local/var/postgres start
        Connection conn = null;
        String SQL1 = "psql lunchinator3000;";
        String SQL = "CREATE TABLE Restaraunt(   ID INT PRIMARY KEY     NOT NULL," +
                "   NAME           TEXT    );";
        String SQL2 = "INSERT INTO restaraunt(id,name) "
                + "VALUES(?,?);";
        try {
            conn = DriverManager.getConnection(url, user, password);
//            Statement stmt = conn.createStatement();
//            stmt.execute(SQL1);
            PreparedStatement preparedStatement = conn.prepareStatement(SQL2);
            preparedStatement.setInt(1, 8);
            preparedStatement.setString(2, "Dee's");
            preparedStatement.addBatch();
            preparedStatement.setInt(1, 7);
            preparedStatement.setString(2, "Sub's");
            preparedStatement.addBatch();
            sqlReturn = preparedStatement.executeBatch();

            System.out.println("Connected to the PostgreSQL server successfully.");
            System.out.println(sqlReturn);
            return sqlReturn[0];

        } catch (SQLException e) {
            System.out.println(e.getMessage());
*/
        SimpleDateFormat sdfIn = new SimpleDateFormat("MMMM d, yyyy HH:mm");
        SimpleDateFormat sdfOut = new SimpleDateFormat("MM/dd/yy");
        String input = restaurantWinner.getDatetime();
        Date date = null;
        try {
            date = sdfIn.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = sdfOut.format(date);

        String id = String.valueOf(restaurantWinner.getId());
            ArrayList<String> voterNames = new ArrayList<>();

            for (int i = 0; i < voters.size(); i++) {
                voterNames.add(voters.get(i).getName());
            }

//        String sql = "INSERT INTO Lunchinator3000 (date, winnerId, voters) VALUES (11/28/18, 2, [bob, jim])"; //better
            String sql = "INSERT INTO Lunchinator3000 (date, winnerId, voters) VALUES (" + dateString + ", " + id + ", " + voterNames + ")"; //current
            Object o = sheetsDb.execute(sql);
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) o;
            return responseEntity.getStatusCodeValue();

//        }
    }

    public ArrayList<ArrayList<String>> getAWeekOfBallots() {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        ArrayList<ArrayList<String>> arraySubLists = new ArrayList<>();
        ArrayList<ArrayList<String>> ballots = new ArrayList<>();
        ArrayList<String> previousRestaurantIds = new ArrayList<>();
        ArrayList<String> voters = new ArrayList<>();

        String sql = "SELECT * FROM LUNCHINATOR3000";


       /* //to start postressql server
        //pg_ctl -D /usr/local/var/postgres start
        Connection conn = null;
        String SQL1 = "psql lunchinator3000;";
        String SQL = "CREATE TABLE Restaraunt(   ID INT PRIMARY KEY     NOT NULL," +
                "   NAME           TEXT    );";
        String SQL2 = "INSERT INTO restaraunt(id,name) "
                + "VALUES(?,?);";
        try {
            conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // iterate through the java resultset
            while (rs.next())
            {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Date dateCreated = rs.getDate("date_created");
                boolean isAdmin = rs.getBoolean("is_admin");
                int numPoints = rs.getInt("num_points");

                // print the results
                System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints);
            }
            stmt.close();

            System.out.println("Connected to the PostgreSQL server successfully.");
            return ballots;

        } catch (SQLException e) {
            System.out.println(e.getMessage());*/
            Object o = sheetsDb.execute(sql);
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) o;

            try {
                arrayLists = (ArrayList<ArrayList<String>>) responseEntity.getBody();
                //ooo, for lunchinator4000, try to cast it as arrays of arrays and keep going till it errs, then take the one that didn't error
            } catch (Exception ex) {
                //todo throw/log something.
                ex.printStackTrace();
            }
            for (int i = 0; i < arrayLists.size(); i++) {
                if (!arrayLists.get(i).get(0).equals("") && arraySubLists.size() < 5){
                    arraySubLists.add(arrayLists.get(i));
                }
                else if (arraySubLists.size() >= 5){
                    break;
                }
            }

            for (int i = 0; i < arraySubLists.size(); i++) {
                previousRestaurantIds.add(arraySubLists.get(i).get(1));
                voters.add(arraySubLists.get(i).get(2));
            }

            ballots.add(previousRestaurantIds);
            ballots.add(voters);

            return ballots;
//        }
    }
}

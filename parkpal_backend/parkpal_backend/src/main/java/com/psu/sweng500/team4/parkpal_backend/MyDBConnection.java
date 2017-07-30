package com.psu.sweng500.team4.parkpal_backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDBConnection {

    private String mDBURL = "jdbc:sqlserver://parkpal.database.windows.net:1433";
    private String mDBName = "ParkPalSQLDB";
    private String mDBUserName = "parkpalsa";
    private String mDBPass = "H01535026!";
    private String mConnectionUrl = mDBURL + ";"
            + "database=" + mDBName + ";"
            + "user=" + mDBUserName + ";"
            + "password=" + mDBPass;
    private Connection connection;

    public MyDBConnection (){
        try{
            connection = DriverManager.getConnection(mConnectionUrl);
            System.out.print("Connected.<br/>");
        }catch(Exception e){
            System.out.print("Error message: "+ e.getMessage());
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public HashMap<Integer, String> getAllParks(){
        HashMap<Integer, String> result = new HashMap<Integer, String>();

        try{
            String SQL = "SELECT loc_id, name FROM LOCATIONS";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                //System.out.println(rs.getString(1) + ": " + rs.getString(2));
                result.put(rs.getInt(1), rs.getString(2));
            }
        }catch(Exception e){
            System.out.print("Error message: "+ e.getMessage());
        }
        return result;

    }

    public HashMap<Integer, List<String>> getAllParkTags(){
        HashMap<Integer, List<String>> result = new HashMap<>();

        try{
            String SQL = "SELECT loc_id, amenities, loc_type from LOCATIONS";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                int loc_id = rs.getInt(1);
                String amenities = rs.getString(2);
                String loc_type = rs.getString(3);
                ArrayList<String> tags = new ArrayList<String>();

                //System.out.print(loc_id + ": " + amenities + ": " + loc_type + "\n");

                // Add amenities to list
                for (String tag : amenities.split(" ")){
                    if (tag != null && !tag.isEmpty()) {
                        tags.add(tag);
                    }
                }

                // Add loc type to list
                tags.add(loc_type);

                result.put(loc_id, tags);
            }
        }catch(Exception e){
            System.out.print("Error message: "+ e.getMessage());
        }
        return result;

    }

    public HashMap<Integer, List<String>> getAllParkRatings(){
        HashMap<Integer, List<String>> result = new HashMap<>();

        try{
            Connection connection = DriverManager.getConnection(mConnectionUrl);
            System.out.print("Connected.<br/>");
            String SQL = "SELECT loc_id, amenities, loc_type from LOCATIONS";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                int loc_id = rs.getInt(1);
                String amenities = rs.getString(2);
                String loc_type = rs.getString(3);
                ArrayList<String> tags = new ArrayList<String>();

                //System.out.print(loc_id + ": " + amenities + ": " + loc_type + "\n");

                // Add amenities to list
                for (String tag : amenities.split(" ")){
                    tags.add(tag);
                }

                // Add loc type to list
                tags.add(loc_type);

                result.put(loc_id, tags);
            }
        }catch(Exception e){
            System.out.print("Error message: "+ e.getMessage());
        }
        return result;

    }

    public HashMap<Integer, String> getAllUsers(){
        HashMap<Integer, String> result = new HashMap<Integer, String>();

        try{
            String SQL = "SELECT id, username FROM USERS";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                System.out.print(rs.getString(1) + ": " + rs.getString(2) + "\n");
                result.put(rs.getInt(1), rs.getString(2));
            }
        }catch(Exception e){
            System.out.print("Error message: "+ e.getMessage());
        }
        return result;

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.*;

/**
 *
 * @author hlsmi
 */
public class DbConnection {
    
    private static Connection connDB;
    
    //Opens DB connection
    public static void init(){
        System.out.println("Connecting to the database");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connDB = DriverManager.getConnection("jdbc:mysql://3.227.166.251/U0753l", "U0753l", "53688946913");
        }catch (ClassNotFoundException ce){
            System.out.println("Cannot find the right class.  Did you remember to add the mysql library to your Run Configuration?");
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     //Returns Connection
    public static Connection getConn(){
        return connDB;
    }
    
    //Closes connections
    public static void closeConn(){
        try{
            connDB.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Connection closed.");
        }
    }
}

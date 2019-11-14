/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import static cinema.Cinema.url;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javafx.util.Pair;

class User {
    
    static String url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/vzwjksup";
    static String username = "vzwjksup";
    static String password = "OfSGhD9m8yhKrrOmg5vFJ7jbuXQafQ2o";

    String Name;
    String Email;
    String Phone;
    String PwordHash;
    int UID;
    ArrayList<Group> Groups;
    int Reward_Available;
    
    public String GetName() {
        return this.Name;
    };
    
    public String GetEmail() {
        return this.Email;
    };
    
    public String GetPhone() {
        return this.Phone;
    }
    
    
    
    
    public void AddUser(int privilege) {
        
        Connection db;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            db = DriverManager.getConnection(url, username, password);
            st = db.prepareStatement("INSERT INTO users (name, email, password, "
                    + "phone, privilege, shifts, rewards) VALUES (?, ?, ?, ?, ?, 0, 0)");
            st.setString(1, this.GetName());
            st.setString(2, this.GetEmail());
            st.setString(3, "0000");
            st.setInt(4, Integer.parseInt(this.GetPhone()));
            st.setInt(5, privilege);
            
            rs = st.executeQuery();

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    
        
    
    };
}

class Group {

    String Name;
    Pair<User, Date> Members;
    User OnCallSuper;
}

class Show {

    String name;
    Date SDate;
    Date EDate;
    ArrayList<Shift> Shifts;
    int Room;
}

class Shift {

    Group Group;
    Date SDate;
    Date EDate;
    User person;
    String name;
    int status;
}

/* SQL templates
INSERT INTO users (name, email, password, phone, privilege, shifts, rewards) VALUES ()

INSERT INTO groups (name) VALUES ('')

INSERT INTO groupmembers (groupname, uid, joined) VALUES ()

INSERT INTO shows (name, startdate, enddate, room) VALUES ()

INSERT INTO shifts (name, startdate, enddate, groupname, sid, status) VALUES ()
 */
/**
 *
 * @author Piratica
 */
public class Cinema {

    static String url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/vzwjksup";
    static String username = "vzwjksup";
    static String password = "OfSGhD9m8yhKrrOmg5vFJ7jbuXQafQ2o";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here

        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        Scanner scanner = new Scanner(System.in);
        
        
        System.out.println("You have the following options: ");
        System.out.println("1. Do you want to list users of the system?");
        System.out.println("2. Do you want to create a user?");
        System.out.println("3. Do you want to list the current groups?");
        System.out.println("4. Do you want to create a group? (Only Supers)");
        System.out.println("5. Do you want to list the members of groups?");
        
        String option = scanner.nextLine();
        
        if (option.equalsIgnoreCase("1")) {
            
            System.out.println("Current users of the system: ");
            
            Connection db;
            Statement st = null;
            ResultSet rs = null;
            try {
                db = DriverManager.getConnection(url, username, password);
                st = db.createStatement();
                rs = st.executeQuery("SELECT * FROM users");

            } catch (java.sql.SQLException e) {
                System.out.println(e.getMessage());
            }
            
            int personnr = 1;

            while (rs.next()) {
                System.out.println("Information about user "+ personnr + ": ");
                System.out.print("Name: ");
                System.out.println(rs.getString(2));
                System.out.print("Email: ");
                System.out.println(rs.getString(3));
                System.out.print("Phone Number: ");
                System.out.println(rs.getString(5));
                System.out.print("This user has taken " + rs.getString(7) +
                        " shifts and currently has " + rs.getString(8) + 
                        " rewards available.");
                
                
                
                
                personnr++;
                System.out.println();
                
                
            }

            rs.close();
            
            
            
        } else if (option.equalsIgnoreCase("2")) {
            
            User user = new User();
            
            System.out.println("What is the user's name?: ");
            
            user.Name = scanner.nextLine();
            
            System.out.println("What is the user's email?");
            
            user.Email = scanner.nextLine();
            
            System.out.println("What is the user's phone number?");
            
            user.Phone = scanner.nextLine();
            
            System.out.println("Should " + user.GetName() + " be a Super (1) or "
                    + "a normal user (0)? Enter a number: ");
            
            int privilege = scanner.nextInt();
            
            if (privilege > 1 || privilege < 0) {
                System.out.println("Error, number outside of scope 0-1");
                return;
            }
            
            user.AddUser(privilege);
            
            System.out.println("User created!");
                       
        } else if (option.equalsIgnoreCase("3")) {
            
        } else if (option.equalsIgnoreCase("4")) {
            
        } else if (option.equalsIgnoreCase("5")) {
            
        } else {
            
        }
        

        Connection db;
        Statement st = null;
        ResultSet rs = null;
        try {
            db = DriverManager.getConnection(url, username, password);
            st = db.createStatement();
            rs = st.executeQuery("SELECT * FROM users");

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }

        while (rs.next()) {
            System.out.print("Column 1 returned ");
            System.out.println(rs.getString(2));
            System.out.print("Column 2 returned ");
            System.out.println(rs.getString(3));
        }

        rs.close();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.BiConsumer;
import javafx.util.Pair;

class User {

    static String url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/vzwjksup";
    static String username = "vzwjksup";
    static String password = "OfSGhD9m8yhKrrOmg5vFJ7jbuXQafQ2o";

    String name;
    String email;
    int phone;
    String pwordHash;
    int uId;
    ArrayList<Group> groups;
    int reward_Available;
    int shifts;
    int privilege;

    public String GetName() {
        return this.name;
    }

    public String GetEmail() {
        return this.email;
    }

    public int GetPhone() {
        return this.phone;
    }

    public void AddUser(String pword) {

        Connection db;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            db = DriverManager.getConnection(url, username, password);
            st = db.prepareStatement("INSERT INTO users (name, email, password, "
                    + "phone, privilege, shifts, rewards) VALUES (?, ?, ?, ?, ?, 0, 0)");
            st.setString(1, this.GetName());
            st.setString(2, this.GetEmail());
            st.setString(3, pword);
            st.setInt(4, this.GetPhone());
            st.setInt(5, this.privilege);

            rs = st.executeQuery();

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}

class Group {

    static String url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/vzwjksup";
    static String username = "vzwjksup";
    static String password = "OfSGhD9m8yhKrrOmg5vFJ7jbuXQafQ2o";

    String name;
    Pair<User, Date> Members;
    User onCallSuper;

    private String GetName() {
        return this.name;
    }

    private User GetSuper() {
        return this.onCallSuper;
    }

    public void AddGroup() {

        Connection db;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            db = DriverManager.getConnection(url, username, password);
            st = db.prepareStatement("INSERT INTO groups (name, super) "
                    + "VALUES (?, ?)");
            st.setString(1, this.GetName());
            st.setInt(2, this.GetSuper().uId);

            rs = st.executeQuery();

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }

    }
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

INSERT INTO groups (name, super) VALUES (?, ?)

INSERT INTO groupmembers (groupname, uid, joined) VALUES (?, ?, ?)

INSERT INTO shows (name, startdate, enddate, room) VALUES (?, ?, ?, ?)

INSERT INTO shifts (name, startdate, enddate, groupname, sid, status) VALUES (?, ?, ?, ?, ?, ?)
 */
/**
 *
 * @author Piratica
 */
public class Cinema {

    static String url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/vzwjksup";
    static String username = "vzwjksup";
    static String password = "OfSGhD9m8yhKrrOmg5vFJ7jbuXQafQ2o";

    static User currentUser = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here

        Greeter();

        ListOptions();

    }

    private static void Greeter() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please login to your user account: ");
        System.out.println("username: ");

        String user_name = scanner.nextLine();

        System.out.println("password: ");

        String pword = scanner.nextLine();

        currentUser = LoginUser(user_name, pword);

        System.out.println("Current user is: " + currentUser.name);
    }

    private static void ListOptions() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("You have the following options: ");
        System.out.println("1. Do you want to list users of the system?");
        System.out.println("2. Do you want to create a user?");
        System.out.println("3. Do you want to list the current groups?");
        System.out.println("4. Do you want to create a group? (Only Supers)");
        System.out.println("5. Do you want to list the members of groups?");

        String option = scanner.nextLine();

        if (option.equalsIgnoreCase("1")) {

            ListUser();

        } else if (option.equalsIgnoreCase("2")) {

            NewUser(scanner);

        } else if (option.equalsIgnoreCase("3")) {

            ListGroups();

        } else if (option.equalsIgnoreCase("4")) {

            if (currentUser.privilege == 1) {

                CreateGroup(scanner);

            } else {
                System.out.println("You do not have permission to create "
                        + "groups!");
            }

        } else if (option.equalsIgnoreCase("5")) {

        } else {

        }

    }

    private static User LoginUser(String email, String password) {
        // hash password
        LinkedList vars = new LinkedList();
        vars.add(email);
        vars.add(password);
        String query = "SELECT uid, name, email, phone, privilege, shifts, rewards "
                + "FROM users WHERE email = ? AND password = ?";

        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    l.add(rs.getInt(1));
                    l.add(rs.getString(2));
                    l.add(rs.getString(3));
                    l.add(rs.getInt(4));
                    l.add(rs.getInt(5));
                    l.add(rs.getInt(6));
                    l.add(rs.getInt(7));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };
        LinkedList result = Query(query, vars, f);

        if (result.isEmpty()) {
            return null;
        }

        User user = new User();
        user.uId = (int) result.pop();
        user.name = (String) result.pop();
        user.email = (String) result.pop();
        user.phone = (int) result.pop();
        user.privilege = (int) result.pop();
        user.shifts = (int) result.pop();
        user.reward_Available = (int) result.pop();

        return user;
    }

    public static LinkedList Query(String query, LinkedList queryVariables, BiConsumer f) {
        Connection db;
        LinkedList l = new LinkedList();
        try {
            db = DriverManager.getConnection(url, username, password);
            PreparedStatement pquery = db.prepareStatement(query);

            int i = 1;
            while (!queryVariables.isEmpty()) {
                Object temp = queryVariables.pop();
                if (temp instanceof String) {
                    pquery.setString(i, (String) temp);
                } else if (temp instanceof Integer) {
                    pquery.setInt(i, (Integer) temp);
                } else if (temp instanceof java.sql.Date) {
                    pquery.setDate(i, (java.sql.Date) temp);
                } else {
                }
                i++;
            }
            ResultSet rs = pquery.executeQuery();
            f.accept(l, rs);

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return l;
    }

    private static User GetUser(int userId) {
        LinkedList vars = new LinkedList();
        vars.add(userId);
        String query = "SELECT uid, name, email, phone, privilege, shifts, rewards "
                + "FROM users WHERE uid = ?";

        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    l.add(rs.getInt(1));
                    l.add(rs.getString(2));
                    l.add(rs.getString(3));
                    l.add(rs.getInt(4));
                    l.add(rs.getInt(5));
                    l.add(rs.getInt(6));
                    l.add(rs.getInt(7));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };
        LinkedList result = Query(query, vars, f);

        if (result.isEmpty()) {
            return null;
        }

        User user = new User();
        user.uId = (int) result.pop();
        user.name = (String) result.pop();
        user.email = (String) result.pop();
        user.phone = (int) result.pop();
        user.privilege = (int) result.pop();
        user.shifts = (int) result.pop();
        user.reward_Available = (int) result.pop();

        return user;
    }
    
    

    private static ArrayList ListUser() throws SQLException {

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

        ArrayList<User> userlist = new ArrayList<>();

        while (rs.next()) {
            User new_user = new User();

            new_user.name = rs.getString(2);

            new_user.email = rs.getString(3);

            new_user.phone = rs.getInt(5);

            new_user.privilege = rs.getInt(6);

            new_user.shifts = rs.getInt(7);

            new_user.reward_Available = rs.getInt(8);

            userlist.add(new_user);
        }

        rs.close();

        return userlist;
    }

    private static void NewUser(String name, String email, int phone,
            int privilege, String password) {

        if (privilege > 1 || privilege < 0) {
            System.out.println("Error, number outside of scope 0-1");
            return;
        }

        User user = new User();
        user.email = email;
        user.name = name;
        user.phone = phone;
        user.privilege = privilege;

        user.AddUser(password);

    }

    //TODO
    private static ArrayList<Group> ListGroups() {

        LinkedList vars = new LinkedList();
        String query = ("SELECT * FROM groups");

        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    LinkedList temp = new LinkedList();
                    temp.add(rs.getString(1));
                    temp.add(rs.getInt(2));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };

        LinkedList<LinkedList> result = Query(query, vars, f);
        Group g;
        ArrayList<Group> groups = new ArrayList();
        while (!result.isEmpty()) {
            LinkedList temp = result.pop();
            g = new Group();
            g.name = (String) temp.pop();
            g.onCallSuper = GetUser((int) temp.pop());
            groups.add(g);
        }
        return groups;
    }

    private static void CreateGroup(String groupname, User currentSuper) {

        Group group = new Group();

        group.name = groupname;

        group.onCallSuper = currentSuper;

        group.AddGroup();

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.BiConsumer;
import javafx.util.Pair;

class User {

    int uId;
    String name;
    String email;
    String pwordHash;
    int phone;
    int privilege;
    int shifts;
    int reward_Available;
    ArrayList<Group> groups;

    public User() {
    }

    public User(int uId, String name, String email, String pwordHash, int phone, int privilege, int shifts, int reward_Available) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.pwordHash = pwordHash;
        this.phone = phone;
        this.privilege = privilege;
        this.shifts = shifts;
        this.reward_Available = reward_Available;
    }    

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
        LinkedList vars = new LinkedList();
        vars.add(this.GetName());
        vars.add(this.GetEmail());
        vars.add(pword);
        vars.add(this.GetPhone());
        vars.add(this.privilege);
        String query = "INSERT INTO users (name, email, password, "
                    + "phone, privilege, shifts, rewards) VALUES (?, ?, ?, ?, ?, 0, 0)";
        
        Cinema.Query(query, vars);
    }
}

class Group {

    String name;
    ArrayList<Pair<User, Timestamp>> Members;
    User onCallSuper;
    
    public Group() {
        
    }
    
    public Group(String name, User onCallSuper) {
        this.name = name;
        this.onCallSuper = onCallSuper;
    }
    
    private String GetName() {
        return this.name;
    }

    private User GetSuper() {
        return this.onCallSuper;
    }

    public void AddGroup() {
        LinkedList vars = new LinkedList();
        vars.add(this.GetName());
        vars.add(this.GetSuper().uId);
        String query = "INSERT INTO groups (name, super) "
                    + "VALUES (?, ?)";
      
        Cinema.Query(query, vars);
        
        this.AddUserToGroup(this.GetSuper());
    }
    
    public void AddUserToGroup(User user) {
        LinkedList vars = new LinkedList();
        vars.add(this.name);
        vars.add(user.uId);
        java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        System.out.println(date);
        vars.add(date);
        String query = "INSERT INTO groupmembers (groupname, uid, joined) VALUES (?, ?, ?)";
       
        Cinema.Query(query, vars);
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
INSERT INTO users (name, email, password, phone, privilege, shifts, rewards) VALUES (?, ?, ?, ?, ?, 0, 0)

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
        System.out.println("6. Do you want to add a user to a group?");
        
        String option = scanner.nextLine();

        if (option.equalsIgnoreCase("1")) {

            LinkedList<User> users = ListUser();
            users.forEach(u -> System.out.println(u.GetName() + " : " + u.GetEmail()));

        } else if (option.equalsIgnoreCase("2")) {
            User user = new User();
            user.privilege = 0;

            System.out.println("Name?");
            user.name = scanner.nextLine();
            
            System.out.println("Email?");
            user.email = scanner.nextLine();
            
            System.out.println("phone?");
            user.phone = Integer.parseInt(scanner.nextLine());
            
            System.out.println("password?");
            user.AddUser(scanner.nextLine());

        } else if (option.equalsIgnoreCase("3")) {

            ArrayList<Group> groups = ListGroups();
            groups.forEach(g -> System.out.println(g.name));

        } else if (option.equalsIgnoreCase("4")) {

            if (currentUser.privilege == 1) {

                System.out.println("Groupname?");
                String name = scanner.nextLine();
                
                System.out.println("Email of on call Super?");
                User user = GetUser(scanner.nextLine());
                
                Group group = new Group(name, user);
                group.AddGroup();

            } else {
                System.out.println("You do not have permission to create "
                        + "groups!");
            }

        } else if (option.equalsIgnoreCase("5")) {
            System.out.println("List members of which group?");
            option = scanner.nextLine();
            LinkedList<Pair<User, Timestamp>> members = ListMembersOfGroup(option);
            members.forEach(u -> System.out.println(u.getKey().GetName() + " : " + u.getKey().GetEmail()));
            
        } else if (option.equalsIgnoreCase("6")) {
            System.out.println("Email of user:");
            String email = scanner.nextLine();
            User user = GetUser(email);
            
            System.out.println("Name of group:");
            String groupname = scanner.nextLine();
            Group group = GetGroup(groupname);
            
            group.AddUserToGroup(user);
            
        } else {
            
        }

    }

    // Use this for all calls to the database.
    // f is a function to handle the resultset and put results into the linkedlist
    // that will be returned.
    public static LinkedList Query(String query, LinkedList queryVariables, BiConsumer<LinkedList, ResultSet> f) {
        Connection db = null;
        PreparedStatement pquery = null;
        ResultSet rs = null;
        
        LinkedList l = new LinkedList();
        try {
            db = DriverManager.getConnection(url, username, password);
            pquery = db.prepareStatement(query);

            int i = 1;
            while (!queryVariables.isEmpty()) {
                Object temp = queryVariables.pop();
                if (temp instanceof String) {
                    pquery.setString(i, (String) temp);
                } else if (temp instanceof Integer) {
                    pquery.setInt(i, (Integer) temp);
                } else if (temp instanceof java.sql.Timestamp) {
                    pquery.setTimestamp(i, (java.sql.Timestamp) temp);
                } else {
                }
                i++;
            }
            rs = pquery.executeQuery();
            f.accept(l, rs);

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pquery != null) pquery.close();
                if (db != null) db.close();
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return l;
    }
    
    // Use if the result doesn't need to be returned.
    public static void Query(String query, LinkedList queryVariables) {
        Query(query, queryVariables, (ls, rs) -> {});
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
    
    public static User GetUser(int userId) {
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
    
    public static User GetUser(String email) {
        LinkedList vars = new LinkedList();
        vars.add(email);
        String query = "SELECT uid, name, email, phone, privilege, shifts, rewards "
                + "FROM users WHERE email = ?";

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
    
    public static LinkedList<User> ListUser() throws SQLException {
        LinkedList vars = new LinkedList();
        String query = "SELECT * FROM users";
        
        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    User new_user = new User();
                    
                    new_user.uId = rs.getInt(1);
                    
                    new_user.name = rs.getString(2);
                    
                    new_user.email = rs.getString(3);
                    
                    new_user.phone = rs.getInt(5);
                    
                    new_user.privilege = rs.getInt(6);
                    
                    new_user.shifts = rs.getInt(7);
                    
                    new_user.reward_Available = rs.getInt(8);
                    
                    l.add(new_user);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };
        LinkedList userlist = Query(query, vars, f);
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

    
    public static ArrayList<Group> ListGroups() {

        LinkedList vars = new LinkedList();
        String query = ("SELECT * FROM groups");

        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    LinkedList temp = new LinkedList();
                    temp.add(rs.getString(1));
                    temp.add(rs.getInt(2));
                    l.add(temp);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };

        LinkedList<LinkedList> result = Query(query, vars, f);
        ArrayList<Group> groups = new ArrayList();
        while (!result.isEmpty()) {
            LinkedList temp = result.pop();
            String name = (String) temp.pop();
            User onCallSuper = GetUser((int) temp.pop());
            groups.add(new Group(name, onCallSuper));
        }
        return groups;
    }
    
    public static Group GetGroup(String name) {
        LinkedList vars = new LinkedList();
        vars.add(name);
        String query = ("SELECT * FROM groups WHERE name = ?");

        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    l.add(rs.getString(1));
                    l.add(rs.getInt(2));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };
        
        LinkedList result = Query(query, vars, f);
        String groupname = (String) result.pop();
        int superId = (int) result.pop();
        User onCallSuper = GetUser(superId);
        
        Group group = new Group(groupname, onCallSuper);
        
        LinkedList<Pair<User, Timestamp>> members = ListMembersOfGroup(group.name);
        
        group.Members.addAll(members);
        
        return new Group(groupname, onCallSuper);
    }
    
    public static LinkedList<Pair<User, Timestamp>> ListMembersOfGroup(String groupName) {
        LinkedList vars = new LinkedList();
        vars.add(groupName);
        String query = ("SELECT users.uid, users.name, users.email, users.phone,"
                + " users.privilege, users.shifts, users.rewards, groupmembers.joined FROM users"
                + " INNER JOIN groupmembers ON users.uid = groupmembers.uid"
                + " WHERE groupmembers.groupname = ?");

        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    User new_user = new User();
                    
                    new_user.uId = rs.getInt(1);
                    
                    new_user.name = rs.getString(2);
                    
                    new_user.email = rs.getString(3);
                    
                    new_user.phone = rs.getInt(4);
                    
                    new_user.privilege = rs.getInt(5);
                    
                    new_user.shifts = rs.getInt(6);
                    
                    new_user.reward_Available = rs.getInt(7);
                    
                    l.add(new Pair(new_user, rs.getTimestamp(8)));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };

        LinkedList userlist = Query(query, vars, f);
        
        return userlist;
    }
    
    private static void CreateGroup(String groupname, User currentSuper) {

        Group group = new Group();

        group.name = groupname;

        group.onCallSuper = currentSuper;

        group.AddGroup();

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import javafx.util.Pair;

/* SQL templates
    INSERT INTO users (name, email, password, phone, privilege, shifts, rewards) VALUES (?, ?, ?, ?, ?, 0, 0)
    
    INSERT INTO groups (name, super) VALUES (?, ?)
    
    INSERT INTO groupmembers (groupname, uid, joined) VALUES (?, ?, ?)
    
    INSERT INTO shows (name, startdate, enddate, room) VALUES (?, ?, ?, ?)
    
    INSERT INTO shifts (name, startdate, enddate, groupname, sid, status) VALUES (?, ?, ?, ?, ?, ?)
 */
/**
 *
 * @author Lucas
 */
public class Database {
    private static String url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/vzwjksup";
    private static String username = "vzwjksup";
    private static String password = "OfSGhD9m8yhKrrOmg5vFJ7jbuXQafQ2o";
    
    /* 
        Use this for all calls to the database.
        f is a function to handle the resultset and put results into the linkedlist
        that will be returned.
    */
    public static LinkedList Query(String query, LinkedList queryVariables, BiConsumer<LinkedList, ResultSet> f) {
        Connection db = null;
        PreparedStatement pquery = null;
        ResultSet rs = null;
        
        LinkedList l = new LinkedList();
        try {
            db = DriverManager.getConnection(url, username, password);
            pquery = db.prepareStatement(query);

            // Checking the types of each variable and inserting them
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
            //Making sure connections are closed properly
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
    
    // Gets a user by email and password
    public static User LoginUser(String email, String password) {
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
    
    // Gets a user by unique id
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
    
    // Gets a user by unique email
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
    
    // Gets a list of all users
    public static LinkedList<User> ListUser() {
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
    
    // Gets a list of all groups
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
    
    // Gets a group by unique name and all members of the group
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
        
        return group;
    }
    
    // Gets a list of all members of a group. The list contains Pairs of Users
    // and timestamp of the date they joined the group
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
}

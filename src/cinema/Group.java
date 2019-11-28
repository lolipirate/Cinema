/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import javafx.util.Pair;

/**
 *
 * @author Lucas
 */
public class Group {

    String name;
    ArrayList<Pair<User, Timestamp>> Members;
    User onCallSuper;
    
    public Group() {
        this.Members = new ArrayList(); 
    }
    
    public Group(String name, User onCallSuper) {
        this.name = name;
        this.onCallSuper = onCallSuper;
        this.Members = new ArrayList();
    }
    
    private String GetName() {
        return this.name;
    }

    private User GetSuper() {
        return this.onCallSuper;
    }

    // Adds a group to the database. The onCallSuper is automatically added as a
    // member of the group
    public void AddGroup() {
        LinkedList vars = new LinkedList();
        vars.add(this.GetName());
        vars.add(this.GetSuper().uId);
        String query = "INSERT INTO groups (name, super) "
                    + "VALUES (?, ?)";
      
        Database.Query(query, vars);
        
        this.AddUserToGroup(this.GetSuper());
    }
    
    // Adds a User to this group in the database
    public void AddUserToGroup(User user) {
        LinkedList vars = new LinkedList();
        vars.add(this.name);
        vars.add(user.uId);
        java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        vars.add(date);
        String query = "INSERT INTO groupmembers (groupname, uid, joined) VALUES (?, ?, ?)";
       
        Database.Query(query, vars);
    }
}

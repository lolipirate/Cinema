/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Lucas
 */
public class User {
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

    public int GetUId() {
        return this.uId;
    }
    
    // Adds this user to the database
    public void AddUser(String pword) {
        LinkedList vars = new LinkedList();
        vars.add(this.GetName());
        vars.add(this.GetEmail());
        vars.add(pword);
        vars.add(this.GetPhone());
        vars.add(this.privilege);
        String query = "INSERT INTO users (name, email, password, "
                    + "phone, privilege, shifts, rewards) VALUES (?, ?, ?, ?, ?, 0, 0)";
        
        Database.Query(query, vars);
    }
}

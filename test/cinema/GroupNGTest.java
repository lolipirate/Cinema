/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import static cinema.Cinema.Query;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Piratica
 */
public class GroupNGTest {
    
    public GroupNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
        Group group = new Group();
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        
       Group group = null;
    }

    /**
     * Test of AddGroup method, of class Group.
     * The method will also test the AddUserToGroup method. 
     */
    @Test
    public void testAddGroup() {
        System.out.println("AddGroup");
        Group instance = new Group();
        Group expected = new Group();
        User user = new User();
        User expected_user = new User();
        user.email = "unit@test.ok";
        instance.name = "unit test";
        instance.onCallSuper = user;
        
        
        user.AddUser("test");
        
        user.uId = Cinema.GetUser(user.email).uId;
        
        instance.AddGroup();
        
        
        expected = Cinema.GetGroup(instance.name);
        
        Assert.assertEquals(instance.name, expected.name);
        
        Assert.assertEquals(user.email, expected.onCallSuper.email);
        
        
        RemoveGroupmemberFromDB(instance.name);
        UserNGTest.RemoveFromDB(user.email);
        RemoveGroupFromDB(instance.name);
    }
    
    
    
    private void RemoveGroupFromDB(String group_name) {
        
        LinkedList vars = new LinkedList();
        vars.add(group_name);
        String query = "DELETE FROM groups WHERE name = ?";
        
        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {};
        Query(query, vars, f);
    }
    
    private void RemoveGroupmemberFromDB(String name) {
        LinkedList vars = new LinkedList();
        vars.add(name);
        String query = "DELETE FROM groupmembers WHERE groupname = ?";
        
        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {};
        Query(query, vars, f);
    }
    
    
    
}

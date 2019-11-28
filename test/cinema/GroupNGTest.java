/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import static cinema.Cinema.Query;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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
     * It will create a group and a super to add to said group. It will then 
     * update the database with these entries and finally attempt to pull them
     * for the database to compare with the expected values. 
     */
    @Test
    public void testAddGroup() {
        System.out.println("AddGroup");
        Group instance = new Group();
        Group expected = new Group();
        User user = new User();
        User expected_user = new User();
        user.email = "unit@test.ok";
        user.name = "unit test";
        user.phone = 1234;
        user.privilege = 1;
        instance.name = "unit test";
        instance.onCallSuper = user;
        
        
        user.AddUser("test");
        
        
        
        user.uId = Cinema.GetUser(user.email).uId;
        
        instance.AddGroup();
        
        expected = Cinema.GetGroup(instance.name);
        
        Assert.assertEquals(instance.name, expected.name);
        
        Assert.assertEquals(user.email, expected.onCallSuper.email);
        
        RemoveGroupmemberFromDB(instance.name);
        RemoveGroupFromDB(instance.name);
        UserNGTest.RemoveFromDB(user.email);
    }
    
    /**
     * Test of AddUserToGroup method, of class Group.
     */
    @Test
    public void testAddUserToGroup() {
        User superuser = new User();
        User toAdd = new User();
        Group instance = new Group();
        Group expected_group = new Group();
        User expected_user = new User();

        superuser.email = "unit@test.ok";
        superuser.name = "Unit Test";
        superuser.phone = 1337;
        superuser.privilege = 1;
        superuser.AddUser("unit");
        
        toAdd.email = "test@unit.test";
        toAdd.name = "mr Unit Test";
        toAdd.phone = 4321;
        toAdd.privilege = 0;
        toAdd.AddUser("test");
        
        instance.name = "Unit Test";
        instance.onCallSuper = superuser;
        instance.AddGroup();
        instance.AddUserToGroup(toAdd);
        
        expected_group = Cinema.GetGroup(instance.name);
        
        expected_user = expected_group.Members.getKey();
        
        Assert.assertEquals(toAdd.name, expected_user.name);
        
        
        
        RemoveGroupmemberFromDB(instance.name);
        RemoveGroupFromDB(instance.name);
        UserNGTest.RemoveFromDB(superuser.email);
        UserNGTest.RemoveFromDB(toAdd.email);
        
    }
    
    
    /*
    * General cleanup methods to make the tests reusable and avoid
    * cluttering up the database. 
    */
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

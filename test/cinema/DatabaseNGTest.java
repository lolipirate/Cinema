/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javafx.util.Pair;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Lucas
 */
public class DatabaseNGTest {

    public DatabaseNGTest() {
    }

    private static User testUser;
    private static Group testGroup;

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @BeforeClass
    public static void setUpClass() throws Exception {
        // Add test user
        testUser = new User();
        testUser.email = "unit@testDatabase.ok";
        testUser.name = "unit test";
        testUser.phone = 1488;
        testUser.privilege = 1;
        testUser.pwordHash = "unit tester";
        testUser.AddUser(testUser.pwordHash);
        testUser.uId = Database.GetUser(testUser.email).uId;

        // Add test group
        testGroup = new Group();
        testGroup.name = "Database test group";
        testGroup.onCallSuper = testUser;
        testGroup.AddGroup();
        testGroup.Members.add(new Pair(testUser, new Timestamp(0)));

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Remove test user and test group
        GroupNGTest.RemoveGroupmembersFromDB(testGroup.name);
        GroupNGTest.RemoveGroupFromDB(testGroup.name);
        UserNGTest.RemoveFromDB(testUser.email);
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of Query method, of class Database.
     */
    @Test
    public void testQuery() {
        System.out.println("Query");
        String query = "SELECT phone FROM users WHERE email = ?";
        LinkedList queryVariables = new LinkedList();
        queryVariables.add(testUser.email);
        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
            try {
                while (rs.next()) {
                    l.add(rs.getInt(1));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        };
        
        LinkedList expResult = new LinkedList();
        expResult.add(testUser.phone);

        LinkedList result = Database.Query(query, queryVariables, f);

        // Does result and expResult have exactly the same elements?
        assertEquals(result, expResult);
    }

    /**
     * Test of LoginUser method, of class Database.
     */
    @Test
    public void testLoginUser() {
        System.out.println("LoginUser");
        String email = testUser.email;
        String password = testUser.pwordHash;
        User expResult = testUser;

        User result = Database.LoginUser(email, password);

        assertEquals(result.email, expResult.email);
        assertEquals(result.name, expResult.name);
        assertEquals(result.phone, expResult.phone);
        assertEquals(result.privilege, expResult.privilege);
        assertEquals(result.uId, expResult.uId);
    }

    /**
     * Test of GetUser method, of class Database.
     */
    @Test
    public void testGetUser_int() {
        System.out.println("GetUser");
        int userId = testUser.uId;
        User expResult = testUser;
        User result = Database.GetUser(userId);

        assertEquals(result.email, expResult.email);
        assertEquals(result.name, expResult.name);
        assertEquals(result.phone, expResult.phone);
        assertEquals(result.privilege, expResult.privilege);
        assertEquals(result.uId, expResult.uId);
    }

    /**
     * Test of GetUser method, of class Database.
     */
    @Test
    public void testGetUser_String() {
        System.out.println("GetUser");
        String email = testUser.email;
        User expResult = testUser;
        User result = Database.GetUser(email);

        assertEquals(result.email, expResult.email);
        assertEquals(result.name, expResult.name);
        assertEquals(result.phone, expResult.phone);
        assertEquals(result.privilege, expResult.privilege);
        assertEquals(result.uId, expResult.uId);
    }

    /**
     * Test of ListUser method, of class Database.
     */
    @Test
    public void testListUser() {
        System.out.println("ListUser");
        User expResult = Database.GetUser(testUser.uId);
        LinkedList<User> result = Database.ListUser();

        // Is testUser in the list of all users?
        assertTrue(result.stream().map(User::GetUId).anyMatch(Predicate.isEqual(expResult.uId)));
    }

    /**
     * Test of ListGroups method, of class Database.
     */
    @Test
    public void testListGroups() {
        System.out.println("ListGroups");
        Group expResult = testGroup;
        ArrayList<Group> result = Database.ListGroups();

        // Is testGroup in the list of all groups?
        assertTrue(result.stream().map(Group::GetName).anyMatch(Predicate.isEqual(expResult.name)));
    }

    /**
     * Test of GetGroup method, of class Database.
     */
    @Test
    public void testGetGroup() {
        System.out.println("GetGroup");
        String name = testGroup.name;
        Group expResult = testGroup;
        Group result = Database.GetGroup(name);

        assertEquals(result.name, expResult.name);
        assertEquals(result.onCallSuper.GetUId(), expResult.onCallSuper.GetUId());
        assertEquals(result.Members.get(0).getKey().GetUId(), expResult.Members.get(0).getKey().GetUId());
    }

    /**
     * Test of ListMembersOfGroup method, of class Database.
     */
    @Test
    public void testListMembersOfGroup() {
        System.out.println("ListMembersOfGroup");
        String groupName = testGroup.name;
        LinkedList<Pair<User, Timestamp>> expResult = new LinkedList();
        expResult.add(testGroup.Members.get(0));

        LinkedList<Pair<User, Timestamp>> result = Database.ListMembersOfGroup(groupName);

        assertEquals(result.get(0).getKey().GetUId(), expResult.get(0).getKey().GetUId());
    }
}

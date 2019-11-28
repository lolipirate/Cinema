/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.BiConsumer;
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
public class CinemaNGTest {
    
    public CinemaNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of main method, of class Cinema.
     */
    @Test
    public void testMain() throws Exception {
        
        System.out.println("main");
        String[] args = null;
        Cinema.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Query method, of class Cinema.
     */
    @Test
    public void testQuery() {
        System.out.println("Query");
        String query = "";
        LinkedList queryVariables = null;
        BiConsumer f = null;
        LinkedList expResult = null;
        LinkedList result = Cinema.Query(query, queryVariables, f);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of GetUser method, of class Cinema.
     */
    @Test
    public void testGetUser_int() {
        System.out.println("GetUser");
        int userId = 0;
        User expResult = null;
        User result = Cinema.GetUser(userId);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of GetUser method, of class Cinema.
     */
    @Test
    public void testGetUser_String() {
        System.out.println("GetUser");
        String email = "";
        User expResult = null;
        User result = Cinema.GetUser(email);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ListUser method, of class Cinema.
     */
    @Test
    public void testListUser() throws Exception {
        System.out.println("ListUser");
        LinkedList expResult = null;
        LinkedList result = Cinema.ListUser();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ListGroups method, of class Cinema.
     */
    @Test
    public void testListGroups() {
        System.out.println("ListGroups");
        ArrayList expResult = null;
        ArrayList result = Cinema.ListGroups();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of GetGroup method, of class Cinema.
     */
    @Test
    public void testGetGroup() {
        System.out.println("GetGroup");
        String name = "";
        Group expResult = null;
        Group result = Cinema.GetGroup(name);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ListMembersOfGroup method, of class Cinema.
     */
    @Test
    public void testListMembersOfGroup() {
        System.out.println("ListMembersOfGroup");
        String groupName = "";
        LinkedList expResult = null;
        LinkedList result = Cinema.ListMembersOfGroup(groupName);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

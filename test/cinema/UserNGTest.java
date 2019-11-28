/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import static cinema.Cinema.Query;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Piratica
 */
public class UserNGTest {

    /**
     * Test of GetName method, of class User.
     */
    @Test
    public void testGetName() {
        System.out.println("GetName");

        User instance = new User();

        instance.name = "Hans Andersen";

        String expResult = "Hans Andersen";
        String result = instance.GetName();
        assertEquals(result, expResult);
    }

    /**
     * Test of GetEmail method, of class User.
     */
    @Test
    public void testGetEmail() {
        System.out.println("GetEmail");
        User instance = new User();
        instance.email = "test@test.test";
        String expResult = "test@test.test";
        String result = instance.GetEmail();
        assertEquals(result, expResult);
    }

    /**
     * Test of GetPhone method, of class User.
     */
    @Test
    public void testGetPhone() {
        System.out.println("GetPhone");
        User instance = new User();
        instance.phone = 22334455;
        int expResult = 22334455;
        int result = instance.GetPhone();
        assertEquals(result, expResult);
    }

    /**
     * Test of AddUser method, of class User.
     */
    @Test
    public void testAddUser() {
        System.out.println("AddUser");
        User instance = new User();
        User expected = new User();
        String pass = "unit tester";

        instance.email = "unit@test.ok";
        instance.phone = 1488;
        instance.name = "unit test";
        instance.privilege = 0;

        instance.AddUser(pass);

        expected = GetUserFromEmail(instance.email);

        assertEquals(instance.email, expected.email);

        assertEquals(instance.name, expected.name);

        assertEquals(instance.phone, expected.phone);

        RemoveFromDB(instance.email);
    }

    private static User GetUserFromEmail(String email) {
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

    /**
     * General clean up function for the database.
     *
     * @param email
     */
    public static void RemoveFromDB(String email) {

        LinkedList vars = new LinkedList();
        vars.add(email);
        String query = "DELETE FROM users WHERE email = ?";

        Connection db;
        BiConsumer<LinkedList, ResultSet> f = (l, rs) -> {
        };
        Query(query, vars, f);
    }

}

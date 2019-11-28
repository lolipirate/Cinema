/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import static cinema.Cinema.Query;
import java.util.LinkedList;
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
     * Test of AddGroup method, of class Group. The method will also test the
     * AddUserToGroup method. It will create a group and a super to add to said
     * group. It will then update the database with these entries and finally
     * attempt to pull them for the database to compare with the expected
     * values.
     */
    @Test
    public void testAddGroup() {
        System.out.println("AddGroup");
        Group instance = new Group();
        Group expected;
        User user = new User();
        User expected_user = new User();
        user.email = "unit@testing.dk";
        user.name = "unit test addmember";
        user.phone = 1234;
        user.privilege = 1;
        instance.name = "testing groups";
        instance.onCallSuper = user;

        user.AddUser("test");

        user.uId = Cinema.GetUser(user.email).uId;

        instance.AddGroup();

        expected = Cinema.GetGroup(instance.name);

        Assert.assertEquals(instance.name, expected.name);

        Assert.assertEquals(user.email, expected.onCallSuper.email);

        RemoveGroupmembersFromDB(instance.name);
        RemoveGroupFromDB(instance.name);
        UserNGTest.RemoveFromDB(user.email);
    }

    /**
     * Test of AddUserToGroup method, of class Group.
     */
    @Test
    public void testAddUserToGroup() {

        System.out.println("AddUserToGroup");
        User superuser = new User();
        User toAdd = new User();
        Group instance = new Group();
        Group expected_group;
        User expected_user;

        superuser.email = "unit2@test.dk";
        superuser.name = "Unit Test";
        superuser.phone = 1337;
        superuser.privilege = 1;
        superuser.AddUser("unit");
        superuser.uId = Cinema.GetUser(superuser.email).uId;

        toAdd.email = "unit3@unit.test";
        toAdd.name = "mr Unit Test";
        toAdd.phone = 4321;
        toAdd.privilege = 0;
        toAdd.AddUser("test");
        toAdd.uId = Cinema.GetUser(toAdd.email).uId;

        instance.name = "test group";
        instance.onCallSuper = superuser;
        instance.AddGroup();
        instance.AddUserToGroup(toAdd);

        expected_group = Cinema.GetGroup(instance.name);

        expected_user = expected_group.Members.get(1).getKey();

        Assert.assertEquals(toAdd.name, expected_user.name);

        RemoveGroupmembersFromDB(instance.name);
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

        Query(query, vars);
    }

    private void RemoveGroupmembersFromDB(String name) {
        LinkedList vars = new LinkedList();
        vars.add(name);
        String query = "DELETE FROM groupmembers WHERE groupname = ?";

        Query(query, vars);
    }

}

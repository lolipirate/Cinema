/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import static cinema.Database.*;

/**
 *
 * @author Piratica
 */
public class Cinema {

    static User currentUser = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here

        //Greeter();
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

    private static void ListOptions() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("You have the following options: ");
            System.out.println("1. Do you want to list users of the system?");
            System.out.println("2. Do you want to create a user?");
            System.out.println("3. Do you want to list the current groups?");
            System.out.println("4. Do you want to create a group? (Only Supers)");
            System.out.println("5. Do you want to list the members of groups?");
            System.out.println("6. Do you want to add a user to a group?");
            System.out.println("Enter anything else to end");

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
                Group group = GetGroup(option);
                group.Members.forEach(u -> System.out.println(u.getKey().GetName() + " : " + u.getKey().GetEmail() + " : Joined " + u.getValue()));

            } else if (option.equalsIgnoreCase("6")) {
                System.out.println("Email of user:");
                String email = scanner.nextLine();
                User user = GetUser(email);

                System.out.println("Name of group:");
                String groupname = scanner.nextLine();
                Group group = GetGroup(groupname);

                group.AddUserToGroup(user);

            } else {
                return;
            }
            scanner.nextLine();
        }

    }

}

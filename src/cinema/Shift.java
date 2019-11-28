/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.Timestamp;

/**
 *
 * @author Lucas
 */
public class Shift {
    int shiftId;
    int showId;
    String name;
    Timestamp sDate;
    Timestamp eDate;
    Group group;
    User person;
    int status;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Lucas
 */
public class Show {
    int id;
    String name;
    Timestamp SDate;
    Timestamp EDate;
    int Room;
    ArrayList<Shift> Shifts;
}

package Primary;

import java.util.LinkedList;

/**
 * InputController
 * Abstracts inputs
 */
public class Intersection {
    //Traffic Loop Sensors
    public LinkedList<Lanes> north_south = new LinkedList<>();
    public LinkedList<Lanes> east_west = new LinkedList<>();
    public LinkedList<Lanes> north = new LinkedList<>();
    public LinkedList<Lanes> south = new LinkedList<>();
    public LinkedList<Lanes> east = new LinkedList<>();
    public LinkedList<Lanes> west = new LinkedList<>();
    public LinkedList<Lights> north_south_ped = new LinkedList<>();
    public LinkedList<Lights> east_west_ped = new LinkedList<>();
    /**
     * Initializes the Input Sensors
     */
    public Intersection(){
        //List of Traffic Lights

        for(Lanes l: Lanes.values())
        {
            if(l.toString().contains("N") || l.toString().contains("S")) north_south.add(l);
            else east_west.add(l);
        }

        for(Lanes l: Lanes.values())
        {
            if(l.toString().contains("N")) north.add(l);
            if(l.toString().contains("S")) south.add(l);
            if(l.toString().contains("E")) east.add(l);
            if(l.toString().contains("W")) west.add(l);
        }

        //List of Pedestrian Lights
        for(Lights l: Lights.values())
        {
            if(l.toString().contains("EAST") || l.toString().contains("WEST")) north_south_ped.add(l);//Opposites match uo to lanes
            else east_west_ped.add(l);
        }
    }

}

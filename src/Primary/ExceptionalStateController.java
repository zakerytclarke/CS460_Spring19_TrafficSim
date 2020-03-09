package Primary;

import java.util.LinkedList;

/**
 * Class: EmergencyStateController
 * EmergencyStateController will control the e
 */
public class ExceptionalStateController {

    static Direction detectEmergency(LinkedList<Lanes> north, LinkedList<Lanes> south, LinkedList<Lanes> east, LinkedList<Lanes> west){
        for(Lanes l: north)
        {
            if(l.getEmergencyOnLane()){
                return Direction.N;
            }
        }
        for(Lanes l: south)
        {
            if(l.getEmergencyOnLane()){
                return Direction.S;
            }
        }
        for(Lanes l: east)
        {
            if(l.getEmergencyOnLane()){
                return Direction.E;
            }
        }
        for(Lanes l: west)
        {
            if(l.getEmergencyOnLane()){
                return Direction.W;
            }
        }
        return null;
    }
}

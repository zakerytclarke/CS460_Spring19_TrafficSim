package Primary;

import java.util.LinkedList;

/**
 * Class: EmergencyStateController
 * EmergencyStateController will control the e
 */
public class ExceptionalStateController {

    /**
     * Method: detectEmergency
     * detectEmergency will detect if there is an oncoming emergency
     * @param north the north lanes
     * @param south the south lanes
     * @param east the east lanes
     * @param west the west lanes
     * @return the direction (if any) of an oncoming emergency
     */
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

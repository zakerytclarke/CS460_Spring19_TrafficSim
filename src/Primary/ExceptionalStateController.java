package Primary;

/**
 * Class: EmergencyStateController
 * EmergencyStateController will control the e
 */
public class ExceptionalStateController {

    private static Intersection intersection=new Intersection();

    /**
     * Method: detectEmergency
     * detects if there is an oncoming emergency vehicle
     * @return the direction of an oncoming emergency (if applicable)
     */
    static Direction detectEmergency(){
        for(Lanes l: intersection.north)
        {
            if(l.getEmergencyOnLane()){
                return Direction.N;
            }
        }
        for(Lanes l: intersection.south)
        {
            if(l.getEmergencyOnLane()){
                return Direction.S;
            }
        }
        for(Lanes l: intersection.east)
        {
            if(l.getEmergencyOnLane()){
                return Direction.E;
            }
        }
        for(Lanes l: intersection.west)
        {
            if(l.getEmergencyOnLane()){
                return Direction.W;
            }
        }
        return null;
    }
}

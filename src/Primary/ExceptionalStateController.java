package Primary;
import java.util.LinkedList;
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
    static void powerMode(LinkedList<Lanes> ns, LinkedList<Lanes> ew, LinkedList<Lights> nsped, LinkedList<Lights> ewped)
    {
        boolean hasPower = true;
        //boolean hasPower = Sensor.pulsePower();
        int stopLightimer = 350;
        long curTime = System.currentTimeMillis();
        OutputController.stopPeds(nsped, ewped, null);

        while(!hasPower) {
            if(ns.get(0).getSignal() == SignalColor.GREEN && System.currentTimeMillis() > curTime + stopLightimer) {
                OutputController.setRed(ns);
                for(int i = 0; i < 3; i++) ew.get(i).setColor(SignalColor.GREEN);
            }
            if(ew.get(0).getSignal() == SignalColor.GREEN && System.currentTimeMillis() > curTime + stopLightimer) {
                OutputController.setRed(ew);
                for(int i = 0; i < 3; i++) ns.get(i).setColor(SignalColor.GREEN);
            }
            curTime = System.currentTimeMillis();
        }

    }

    static void maintenanceMode(LinkedList<Lights> nsped, LinkedList<Lights> ewped, LinkedList<Lanes> lanes) {
        OutputController.stopPeds(nsped, ewped, null);
        OutputController.setRed(lanes);
    }


}

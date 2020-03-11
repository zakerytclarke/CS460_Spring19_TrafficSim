package Primary;
import java.util.LinkedList;

public class VPStateController {

    private static boolean pedWaiting(LinkedList<Lights> peds) {
        return peds.get(0).isPedestrianAt() || peds.get(1).isPedestrianAt();
    }

    private static boolean carWaiting(LinkedList<Lanes> cars) {
        return cars.get(0).isCarOnLane()|| cars.get(3).isCarOnLane();
    }

    /**
     * Method: anyPed
     * checks if any pedestrians are at the intersection
     * @param peds the list of pedestrians
     * @return true if pedestrians at lights
     */
    static boolean anyPed(LinkedList<Lights> peds) {
        for(Lights l: peds)
        {
            if(l.isPedestrianAt()){
               return true;
            }
        }
        return false;
    }

    /**
     * Method: anyCar
     * sees if there are cars in the lanes of the intersection
     * @param cars list of lanes of cars
     * @return if there is a car at the intersection
     */
    static boolean anyCar(LinkedList<Lanes> cars) {
        for(Lanes l: cars)
        {
            if(l.isCarOnLane()){
                return true;
            }
        }
        return false;
    }

    /**
     * Method: occupiedTurn
     * occupiedTurn will see if there exists a car in the turn lanes
     * @param dir current direction of travel allowed
     * @param ns the list of north/south lanes
     * @param ew the list of east/west lanes
     * @return if there exists a car waiting to turn left
     */
    static boolean occupiedTurn(Direction dir, LinkedList<Lanes> ns, LinkedList<Lanes> ew) {
        if(dir == Direction.NS){
            return VPStateController.carWaiting(ns);
        }else{
            return VPStateController.carWaiting(ew);
        }
    }

    /**
     * Method: occupiedPed
     * occupiedPed will see if there is a pedestrian waiting to cross the intersection
     * @param dir current direction of travlel allowed
     * @param ns list of north/south pedestrians
     * @param ew list of east/west pedestrians
     * @return if there is a pedestrian waiting
     */
    static boolean occupiedPed(Direction dir, LinkedList<Lights> ns, LinkedList<Lights> ew) {
        if(dir==Direction.NS){
           return VPStateController.pedWaiting(ns);
        }else{
            return VPStateController.pedWaiting(ew);
        }
    }
}

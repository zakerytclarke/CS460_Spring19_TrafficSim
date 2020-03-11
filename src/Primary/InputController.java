package Primary;
import java.util.LinkedList;

/**
 * Class: InputController
 * InputController controls the input drivers related to the TICS
 */
public class InputController {

    /**
     * Method: pedWaiting
     * sees if there is a pedestrian waiting at any of the lights
     * @param peds pedestrian lights
     * @return if there is a pedestrian waiting
     */
    static boolean pedWaiting(LinkedList<Lights> peds) {
        return peds.get(0).isPedestrianAt() || peds.get(1).isPedestrianAt();
    }

    /**
     * Method: carWaiting
     * carWaiting waits to see if there is a car presently waiting at an intersection
     * @param cars list of lanes
     * @return if there is a car waiting
     */
    static boolean carWaiting(LinkedList<Lanes> cars) {
        return cars.get(0).isCarOnLane()|| cars.get(3).isCarOnLane();
    }

    /**
     * Method: anyPed
     * @param peds sees if there is a waiting pedestrian at the given light
     * @return true if there is a waiting pedestrian
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
     * anyCar sees if there is a car waiting at any of the provided lanes
     * @param cars list of intersection lanes
     * @return if there is a car in the lanes
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
}

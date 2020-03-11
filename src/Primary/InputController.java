package Primary;
import java.util.LinkedList;

public class InputController {

    static boolean pedWaiting(LinkedList<Lights> peds) {
        return peds.get(0).isPedestrianAt() || peds.get(1).isPedestrianAt();
    }

    static boolean carWaiting(LinkedList<Lanes> cars) {
        return cars.get(0).isCarOnLane()|| cars.get(3).isCarOnLane();
    }

    static boolean anyPed(LinkedList<Lights> peds) {
        for(Lights l: peds)
        {
            if(l.isPedestrianAt()){
               return true;
            }
        }
        return false;
    }

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

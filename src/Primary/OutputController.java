package Primary;
import java.util.LinkedList;

/**
 * Class: OutputController
 * outputController will control the methods relating to displaying the output
 * signal lights
 */
class OutputController {
    /**
     * Method: handleEmergencyState
     * handles the emergency state for a vehicle coming from the given lane
     * @param laneList list of lanes of the direction of the emergency vehicle
     */
    static void handleEmergencyState(LinkedList<Lanes> laneList) {
        laneList.get(0).setColor(SignalColor.GREEN);//Left Turn
        laneList.get(1).setColor(SignalColor.GREEN);//Straight
        laneList.get(2).setColor(SignalColor.GREEN);//Straight

    }

    /**
     * Method: stopPeds
     * stopPeds will stop the pedestrians coming in each direction
     * @param nsPed the north and south predestrians
     * @param ewPed the east and west pedestrians
     * @param currentState the current state of the intersecition
     */
    static void stopPeds(LinkedList<Lights> nsPed, LinkedList<Lights> ewPed, IntersectionState currentState) {
        nsPed.get(0).setColor((currentState.ped));//All Ped Stop
        nsPed.get(1).setColor((currentState.ped));//All Ped Stop
        ewPed.get(0).setColor((currentState.ped));//All Ped Stop
        ewPed.get(1).setColor((currentState.ped));//All Ped Stop
    }

    /**
     * Setter: setRed
     * setRed will set all signal colors red
     * @param lanes the list of lanes
     */
    static void setRed(LinkedList<Lanes> lanes){
        for(Lanes l: lanes)
        {
            l.setColor(SignalColor.RED);
        }
    }

    /**
     * Method: outputSignal
     * outputSignal will change the signal output to accompany the next output
     * @param currentState the current light state
     * @param currentDirection current direction of travel
     * @param north_south the north and south lights
     * @param east_west the east and west lights
     * @param north_south_ped the north and south pedestrian lights
     * @param east_west_ped the east and west pedestrian lights
     */
    static void outputSignal(IntersectionState currentState, Direction currentDirection, LinkedList<Lanes> north_south, LinkedList<Lanes> east_west, LinkedList<Lights> north_south_ped, LinkedList<Lights> east_west_ped) {
        if(currentDirection==Direction.NS){
            //North/South has control
            setRed(east_west);//Opposing Side Red
            nextState(north_south, north_south_ped, currentState);

        }else{
            //East/West has control
            setRed(north_south);//Opposing Side Red
            nextState(east_west, east_west_ped, currentState);
        }
    }

    /**
     * Method: nextState
     * nextState will set the next state for the intersection
     * @param trafficList the list of traffic lights in their current state
     * @param pedList list of pedestrian lights in their current state
     * @param currentState the current state of the intersection
     */
    static void nextState(LinkedList<Lanes> trafficList, LinkedList<Lights> pedList, IntersectionState currentState){
        trafficList.get(0).setColor((currentState.turn));//Left Turn
        trafficList.get(1).setColor((currentState.straight));//Straight
        trafficList.get(2).setColor((currentState.straight));//Straight
        trafficList.get(3).setColor((currentState.turn));//Left Turn
        trafficList.get(4).setColor((currentState.straight));//Straight
        trafficList.get(5).setColor((currentState.straight));//Straight
        pedList.get(0).setColor((currentState.ped));//Ped
        pedList.get(1).setColor((currentState.ped));//Ped
    }
}

package Primary;
import java.util.LinkedList;

/**
 * Class: OutputController
 * outputController will control the methods relating to displaying the output
 * signal lights
 */
class OutputController {
    private static Intersection intersection=new Intersection();
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
     *
     */
    static void outputSignal(IntersectionState currentState, Direction currentDirection) {
        if(currentDirection==Direction.NS){
            //North/South has control
            setRed(intersection.east_west);//Opposing Side Red
            nextState(intersection.north_south,intersection.north_south_ped, currentState);

        }else if(currentDirection==Direction.EW){
            //East/West has control
            setRed(intersection.north_south);//Opposing Side Red
            nextState(intersection.east_west, intersection.east_west_ped,currentState);
        }else if(currentDirection==Direction.N){
            //North has control
            setRed(intersection.north);//Opposing Side Red
            nextState(intersection.north,intersection.north_south_ped,currentState);

        }else if(currentDirection==Direction.E){
            //North has control
            setRed(intersection.east);//Opposing Side Red
            nextState(intersection.east,intersection.east_west_ped,currentState);

        }else if(currentDirection==Direction.S){
            //North has control
            setRed(intersection.south);//Opposing Side Red
            nextState(intersection.south,intersection.north_south_ped,currentState);

        }else if(currentDirection==Direction.W){
            //North has control
            setRed(intersection.west);//Opposing Side Red
            nextState(intersection.west,intersection.east_west_ped,currentState);

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
        pedList.get(0).setColor((currentState.ped));//Ped
        if(trafficList.size()>3){
            trafficList.get(3).setColor((currentState.turn));//Left Turn
            trafficList.get(4).setColor((currentState.straight));//Straight
            trafficList.get(5).setColor((currentState.straight));//Straight
            pedList.get(1).setColor((currentState.ped));//Ped
        }



    }
}

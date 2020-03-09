package Primary;
import java.util.LinkedList;

class OutputController {
    static void handleEmergencyState(LinkedList<Lanes> laneList) {
        laneList.get(0).setColor(SignalColor.GREEN);//Left Turn
        laneList.get(1).setColor(SignalColor.GREEN);//Straight
        laneList.get(2).setColor(SignalColor.GREEN);//Straight

    }
    static void stopPeds(LinkedList<Lights> nsPed, LinkedList<Lights> ewPed, IntersectionState currentState) {
        nsPed.get(0).setColor((currentState.ped));//All Ped Stop
        nsPed.get(1).setColor((currentState.ped));//All Ped Stop
        ewPed.get(0).setColor((currentState.ped));//All Ped Stop
        ewPed.get(1).setColor((currentState.ped));//All Ped Stop
    }
}

package Primary;

public class IntersectionState {

    //Output States
    public SignalColor straight =SignalColor.RED;
    public SignalColor turn=SignalColor.RED;
    public SignalColor ped=SignalColor.RED;


    //Timers
    public int timer=0;

    //Transition States
    public IntersectionState next;
    public IntersectionState nextLeftTurn;
    public IntersectionState nextPedestrian;
    public IntersectionState nextEmergency;

    public boolean changeDirection=false;//Whether the light should change direction

    public boolean overrideDuringNight=false;//Whether the light should change direction

    public IntersectionState(SignalColor light_Set, SignalColor turn_Set, SignalColor ped_Set){
        straight=light_Set;
        turn=turn_Set;
        ped=ped_Set;

    }
}

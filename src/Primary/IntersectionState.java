package Primary;

/**
 * IntersectionState
 * Describes valid intersection states
 */
public class IntersectionState {

    //Output States
    public SignalColor straight=SignalColor.RED;
    public SignalColor turn=SignalColor.RED;
    public SignalColor ped=SignalColor.RED;


    //Timers
    public int timer=0;//Default Immediate

    //Transition States
    public IntersectionState next;
    public IntersectionState nextLeftTurn;
    public IntersectionState nextPedestrian;
    public IntersectionState nextEmergency;

    public boolean changeDirection=false;//Whether the light should change direction
    public boolean overrideDuringNight=false;//Whether the light phase can be shortened during night

    /**
     * Constructor:IntersectionState
     * Sets Output for State
     * @param light_Set the current light set
     * @param turn_Set the turn set for color
     * @param ped_Set the pedestrian color set
     */
    public IntersectionState(SignalColor light_Set, SignalColor turn_Set, SignalColor ped_Set){
        straight=light_Set;
        turn=turn_Set;
        ped=ped_Set;

    }
}

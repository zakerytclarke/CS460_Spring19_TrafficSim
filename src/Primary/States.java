package Primary;


/**
 * State
 * Defines valid states of the intersection,
 * their trnasitions, and other system settings
 */
public class States {
    public int yellowClearanceInterval=2000;
    public int redClearanceInterval=500;


    //All states red
    public static IntersectionState Red_All = new IntersectionState(
            SignalColor.RED,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian

    //Green left turn
    public static IntersectionState Green_Turn = new IntersectionState(
            SignalColor.RED,//Light
            SignalColor.GREEN,//Turn
            SignalColor.RED);//Pedestrian

    //Yellow left turn
    public static IntersectionState Yellow_Turn = new IntersectionState(
            SignalColor.RED,//Light
            SignalColor.YELLOW,//Turn
            SignalColor.RED);//Pedestrian

    //Red Turn (equal to all reds)
    public static IntersectionState Red_Turn = new IntersectionState(
            SignalColor.RED,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian

    //Green pedestrian cross
    public static IntersectionState Green_Ped = new IntersectionState(
            SignalColor.GREEN,//Light
            SignalColor.RED,//Turn
            SignalColor.GREEN);//Pedestrian

    //Green pedestrian cross
    public static IntersectionState Yellow_Ped = new IntersectionState(
            SignalColor.YELLOW,//Light
            SignalColor.RED,//Turn
            SignalColor.YELLOW);//Pedestrian

    //Red pedestrian cross
    public static IntersectionState Red_Ped = new IntersectionState(
            SignalColor.RED,//NS Light
            SignalColor.RED,//NS Turn
            SignalColor.RED);//EW Pedestrian

    //Green straight
    public static IntersectionState Green_Light = new IntersectionState(
            SignalColor.GREEN,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian

    //Yellow straight
    public static IntersectionState Yellow_Light = new IntersectionState(
            SignalColor.YELLOW,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian

    //Red straight (all reds)
    public static IntersectionState Red_Light = new IntersectionState(
            SignalColor.RED,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian


    //Emergency Preemption
    //Transitions
    public static IntersectionState Yellow_Light_Emergency = new IntersectionState(
            SignalColor.YELLOW,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian
    public static IntersectionState Yellow_Turn_Emergency = new IntersectionState(
            SignalColor.RED,//Light
            SignalColor.YELLOW,//Turn
            SignalColor.RED);//Pedestrian
    public static IntersectionState Yellow_Ped_Emergency = new IntersectionState(
            SignalColor.YELLOW,//Light
            SignalColor.RED,//Turn
            SignalColor.YELLOW);//Pedestrian
    public static IntersectionState Emergency_Red = new IntersectionState(
            SignalColor.RED,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian
    public static IntersectionState Yellow_Light_Out_Emergency = new IntersectionState(
            SignalColor.YELLOW,//Light
            SignalColor.RED,//Turn
            SignalColor.RED);//Pedestrian
    //Emergency State
    public static IntersectionState Emergency_State = new IntersectionState(
            SignalColor.GREEN,//Light
            SignalColor.GREEN,//Turn
            SignalColor.RED);//Pedestrian


    public static IntersectionState All_Yellow = new IntersectionState(
            SignalColor.YELLOW,//Light
            SignalColor.YELLOW,//Turn
            SignalColor.RED);//Pedestrian


    public States(){
        //Setup Connections
        Red_All.next=Green_Light;//Default Transition
        Red_All.nextLeftTurn=Green_Turn;//Default Transition
        Red_All.nextEmergency=Emergency_Red;//Emergency Transition
        Red_All.nextPedestrian=Green_Ped;//Default Transition
        Red_All.changeDirection=true;//Change direction of cycle
        Red_All.timer=redClearanceInterval;


        //Straight
        Green_Light.next=Yellow_Light;//Default Transition
        Green_Light.nextEmergency=Yellow_Light_Emergency;//Emergency Transition
        Green_Light.timer=7000;
        Green_Light.overrideDuringNight=true;
        Yellow_Light.next=Red_Light;//Default Transition
        Yellow_Light.timer=yellowClearanceInterval;
        Yellow_Light.nextEmergency=Yellow_Light_Emergency;//Emergency Transition
        Red_Light.next=Red_All;//Default Transition
        Red_Light.timer=redClearanceInterval;
        Red_Light.nextEmergency=Emergency_State;//Emergency Transition

        //Turn
        Green_Turn.next=Yellow_Turn;//Default Transition
        Green_Turn.nextEmergency=Yellow_Turn_Emergency;//Emergency Transition
        Green_Turn.timer=5000;
        Green_Turn.overrideDuringNight=true;
        Yellow_Turn.next=Red_Turn;//Default Transition
        Yellow_Turn.nextEmergency=Yellow_Turn_Emergency;//Emergency Transition
        Yellow_Turn.timer=yellowClearanceInterval;
        Red_Turn.next=Green_Light;//Default Transition
        Red_Turn.nextPedestrian=Green_Ped;//Default Transition
        Red_Turn.timer=redClearanceInterval;
        Red_Turn.nextEmergency=Emergency_State;//Emergency Transition

        //Pedestrians
        Green_Ped.next=Yellow_Ped;//Default Transition
        Green_Ped.nextEmergency=Yellow_Ped_Emergency;//Default Transition
        Green_Ped.timer=7000;
        //Green_Ped.overrideDuringNight=true;
        Yellow_Ped.next=Red_Ped;//Default Transition
        Yellow_Ped.nextEmergency=Yellow_Ped_Emergency;//Emergency Transition
        Yellow_Ped.timer=yellowClearanceInterval;
        Red_Ped.next=Red_All;//Default Transition
        Red_Ped.timer=redClearanceInterval;

        //Emergency
        Yellow_Light_Emergency.next=Emergency_Red;//Default Transition
        Yellow_Light_Emergency.timer=yellowClearanceInterval;
        Yellow_Turn_Emergency.next=Emergency_Red;//Default Transition
        Yellow_Turn_Emergency.timer=yellowClearanceInterval;
        Yellow_Ped_Emergency.next=Emergency_Red;//Default Transition
        Yellow_Ped_Emergency.timer=yellowClearanceInterval;
        Emergency_Red.next=Emergency_State;//Default Transition
        Emergency_Red.changeDirection=true;//Switch to Emergency Vehicle Direction
        Emergency_Red.timer=yellowClearanceInterval;
        Emergency_State.next=Yellow_Light_Out_Emergency;//Transition back to Normal
        Emergency_State.timer=0;
        Yellow_Light_Out_Emergency.next=Red_All;//Default Transition
        Yellow_Light_Out_Emergency.timer=yellowClearanceInterval;


    }
}

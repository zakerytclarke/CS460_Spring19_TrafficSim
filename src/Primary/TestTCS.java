package Primary;

import com.sun.tools.internal.ws.wsdl.document.Output;

import java.util.LinkedList;

/**
 * TestTCS is the access point for Traffic Control System (TCS) interaction with the testbed.
 * There exist a total of 5 method calls for interacting with the testbed properly.
 * These 5 methods are detailed and demo'd below. Additional tips are included for
 * interacting with the testbed.
 *
 *   Methods of interest.
 *       Class: Lanes
 *           public boolean getCarOnLane()
 *           public boolean getEmergencyOnLane()
 *           public void setColor(SignalColor color)
 *
 *       Class: Lights
 *           public void setColor(SignalColor c)
 *           public boolean isPedestrianAt()
 *
 */
class TestTCS extends Thread {

    private int count = 0;

    private Boolean running = true;

    /**
     * TestTCS.begin() is the communication point between the testbed and the
     * TCS being tested on. Interactions between the testbed and TCS should all
     * be laid out in this method.
     */
    public void begin() {

        //TimedModeTest.run();
        //ResponsiveTest.run();
        //EmergencyModeTest.run();

    }

    /*
     * This is the old begin method. I kept it here for reference
     */
    public void testBegin() throws InterruptedException {
        //Day/Night Mode
        DayNight dayNightMode = DayNight.DAY;
        //Valid Intersection States

        //All states red
        IntersectionState Red_All = new IntersectionState(
                SignalColor.RED,//Light
                SignalColor.RED,//Turn
                SignalColor.RED);//Pedestrian

        //Green left turn
        IntersectionState Green_Turn = new IntersectionState(
                SignalColor.RED,//Light
                SignalColor.GREEN,//Turn
                SignalColor.RED);//Pedestrian

        //Yellow left turn
        IntersectionState Yellow_Turn = new IntersectionState(
                SignalColor.RED,//Light
                SignalColor.YELLOW,//Turn
                SignalColor.RED);//Pedestrian

        //Red Turn (equal to all reds)
        IntersectionState Red_Turn = new IntersectionState(
                SignalColor.RED,//Light
                SignalColor.RED,//Turn
                SignalColor.RED);//Pedestrian

        //Green pedestrian cross
        IntersectionState Green_Ped = new IntersectionState(
                SignalColor.GREEN,//Light
                SignalColor.RED,//Turn
                SignalColor.GREEN);//Pedestrian

        //Green pedestrian cross
        IntersectionState Yellow_Ped = new IntersectionState(
                SignalColor.YELLOW,//Light
                SignalColor.RED,//Turn
                SignalColor.YELLOW);//Pedestrian

        //Red pedestrian cross
        IntersectionState Red_Ped = new IntersectionState(
                SignalColor.RED,//NS Light
                SignalColor.RED,//NS Turn
                SignalColor.RED);//EW Pedestrian

        //Green straight
        IntersectionState Green_Light = new IntersectionState(
                SignalColor.GREEN,//Light
                SignalColor.RED,//Turn
                SignalColor.RED);//Pedestrian

        //Yellow straight
        IntersectionState Yellow_Light = new IntersectionState(
                SignalColor.YELLOW,//Light
                SignalColor.RED,//Turn
                SignalColor.RED);//Pedestrian

        //Red straight (all reds)
        IntersectionState Red_Light = new IntersectionState(
                SignalColor.RED,//Light
                SignalColor.RED,//Turn
                SignalColor.RED);//Pedestrian




        int yellowClearanceInterval=2000;
        int redClearanceInterval=500;

        //Setup Connections
        Red_All.next=Green_Light;//Default Transition
        Red_All.nextLeftTurn=Green_Turn;//Default Transition
        Red_All.nextPedestrian=Green_Ped;//Default Transition
        Red_All.changeDirection=true;//Change direction of cycle
        Red_All.timer=0;


        //Straight
        Green_Light.next=Yellow_Light;//Default Transition
        Green_Light.nextEmergency=Yellow_Light;//Emergency Transition
        Green_Light.timer=7000;
        Green_Light.overrideDuringNight=true;
        Yellow_Light.next=Red_Light;//Default Transition
        Yellow_Light.timer=yellowClearanceInterval;
        Yellow_Light.nextEmergency=Yellow_Light;//Emergency Transition
        Red_Light.next=Red_All;//Default Transition
        Red_Light.timer=redClearanceInterval;

        //Turn
        Green_Turn.next=Yellow_Turn;//Default Transition
        Green_Turn.nextEmergency=Yellow_Turn;//Emergency Transition
        Green_Turn.timer=3000;
        Green_Turn.overrideDuringNight=true;
        Yellow_Turn.next=Red_Turn;//Default Transition
        Yellow_Turn.nextEmergency=Yellow_Turn;//Emergency Transition
        Yellow_Turn.timer=yellowClearanceInterval;
        Red_Turn.next=Green_Light;//Default Transition
        Red_Turn.nextPedestrian=Green_Ped;//Default Transition
        Red_Turn.timer=redClearanceInterval;

        //Pedestrians
        Green_Ped.next=Yellow_Ped;//Default Transition
        Green_Ped.nextEmergency=Yellow_Ped;//Default Transition
        Green_Ped.timer=7000;
        Green_Ped.overrideDuringNight=true;
        Yellow_Ped.next=Red_Ped;//Default Transition
        Yellow_Ped.nextEmergency=Yellow_Ped;//Emergency Transition
        Yellow_Ped.timer=yellowClearanceInterval;
        Red_Ped.next=Red_All;//Default Transition
        Red_Ped.timer=redClearanceInterval;


        IntersectionState currentState = Red_All;
        Direction currentDirection=Direction.EW;

         //List of Traffic Lights
        LinkedList<Lanes> north_south = new LinkedList<>();
        LinkedList<Lanes> east_west = new LinkedList<>();
        for(Lanes l: Lanes.values())
        {
            if(l.toString().contains("N") || l.toString().contains("S")) north_south.add(l);
            else east_west.add(l);
        }

        LinkedList<Lanes> north = new LinkedList<>();
        LinkedList<Lanes> south = new LinkedList<>();
        LinkedList<Lanes> east = new LinkedList<>();
        LinkedList<Lanes> west = new LinkedList<>();
        for(Lanes l: Lanes.values())
        {
            if(l.toString().contains("N")) north.add(l);
            if(l.toString().contains("S")) south.add(l);
            if(l.toString().contains("E")) east.add(l);
            if(l.toString().contains("W")) west.add(l);
        }

        //List of Pedestrian Lights
        LinkedList<Lights> north_south_ped = new LinkedList<>();
        LinkedList<Lights> east_west_ped = new LinkedList<>();
        for(Lights l: Lights.values())
        {
            if(l.toString().contains("EAST") || l.toString().contains("WEST")) north_south_ped.add(l);//Opposites match uo to lanes
            else east_west_ped.add(l);
        }



        long currentTime=System.currentTimeMillis();
        long currentTimer=currentState.timer;
        long currentPedestrianTimer=3000;

        while(running){

            //Execute current state
            OutputController.outputSignal(currentState,currentDirection,north_south,east_west,north_south_ped,east_west_ped);

            //Check if we should transition to the next state

            //Check Emergency
            Direction emergency = ExceptionalStateController.detectEmergency(north,south,east,west);

            if(emergency != null){//Check Emergency Preemption
                //Transition out of state to emergency
                if(currentState.nextEmergency!=null){
                    currentState=currentState.nextEmergency;
                    OutputController.outputSignal(currentState,currentDirection,north_south,east_west,north_south_ped,east_west_ped);
                    sleep(currentState.timer);
                }

                OutputController.setRed(north_south);//All cars stop
                OutputController.setRed(east_west);//All cars stop
                OutputController.outputSignal(currentState,currentDirection,north_south,east_west,north_south_ped,east_west_ped);
                OutputController.stopPeds(north_south_ped, east_west_ped, currentState);
                sleep(redClearanceInterval);
                if(emergency==Direction.N){
                    OutputController.handleEmergencyState(north);
                }
                if(emergency==Direction.S){
                    OutputController.handleEmergencyState(south);
                }
                if(emergency==Direction.E){
                    OutputController.handleEmergencyState(east);
                }
                if(emergency==Direction.W){
                    OutputController.handleEmergencyState(west);
                }
                while(emergency== ExceptionalStateController.detectEmergency(north,south,east,west)){
                    //Wait until ambulance passes
                }
                sleep(redClearanceInterval);
                //Clearance Interval for Emergency Vehicle
                OutputController.setRed(north_south);
                OutputController.setRed(east_west);
                sleep(3000);
                currentState=Red_All;

            }



            if(System.currentTimeMillis()>currentTime+currentTimer){//Check Timer has elapsed
                System.out.println(currentState.turn.toString()+":"+currentState.straight.toString()+":"+currentState.ped.toString()+":"+currentDirection.toString()+":"+currentTimer);
                if(currentState.changeDirection){//Change direction if cycle is done
                    if(currentDirection==Direction.NS){
                        currentDirection=Direction.EW;
                    }else{
                        currentDirection=Direction.NS;
                    }
                }

                boolean turnLaneOccupied, pedestrianOccupied;

                //Check if Left turn Lane is occupied
                if(currentDirection==Direction.NS){
                    turnLaneOccupied = VPStateController.carWaiting(north_south);
                }else{
                    turnLaneOccupied = VPStateController.carWaiting(east_west);
                }

                //Check if there are pedestrians
                if(currentDirection==Direction.NS){
                    pedestrianOccupied = VPStateController.pedWaiting(north_south_ped);
                }else{
                    pedestrianOccupied = VPStateController.pedWaiting(east_west_ped);
                }

                if(turnLaneOccupied && currentState.nextLeftTurn!=null){
                    currentState=currentState.nextLeftTurn;
                }
                else if(pedestrianOccupied && currentState.nextPedestrian!=null){
                    currentState=currentState.nextPedestrian;
                }else{
                    currentState=currentState.next;
                }


                currentTimer=currentState.timer;//Set next timer
                currentTime=System.currentTimeMillis();//Reset Timer
            }

            if(!dayNightMode.getDay() && currentState.overrideDuringNight){//Check Nighttime Mode
                //During Nightime Mode, you can ignore the timer
                // if there is no one traveling in this direction
                boolean isTrafficNS, isTrafficEW;

                isTrafficNS = VPStateController.anyCar(north_south) || VPStateController.anyPed(north_south_ped);
                isTrafficEW = VPStateController.anyCar(east_west) || VPStateController.anyPed(east_west_ped);

                if(currentDirection==Direction.NS){//Check NS
                    if(isTrafficEW&&!isTrafficNS){
                        currentState=currentState.next;
                        currentTimer=currentState.timer;//Set next timer
                        currentTime=System.currentTimeMillis();//Reset Timer
                    }
                }else{//Check EW
                    if(isTrafficNS&&!isTrafficEW){
                        currentState=currentState.next;
                        currentTimer=currentState.timer;//Set next timer
                        currentTime=System.currentTimeMillis();//Reset Timer
                    }
                }

            }

        }
        System.out.println("Test ended..");
    }


    public void end(){
        running = false;
    }


}

package Primary;

import static java.lang.Thread.sleep;

/**
 * TICS
 * Traffic Intersection Controller
 */
public class TICS {
    public boolean running;

    /**
     * run()
     * Runs the TICS
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        running=true;

        InputController inputController=new InputController();//Input Controller
        OutputController outputController=new OutputController();//Output Controller
        Intersection intersection=new Intersection();
        IntervalStateController intervalStateController=new IntervalStateController();
        States states = new States();//Valid Intersection States


        //Start all Red
        IntersectionState currentState = states.Red_All;

        Direction currentDirection=Direction.EW;//Direction that has right of way



        long currentTime=System.currentTimeMillis();
        long currentTimer=currentState.timer;//Timer tracker


        while(running){//Runtime Loop

            System.out.println(currentState.straight+":"+currentState.turn+":"+currentState.ped+":"+currentState.timer+":"+currentDirection);
            //Execute current state
           outputController.outputSignal(currentState,currentDirection);

            //Check if we should transition to the next state

            //Check Emergency
            Direction emergency = ExceptionalStateController.detectEmergency();

            if(emergency != null) {//Check Emergency Preemption
                if(!(currentState.straight==SignalColor.RED&&
                        currentState.turn==SignalColor.RED&&
                        currentState.ped==SignalColor.RED)
                ){
                    currentState=states.All_Yellow;//Clearance Interval
                    outputController.outputSignal(currentState,currentDirection);
                    sleep(states.yellowClearanceInterval);
                }


               currentState=states.Emergency_Red;//Red Clearance Interval
               outputController.outputSignal(currentState,currentDirection);
               sleep(states.redClearanceInterval);
               Direction prevDirection=currentDirection;
               currentDirection=emergency;
               currentState=states.Emergency_State;//Emergency Crossing State
               outputController.outputSignal(currentState,currentDirection);
               while( ExceptionalStateController.detectEmergency()==emergency);// Wait for Emergency Vehicle to Pass
               sleep(states.yellowClearanceInterval);
               currentState=states.All_Yellow;//Yellow Clearance Out
               outputController.outputSignal(currentState,currentDirection);
               sleep(states.yellowClearanceInterval);
               currentState=states.Emergency_Red;//Red Clearance Out
               currentDirection=prevDirection;
               /*
               outputController.outputSignal(currentState,currentDirection);
               sleep(states.yellowClearanceInterval);
               currentState=states.Red_All;
               */

            }





            if(System.currentTimeMillis()>currentTime+currentTimer&&currentState.timer!=-1){//Check Timer has elapsed
                if(currentState.changeDirection&&emergency==null){//Change direction if cycle is done
                    if(currentDirection==Direction.NS){
                        currentDirection=Direction.EW;
                    }else{
                        currentDirection=Direction.NS;
                    }
                }

                boolean turnLaneOccupied, pedestrianOccupied;

                //Check if Left turn Lane is occupied
                if(currentDirection==Direction.NS){
                    turnLaneOccupied = inputController.carWaiting(intersection.north_south);
                }else{
                    turnLaneOccupied = inputController.carWaiting(intersection.east_west);
                }

                //Check if there are pedestrians
                if(currentDirection==Direction.NS){
                    pedestrianOccupied = inputController.pedWaiting(intersection.north_south_ped);
                }else{
                    pedestrianOccupied = inputController.pedWaiting(intersection.east_west_ped);
                }

                if(turnLaneOccupied && currentState.nextLeftTurn!=null){
                    currentState=currentState.nextLeftTurn;
                }
                else if(pedestrianOccupied && currentState.nextPedestrian!=null){
                    currentState=currentState.nextPedestrian;
                }else{
                    currentState=currentState.next;
                }


                if(currentState.timer!=-1){//Transition Allowed
                    currentTimer=currentState.timer;//Set next timer
                    currentTime=System.currentTimeMillis();//Reset Timer
                }

            }

            if(!intervalStateController.isDay() && currentState.overrideDuringNight){//Check Nighttime Mode
                //During Nighttime Mode, you can ignore the timer
                // if there is no one traveling in this direction
                boolean isTrafficNS, isTrafficEW;

                isTrafficNS = inputController.anyCar(intersection.north_south) || inputController.anyPed(intersection.north_south_ped);
                isTrafficEW = inputController.anyCar(intersection.east_west) || inputController.anyPed(intersection.east_west_ped);

                if(currentDirection==Direction.NS){//Check NS
                    if(isTrafficEW&&!isTrafficNS){
                        if(currentState.timer!=-1) {//Transition
                            currentState=currentState.next;
                            currentTimer=currentState.timer;//Set next timer
                            currentTime=System.currentTimeMillis();//Reset Timer
                        }
                    }
                }else{//Check EW
                    if(isTrafficNS&&!isTrafficEW){
                        if(currentState.timer!=-1){//Transition
                            currentState=currentState.next;
                            currentTimer=currentState.timer;//Set next timer
                            currentTime=System.currentTimeMillis();//Reset Timer
                        }
                    }
                }

            }

        }
    }
}

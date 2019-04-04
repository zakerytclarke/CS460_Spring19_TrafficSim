package Primary;

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
    public void testBegin() {

        /*
        RED (Color.RED),
        YELLOW (Color.YELLOW),
        GREEN (Color.GREEN),
        BLACK (Color.BLACK);

        SignalColor is an enum holding possible signal colors.
         */
        SignalColor north_south_color, east_west_color;

        /*
        This is a useful way of grouping lights by direction.
        Here we are grouping parallel directions north with south, and east with west.
         */
        LinkedList<Lanes> north_south = new LinkedList<>();
        LinkedList<Lanes> east_west = new LinkedList<>();
        for(Lanes l: Lanes.values())
        {
            if(l.toString().contains("N") || l.toString().contains("S")) north_south.add(l);
            else east_west.add(l);
        }

        while(running){
            /*
            This is a simple way of alternating the states of signal colors on a timed basis.
             */

            if (count %  4 ==  0){
                north_south_color = SignalColor.GREEN;
                east_west_color = SignalColor.RED;
            } else if (count % 4 == 1){
                north_south_color = SignalColor.YELLOW;
                east_west_color = SignalColor.RED;
            } else if (count % 4 == 2){
                north_south_color = SignalColor.RED;
                east_west_color = SignalColor.GREEN;
            } else {
                north_south_color = SignalColor.RED;
                east_west_color = SignalColor.YELLOW;
            }

            /*
            This changes our grouping of lanes to the colors specified above.
             */
            for(Lanes l: north_south)
            {
                l.setColor(north_south_color);
            }
            for(Lanes l: east_west)
            {
                l.setColor(east_west_color);
            }
            Lights.WEST.setColor(SignalColor.GREEN);
            count ++;


            testSensors();

            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Test ended..");
    }

    public void end(){
        running = false;
    }

    /*
    Loops over the ped crosswalks and car lanes to
    print if each has traffic waiting on it
    */
    private void testSensors() {
        for (Lanes l : Lanes.values()) {
            System.out.println(l.toString() + " has car waiting: " + l.isCarOnLane());
        }

        for (Lights l : Lights.values()){
            System.out.println(l.toString() + " has ped waiting: " + l.isPedestrianAt());
        }
    }

}

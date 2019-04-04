package Primary;

import Graphics.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Controller extends Thread{

    private Simulation sim;
    private GraphicsContext gc;
    private TestTCS test;

    public volatile static int threadCount = 0; // used to see how many threads need to move before draw update
    public static final Object countLock = new Object(); // Used to lock the threadCount when changed
    public static final Object simLock = new Object(); // Used to lock the threadCount when changed

    private ScheduledExecutorService spawner;
    private ScheduledFuture<?> spawnInterval;

    public Controller(GraphicsContext gc){
        this.gc = gc;
        this.sim = new Simulation(gc);
    }

    public void run(){
        try
        {
            // Displaying the thread that is running
            System.out.println ("Thread " +
                    Thread.currentThread().getId() + " Controller Thread" +
                    " is running");

            spawner = Executors.newScheduledThreadPool(1);
            spawnInterval = spawner.scheduleAtFixedRate(() -> spawnCar(), 1000000, 1000000, TimeUnit.SECONDS);

            Animation a = new Animation();
            a.start();

            this.test = new TestTCS();
            test.testBegin();
        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught: " + e.toString());
        }
    }

    public void rushMode(Label label){
        label.setText("Modes:\nRush Hour\nVehicle & Pedestrian\nPeriod = 0.25s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnBoth(), 100, 200, TimeUnit.MILLISECONDS);
    }


    public void heavyMode(Label label)
    {
        label.setText("Modes:\nHeavy traffic\nPeriod = 0.5s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnBoth(), 100, 500, TimeUnit.MILLISECONDS);
    }

    public void moderateMode(Label label)
    {
        label.setText("Modes:\nModerate traffic\nPeriod = 1s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnBoth(), 100, 1000, TimeUnit.MILLISECONDS);
    }

    public void lightMode(Label label)
    {
        label.setText("Modes:\nLight traffic\nPeriod = 2s");
        spawnInterval.cancel(false);
        spawnInterval = spawner.scheduleAtFixedRate(() -> spawnCar(), 100, 2000, TimeUnit.MILLISECONDS);
    }


    public void spawnBoth()
    {
        spawnCar();
        spawnPed();
    }


    public void walkFaster(Boolean b, Label label) {
        sim.addPedSpeed(b);
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(sim.pedSpeed);
        label.setText(formatted);
    }

    public void driveFaster(Boolean b, Label label) {
        sim.addCarSpeed(b);
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(sim.carSpeed);
        label.setText(formatted);

    }


    // Button press action to spawn a car
    //
    public void spawnCar(){
        sim.spawnCar(false);
    }

    public void spawnEmergency(){
        sim.spawnCar(true);
    }

    // Button press action to spawn a pedestrian
    //
    public void spawnPed(){
        sim.spawnPed();
    }

    // Button press action to remove all the traffic threads
    //
    public void reset(){
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        spawnInterval.cancel(false);
        this.sim = new Simulation(gc);
    }


    // Inner gui updating class that moves and redraws traffic on a timer
    //
    class Animation extends AnimationTimer
    {
        private Boolean ending = false; // collision is true if any cars have collided in intersection
        private Boolean willEnd = false;

        Animation(){}

        @Override
        public void handle(long now) // called by JavaFX at 60Hz
        {
            if (ending) resetSim(); // check if we should remake the sim

            // threadCount will be 0 when every single car thread has moved
            // then the sim can redraw all traffic at the new positions and notify
            // each thread it can move again

            if (threadCount == 0) {
                // comment out the next two collision lines and you can test while cars
                // don't care about hitting each other

                Boolean collision = sim.updateSpots(); // checks cars for collision
                if (collision && !willEnd) end(); // comment out just this line and you can cause huge car pile up collisions

                sim.drawTraffic(); // loop over all traffic and draw new positions
                sim.freeTraffic(); // notify all traffic they can move again
            }
        }

        // Called when any cars have collided in intersection,
        // uses a timer so cars can escape the crashes
        // if they weren't involved and then stops 3s later
        //
        private void end(){
            willEnd = true; // so this isn't called multiple times while updating Gui
            spawnInterval.cancel(true);

            // Timed task to set up a new simulation after collisions
            //
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        ending = true;
                    });

                }
            };

            Timer timer = new Timer();
            timer.schedule(t, 3000);

        }

        // Calls the function that creates a new Simulation
        // and then resets the ending vars to false
        //
        private void resetSim(){
            System.out.println("resetting the sim");
            reset();
            ending = false;
            willEnd = false;
        }
    }

}

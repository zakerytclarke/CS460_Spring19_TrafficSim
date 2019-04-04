package Graphics.Traffic;

import Graphics.*;
import Graphics.Direction;
import Graphics.Grounds.Ground;
import Graphics.Grounds.LaneDisplay;
import Primary.Controller;
import Primary.SignalColor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import java.util.Random;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Car extends Thread{

    private Ground ground; // keeps track of which Ground piece it's on, could be LaneDisplay or Intersection
    private final Ground dest; // Always the ground piece it's heading to
    public Boolean emergency; // if emergency vehicle then true

    private double speed; // Change this to increase or decrease car thread's animation speed

    private final GraphicsContext gc;
    private Boolean isMoving = true;
    private double laneLength;
    private double destLength;
    private Paint color;

    private CarSignalDisplay carSignalDisplay; // CarSignalDisplay it's checking for when it can go
    private Car lead; // Reference to the car in front of it in each lane (could be null if first)

    private double carX;
    private double carY;
    private int width;
    private int height;
    private Direction side;

    private int pathType; // 0 = straight, 1 = right, 2 = left
    private double rotation = 0; // keeps track of rotation angle when turning
    private double rotationRate = 0;

    public Boolean running = true; // running is true if car hasn't arrived at destination
    private Boolean triggered = false;  // true if car has triggered the lane sensor

    private Boolean isLeaving = false; // is true when car has passed through intersection
    private Boolean willSwitch = false; // is true when car is about to switch Ground components
    private Boolean atCross = false; // true when car is waiting at crosswalk waitng for light

    public Boolean collision = false; // is true when collided within intersection and stopped
    public int needsGroundUpdate = 0; // is 1 or 2 when it has entered or exited the intersection

    private final Paint[] col = {
            Paint.valueOf("#ff8888"), // Array of colors
            Paint.valueOf("#88ff88"), // for the rectangle car to be
            Paint.valueOf("#8888ff"),
            Paint.valueOf("#8f8f8f"),
            Paint.valueOf("#ffffff")
    };


    public Car(Direction side, Ground ground, Ground dest, Boolean emergency, GraphicsContext gc, double speed){
        this.gc = gc;
        this.ground = ground;
        this.dest = dest;
        this.side = side;
        this.speed = speed;
        this.emergency = emergency;
        this.color = randomColor();

        this.lead = ground.getLast(); // get Car to check for collision
        this.ground.setLast(this); // set this Car as last on the current Ground piece

        this.carX = ground.x;
        this.carY = ground.y;
        this.laneLength = (gc.getCanvas().getWidth() - Simulation.size) / 2;
        this.destLength = laneLength;

        if (emergency) triggerEmergencySensor(true);

        setUpCar(this.side); // set up x, y, width, height for car
        setPathType(); // set if going straight = 0, right = 1, left = 2
        this.start();
    }

    // Used when intersection is checking for collisions
    //
    public double getCarX(){
        return carX;
    }

    public double getCarY(){
        return carY;
    }


    @Override
    public void run() {
        increment();
        while (running){
            updatePosition(); // make a move or stay stopped
            waiter(); // wait to be drawn
        }
        decrement();
    }


    // Notify car it has been drawn so it can move again
    //
    public synchronized void free() {
        this.notify();
    }

    // Decrement the total threadCount back towards 0 after move update
    // then wait until drawn and notified to increment back up for another move
    //
    private synchronized void waiter(){
        decrement();
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        increment();
    }

    private void increment(){
        synchronized (Controller.countLock)
        {
            Controller.threadCount++;
        }
    }

    private void decrement(){
        synchronized (Controller.countLock)
        {
            Controller.threadCount--;
        }
    }

    // Called in animation loop for each car that
    // needsGroundUpdate and will do one of four things
    //
    public void updateGround(){
        if (needsGroundUpdate == 2){ // remove car from crosswalk
            ground.getCrossing().removeCar(this);
        } else if (needsGroundUpdate == 3){ // add car to crosswalk
            ground.getCrossing().addCar(this);
        } else if (ground.type == 1){ // 1 type = intersection, add car to intersection
            ground.addCar(this);
            needsGroundUpdate = 0;
        } else if (ground.type == 0){ // 0 type = lane, remove car from intersection
            ground.getIntersection().removeCar(this);
            needsGroundUpdate = 0;
        }
    }

    // Triggers the lane sensor when a car is waiting at a red light
    //
    private void onLaneSensor(Boolean on){
        LaneDisplay l = (LaneDisplay) ground;
        l.setCarOnSensor(on);
    }

    // Triggers the emergency sensor when a new emergency vehicle is spawned on lane
    //
    private void triggerEmergencySensor(Boolean on){
        LaneDisplay l = (LaneDisplay) ground;
        l.setEmergencySensor(on);
    }



    /*
    Keep going unless stopped then check five different scenarios --

    1 ) if ground is a LaneDisplay type ( 0 ) and car is incoming then switch to intersection and remove from crossing and sensor
    2 ) if car is at the crosswalk then check if light is green then move else trigger the lane sensor
    3 ) if car is leaving then it must've arrived so stop running
    4 ) if collision then stop the car
    5 ) if ground type is intersection then switch to destination lane and add to crosswalk on dest road
    6 ) else check if car in lead has moved yet
    */
    private void updatePosition() {
        if (isMoving) {
            move();
        } else {
            if (ground.type == 0 && !isLeaving && willSwitch) {
                atCross = false;
                synchronized (Controller.simLock) {
                    ground.getCrossing().removeCar(this); // remove car from crosswalk
                }
                switchToIntersection();
            } else if (atCross){
                if (carSignalDisplay.getColor() == SignalColor.GREEN) { // check if light is green
                    isMoving = true;
                } else if (!triggered){ // trigger the lane sensor that we're waiting
                    onLaneSensor(true);
                    triggered = true;
                }
            } else if (isLeaving){ // arrived so stop running
                running = false;
            } else if(collision){ // collided so stop moving
                isMoving = false;
            } else if (ground.type == 1){ // leaving intersection to dest lane
                switchToDest();
                needsGroundUpdate = 3; // triggers the car to be added to the crosswalk
                atCross = true;
            } else { // car must be just stopped behind another so check if that one moved
                checkCollision();
            }
        }
    }

    // Setup car width, height, x, ,and y offsets depending on road
    //
    private void renderCar(Direction side)
    {
        if (side == Direction.NORTH || side == Direction.SOUTH) {
            width = 8;
            height = 16;
            carX += 5;
            if (emergency){
                width += 2;
                height += 5;
            }
        } else {
            width = 16;
            height = 8;
            carY += 5;
            if (emergency){
                width += 5;
                height += 2;
            }
        }


    }

    // Called by animation timer to draw the car -- this Boolean used t
    // to return if the car neededRefresh then would only redraw the intersection
    // if one of the cars needed it but that's not being used now
    //
    public void drawCar(){
        if (emergency){
            drawEmergency();
        } else {
            gc.setFill(color);

            if ((pathType == 1 || pathType == 2) && ground.type == 1) { // rotate for turn animation
                if (!collision)rotation += rotationRate;
                gc.save();
                gc.transform(new Affine(new Rotate(rotation, carX + width/2, carY + height/2)));
                gc.fillRect(carX, carY, width, height);
                gc.restore();
            } else { // draw normal car
                gc.fillRect(carX, carY, width, height);
            }
        }
    }


    // Draws a slightly bigger emergency car with different color
    // and small light on top
    //
    private void drawEmergency(){
        gc.setFill(Paint.valueOf("#ff0000"));

        if ((pathType == 1 || pathType == 2) && ground.type == 1) {
            if (!collision)rotation += rotationRate;
            gc.save();
            gc.transform(new Affine(new Rotate(rotation, carX + width/2, carY + height/2)));
            gc.fillRect(carX, carY, width, height);
            gc.setFill(Paint.valueOf("#ffffff"));
            gc.fillOval(carX + width/2 - 1, carY + height/2 - 1, 2, 2);
            gc.restore();
        } else {
            gc.fillRect(carX, carY, width, height);
            gc.setFill(Paint.valueOf("#ffffff"));
            gc.fillOval(carX + width/2 - 1, carY + height/2 - 1, 2, 2);
        }

    }

    // Changes the current Ground object from the start lane to
    // the Intersection object that is the neighbor of it
    //
    private synchronized void switchToIntersection() {

        onLaneSensor(false); // set lane sensor to false
        if (emergency) triggerEmergencySensor(false);

        Ground old = ground;
        ground = ground.getIntersection(); // sets current Ground object to be intersection
        laneLength = Simulation.size;
        isMoving = true;

        if (side == Direction.NORTH || side == Direction.SOUTH) {
            carY = ground.y;
            carX = old.x ;
            carX += 5;
            carY -= height;
        } else {
            carX = ground.x;
            carY = old.y;
            carY += 5;
        }


        if (side == Direction.SOUTH) {
            carY +=  laneLength;
        }

        if (side == Direction.EAST)
        {
            carX +=  laneLength;
        }

        // Tell the current ground object that this car is the last
        // one to be on th piece so if a new car is created then it knows
        // it's lead
        ground.setLast(this);
        needsGroundUpdate = 1; // need to add the car to intersection's list
    }


    // Switches from Intersection object to the destination LaneDisplay
    //
    private synchronized void switchToDest(){
        ground = dest;
        side = ground.side;
        laneLength = (gc.getCanvas().getWidth() - Simulation.size) / 2;
        isLeaving = true;
        carX = ground.x;
        carY = ground.y;
        isMoving = true;
        lead = ground.getLast();

        if (side == Direction.NORTH) {
            carY +=  laneLength;
        }

        if (side == Direction.WEST)
        {
            carX +=  laneLength - width;
        }

        renderCar(side);

        // Tell the current ground object that this car is the last
        // one to be on th piece so if a new car is created then it knows
        // it's lead
        ground.setLast(this);
        needsGroundUpdate = 1; // need to remove the car from intersection list
    }

    // Sets up the width, height, x, y, and carSignalDisplay for
    // a new car
    //
    private void setUpCar(Direction side){
        renderCar(side);
        if (ground.type == 0) {
            LaneDisplay l = (LaneDisplay) ground;
            carSignalDisplay = l.getCarSignalDisplay();
        }

        if (isLeaving) {
            if (side == Direction.NORTH) {
                carY += laneLength;
            }

            if (side == Direction.WEST)
            {
                carX +=  laneLength;
            }

        } else {
            if (side == Direction.SOUTH) {
                carY +=  laneLength;
            }

            if (side == Direction.EAST)
            {
                carX +=  laneLength;
            }
        }
    }

    // Determine if car is heading straight, right, or left
    // and set the rotation rates if turning
    //
    private void setPathType(){
        if (side == Direction.NORTH){
            if (dest.side == Direction.EAST){
                pathType = 2;
            } else if (dest.side == Direction.SOUTH){
                pathType = 0;
            } else if (dest.side == Direction.WEST){
                pathType = 1;
            }
        } else if (side == Direction.SOUTH){
            if (dest.side == Direction.EAST){
                pathType = 1;
            } else if (dest.side == Direction.NORTH){
                pathType = 0;
            } else if (dest.side == Direction.WEST){
                pathType = 2;
            }
        } else if (side == Direction.EAST){
            if (dest.side == Direction.NORTH){
                pathType = 1;
            } else if (dest.side == Direction.SOUTH){
                pathType = 2;
            } else if (dest.side == Direction.WEST){
                pathType = 0;
            }
        } else if (side == Direction.WEST){
            if (dest.side == Direction.EAST){
                pathType = 0;
            } else if (dest.side == Direction.SOUTH){
                pathType = 1;
            } else if (dest.side == Direction.NORTH){
                pathType = 2;
            }
        }

        if (pathType == 1) rotationRate = 3; else if(pathType == 2) rotationRate = -1 * speed;
    }


    // Checks if the car needs to switch Ground component then
    // moves a direction depending on the road it's on and if outgoing
    //
    private void move(){

        if (!isLeaving) checkCollision();
        checkBounds();

        if (isLeaving) {
            if (side == Direction.NORTH) {
                carY -= speed;
            } else if (side == Direction.SOUTH) {
                carY += speed;
            }else if (side == Direction.EAST) {
                carX += speed;
            } else if (side == Direction.WEST) {
                carX -= speed;
            }
        } else {
            if (side == Direction.NORTH) {
                if (ground.type == 1 && pathType == 1)carX -= speed * 0.3; // checks if car
                if (ground.type == 1 && pathType == 2) carX += speed * 0.55; // needs turning movement
                carY += speed;
            } else if (side == Direction.SOUTH) {
                if (ground.type == 1 && pathType == 1)carX += speed * 0.3;
                if (ground.type == 1 && pathType == 2) carX -= speed * 0.55;
                carY -= speed;
            }else if (side == Direction.EAST) {
                if (ground.type == 1 && pathType == 1)carY -= speed * 0.3;
                if (ground.type == 1 && pathType == 2) carY += speed * 0.55;
                carX -= speed;
            } else if (side == Direction.WEST) {
                if (ground.type == 1 && pathType == 1)carY += speed * 0.3;
                if (ground.type == 1 && pathType == 2) carY -= speed * 0.55;
                carX += speed;
            }
        }
    }

    private Paint randomColor(){
        Random r = new Random();
        int range = (5 - 1) + 1;
        int rand =  r.nextInt(range);
        return col[rand];
    }



    // Check car thread leading this one to see if it's within bounds
    // for collision then stops if so
    //
    private void checkCollision() {
        if (ground.type == 1 && isMoving){ // if on the intersection
            Boolean result = ground.checkCollision(this); // then check all other cars on it
            if (result){ // result = true when car has crashed
                color = Paint.valueOf("#ff0000");
                isMoving = false;
                collision = true;
            }

        } else if (lead != null) { // Check the car in front for collision
            if (side == Direction.NORTH || side == Direction.SOUTH){
                double yDif = Math.abs(lead.carY - carY);
                isMoving = !(yDif < height + 5);
                if (lead.ground != this.ground) isMoving = true;
            } else {
                double xDif = Math.abs(lead.carX - carX);
                if (xDif < width + 5) {
                    isMoving = false;
                } else {
                    isMoving = true;
                }
                if (lead.ground != this.ground) isMoving = true;
            }
        } else { // the car in front must've moved onto different component so start moving
            isMoving = true;
        }
    }


    // Checks if the car is about to exit the current Ground object it's on
    //
    private void checkBounds() {

        if (ground.type == 1 && (pathType == 1 || pathType == 2)){ // checks when to switch to dest while turning
            if (side == Direction.SOUTH && carY < dest.y){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.NORTH && carY > dest.y){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.EAST && carX < dest.x){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.WEST && carX > dest.x){
                isMoving = false;
                willSwitch = true;
            }
        } else if (ground.type == 1 && pathType == 0){ // on the intersection and headed straight through
            if (side == Direction.SOUTH && carY < dest.y + destLength ){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.NORTH && carY > dest.y){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.EAST && carX < dest.x + destLength - width){
                isMoving = false;
                willSwitch = true;
            } else if (side == Direction.WEST && carX > dest.x){
                isMoving = false;
                willSwitch = true;
            }
        } else if (isLeaving) { // checks when to stop moving on arrival
            if (side == Direction.NORTH) {
                if (atCross && carY < ground.y + laneLength - 20){
                    atCross = false;
                    needsGroundUpdate = 2;
                } else if(carY < ground.y){
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.SOUTH){
                if (atCross && carY > ground.y + 20){
                    needsGroundUpdate = 2;
                    atCross = false;
                } else if (carY > ground.y - height + laneLength){
                    isMoving = false;
                    willSwitch = true;
                }
            }

            if (side == Direction.EAST){
                if (atCross && carX > ground.x + 20){
                    needsGroundUpdate = 2;
                    atCross = false;
                }else if (carX > ground.x - width + laneLength) {
                    isMoving = false;
                    willSwitch = true;
                }
            }
            if (side == Direction.WEST){
                if (atCross && carX < ground.x + laneLength -20){
                    needsGroundUpdate = 2;
                    atCross = false;
                }else if (carX < ground.x) {
                    isMoving = false;
                    willSwitch = true;
                }
            }

        } else if (ground.type == 0 && !isLeaving){ // checks when to stop moving on lane to switch to intersection
            if (side == Direction.NORTH) {
                if (carY > ground.y - height + laneLength ) {
                    isMoving = false;
                    willSwitch = true;
                } else if (carY > ground.y - height + laneLength - 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    needsGroundUpdate = 3;
                }
            }

            if (side == Direction.SOUTH) {
                if (carY < ground.y - height){
                    isMoving = false;
                    willSwitch = true;
                } else if (carY < ground.y + 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    needsGroundUpdate = 3;
                }
            }

            if (side == Direction.EAST){
                if (carX < ground.x) {
                    isMoving = false;
                    willSwitch = true;
                } else if (carX < ground.x + 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    needsGroundUpdate = 3;
                }
            }
            if (side == Direction.WEST) {
                if (carX > ground.x + laneLength) {
                    isMoving = false;
                    willSwitch = true;
                } else if (carX > ground.x - width + laneLength - 20 && !atCross){
                    atCross = true;
                    isMoving = false;
                    needsGroundUpdate = 3;
                }
            }
        }
    }
}

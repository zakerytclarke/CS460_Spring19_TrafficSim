package Graphics.Grounds;

import Graphics.Direction;
import Graphics.Traffic.Car;
import Graphics.Traffic.Pedestrian;
import Primary.Controller;
import java.util.LinkedList;

public class Ground {

    private Ground neighbor;
    public double x;
    public double y;
    public int type;
    public Direction side;
    public int count = 0;

    // keeps track of the last Car in line on the component so if a
    // new one is spawned it knows the lead Car in front of it
    private Car last;
    private Pedestrian pedLastEW;
    private Pedestrian pedLastNS;
    private Crossing crosswalk;

    private LinkedList<Car> cars = new LinkedList<>();



    // Allows a RoadDisplay or LaneDisplay object to have reference to the Intersection
    // object that is a neighbor of it for Cars to transfer onto
    //
    void setIntersection(Ground neighbor) {
        this.neighbor = neighbor;
    }

    public Ground getIntersection() {
        return this.neighbor;
    }

    void setPosition(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.type = type; // type = 1 if Intersection, type = 0 if LaneDisplay
    }

    // Sets the last car spawned on a lane component
    //
    public void setLast(Car last){
        this.last = last;
    }

    // Sets the last ped spawned on a corner depending on dir
    //
    public void setPedLast(Pedestrian last, Boolean isVert){
        if (isVert) {
            pedLastNS = last;
        } else {
            pedLastEW = last;
        }

    }

    // Used on Car creation to ask the object the last car in the lane
    // so it knows which car to check for collision
    //
    public Car getLast(){
        return last;
    }

    // Used on Ped creation to ask the object the last ped on the corner
    // so it knows which ped to check for collision
    //
    public Pedestrian getLastPed(Boolean isVert){
        if (isVert) {
            return pedLastNS;
        }
        return  pedLastEW;
    }

    public void setCrosswalk(Crossing crosswalk){
        this.crosswalk = crosswalk;
    }

    public void addCar(Car c){
        cars.add(c);
    }

    public void removeCar(Car c ){
        cars.remove(c);
    }

    public void clearOut(){
        while (!cars.isEmpty()){
            cars.remove();
        }

        last = null;
    }

    public Crossing getCrossing(){
        return crosswalk;
    }


    // Check the passed in car to see if it has collided
    // with any of the other cars in the intersection and
    // returns true if so
    //
    public Boolean checkCollision(Car car){
        Boolean result = false;
        Boolean collision;
        for (Car c : cars){
            if (c != car){
                collision = checkBounds(c, car);
                if (collision) result = true;
            }
        }
        return result;
    }

    // Check the passed in ped to see if it has collided
    // with any of the other cars in the crosswalk and
    // returns true if so
    //
    public synchronized Boolean checkCollision(Pedestrian p){
        Boolean result = false;
        Boolean collision;
        synchronized (Controller.simLock) {
            for (Car c : cars){
                collision = checkBounds(c, p);
                if (collision) result = true;
            }
        }

        return result;

    }

    // Takes in two cars and returns true if they are withing
    // 5 x and y of each other
    //
    private Boolean checkBounds(Car c1, Car c2){
        double yDif = Math.abs(c1.getCarY() - c2.getCarY());
        double xDif = Math.abs(c1.getCarX() - c2.getCarX());
        return yDif < 5 && xDif < 5;
    }

    // Takes in a car and a ped and returns true if they are withing
    // 7 x and y of each other
    //
    private Boolean checkBounds(Car c, Pedestrian p){
        double yDif = Math.abs(c.getCarY() -  p.getY());
        double xDif = Math.abs(c.getCarX() - p.getX());
        return yDif < 7 && xDif < 7;
    }


}

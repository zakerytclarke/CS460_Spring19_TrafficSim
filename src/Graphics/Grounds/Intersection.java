package Graphics.Grounds;

import Graphics.Direction;
import Graphics.Simulation;
import Graphics.Traffic.Pedestrian;
import javafx.scene.canvas.GraphicsContext;
import java.util.LinkedList;
import java.util.Random;

public class Intersection extends Ground {

    private final GraphicsContext gc;
    private double size = Simulation.size;
    private LinkedList<RoadDisplay> roads = new LinkedList<>();
    private LinkedList<Crossing> crosswalks = new LinkedList<>();


    public Intersection(GraphicsContext gc){
        this.gc = gc;
        this.setPosition(gc.getCanvas().getWidth() /2 - (size/2), gc.getCanvas().getHeight() /2 - (size/2), 1);
    }


    // Draws the main square and all it's connecting roads
    //
    public void draw(){
        gc.fillRect(x , y, size, size);

        for (RoadDisplay r : roads) {
            r.drawRoad();
       }

       for (Crossing c : crosswalks){
            c.draw();
       }
    }

    // Initialize all the corners for pedestrians and the crosswalks that connect them
    //
    private void setup(){

        double roadLength = (gc.getCanvas().getWidth() - 100) / 2;

        Corner north = new Corner(gc, roads.get(0), roadLength);
        Corner south = new Corner(gc, roads.get(1), roadLength);
        Corner east = new Corner(gc, roads.get(2), roadLength);
        Corner west = new Corner(gc, roads.get(3), roadLength);


        Crossing cNorth = new Crossing(gc, roads.get(0), north, east);
        Crossing cSouth = new Crossing(gc, roads.get(1), south, west);
        Crossing cEast = new Crossing(gc, roads.get(2), east, south);
        Crossing cWest = new Crossing(gc, roads.get(3), west, north);

        crosswalks.add(cNorth);
        crosswalks.add(cSouth);
        crosswalks.add(cEast);
        crosswalks.add(cWest);

        roads.get(0).setCrosswalk(cNorth);
        roads.get(1).setCrosswalk(cSouth);
        roads.get(2).setCrosswalk(cEast);
        roads.get(3).setCrosswalk(cSouth);

    }

    // Cycles over each road type and determines based on the
    // start lane passed in where the car could get to
    // so if in left turn lane then it has to turn, in middle lane
    // it has to go straight, and in right line it can do straight or right
    //
    public LaneDisplay getRandomDest(LaneDisplay start){

        LinkedList<RoadDisplay> possible = new LinkedList<>();
        Random r = new Random();

        if (start.side == Direction.NORTH ){
            if (start.count == 2){
                possible.add(roads.get(2)); // left lane so has to turn east
            } else if (start.count == 0){
                possible.add(roads.get(1)); // right lane so it has two options
                //possible.add(roads.get(3)); // uncomment for right turns
            } else {
                possible.add(roads.get(1)); // mid lane so must head south
            }
        } else if (start.side == Direction.EAST){

            if (start.count == 2){
                possible.add(roads.get(1));
            } else if (start.count == 0){
                //possible.add(roads.get(0)); // uncomment for right turns
                possible.add(roads.get(3));
            } else {
                possible.add(roads.get(3));
            }
        } else if (start.side == Direction.SOUTH){
            if (start.count == 4 ){
                //possible.add(roads.get(2)); // uncomment for right turns
                possible.add(roads.get(0));
            } else if (start.count == 2){
                possible.add(roads.get(3));
            } else {
                possible.add(roads.get(0));
            }
        } else if (start.side == Direction.WEST) {
            if (start.count == 2) {
                possible.add(roads.get(0));
            } else if (start.count == 3){
                possible.add(roads.get(2));
            } else {
                possible.add(roads.get(2));
                //possible.add(roads.get(1)); // uncomment for right turns
            }
        }

        // Get random road from possible destinations
        int range = (possible.size() - 1) + 1;
        int randRoad = r.nextInt(range);
        RoadDisplay road = possible.get(randRoad);

        return road.getRandomDest(start);
    }

    // Gives the main intersection reference to it's connecting roads
    //
    public void connectRoads(LinkedList<RoadDisplay> roads){
        this.roads = roads;
        setup();
    }

    public LinkedList<RoadDisplay> getRoads(){
        return roads;
    }

    // Creates a pedestrian at random corner with random destination
    //
    public Pedestrian createPed(double speed){

        Random rn = new Random();
        int range = (crosswalks.size() - 1) + 1;
        int random =  rn.nextInt(range);
        //random = 0; // uncomment for specific start corner

        Crossing c = crosswalks.get(random);
        Crossing dest = getPedDest(c);
        //Crossing dest = crosswalks.get(3); // uncomment for specific dest crosswalk

        return crosswalks.get(random).spawn(dest, speed);
    }

    // Gets a random ped destination that's possible from it's starting corner
    //
    private Crossing getPedDest(Crossing c){
        LinkedList<Crossing> possibilities = new LinkedList<>();
        if (c.side == Direction.NORTH){
            possibilities.add(crosswalks.get(0));
            possibilities.add(crosswalks.get(3));
        } else if (c.side == Direction.SOUTH){
            possibilities.add(crosswalks.get(1));
            possibilities.add(crosswalks.get(2));
        } else if (c.side == Direction.EAST){
            possibilities.add(crosswalks.get(0));
            possibilities.add(crosswalks.get(2));
        } else if (c.side == Direction.WEST){
            possibilities.add(crosswalks.get(1));
            possibilities.add(crosswalks.get(3));
        }

        Random rn = new Random();

        // Get random road from possible destinations
        int range = (possibilities.size() - 1) + 1;
        int rand = rn.nextInt(range);
        //int rand = 0;
        Crossing dst = possibilities.get(rand);

        return dst;

    }
}

package Graphics.Grounds;

import Graphics.Direction;
import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;
import java.util.Random;

public class RoadDisplay extends Ground {

    // RoadDisplay is currently set up to add 5 lanes to each road, 3 are ingoing,
    // 2 are outgoing. It takes in the size of the intersection, the side its
    // on, and it's neighbor which is always the Intersection object

    private final GraphicsContext gc;
    private final double size; // main intersection square size to fit the lanes around

    private final LinkedList<LaneDisplay> lanes = new LinkedList<>(); // all the lanes within this road
    private final Random rn = new Random();

    public RoadDisplay(GraphicsContext gc, Direction side, double size, Ground neighbor) {
        this.gc = gc;
        this.side = side;
        this.size = size;
        this.setIntersection(neighbor);
        setLanes(neighbor); // Set up the amount of lanes within each road
    }

    // returns north, east, south, or west depending on this road's side
    //
    public Direction getSide(){
        return side;
    }

    public void setCrosswalk(Crossing c){
        for (LaneDisplay l : lanes){
            l.setCrosswalk(c);
        }
    }

    // Gets a random start lane from three two ingoing lanes
    //
    public LaneDisplay getRandomStart() {
        int range = (3 - 1) + 1; // 0, 1, or 2
        int randomStart =  rn.nextInt(range);

        if (side == Direction.SOUTH || side == Direction.WEST) {
            randomStart += 2;
        }

        //randomStart = 4; // uncomment this line to start on specific lane index
        return lanes.get(randomStart);
    }

    // Gets a random destination from the two leaving lanes
    //
    public LaneDisplay getRandomDest(LaneDisplay start) {
        // check if car is going straight then it should stay in the same lane
        if ((start.isVert && (side == Direction.SOUTH || side == Direction.NORTH) ) || (!start.isVert && (side == Direction.WEST || side == Direction.EAST))){
            return lanes.get(start.count);
        }

        int range = (2 - 1) + 1; // 0 or 1
        int randDest =  rn.nextInt(range);

        if (side == Direction.EAST || side == Direction.NORTH) {
            randDest += 3;
        }

        return lanes.get(randDest);
    }


    // Draws the road by giving each lane an x,y coordinate
    // and then telling them to draw themselves
    //
    public void drawRoad() {

        double screenWidth = gc.getCanvas().getWidth();

        double laneLength = (screenWidth - size) / 2; // just randomly came up with this width to take up screen
        double laneWidth = size / lanes.size(); // size / num lanes

        double x = screenWidth / 2 - (size/2);
        double y = x;

        if (side == Direction.NORTH ){
            y -= laneLength;
        } else if (side == Direction.SOUTH) {
            y += size;
        } else if (side == Direction.EAST) {
            x += size;
        } else if (side == Direction.WEST) {
            x -= laneLength;
        }

        this.x = x;
        this.y = y;

        for (LaneDisplay l : lanes) {
            l.setPosition(x, y, 0);
            l.drawLane(x, y, laneWidth);
        }
    }


    // Create all the lanes that are apart of this road
    //
    private void setLanes(Ground neighbor){
        Boolean isVert = (side == Direction.NORTH || side == Direction.SOUTH);

        LaneDisplay l1 = new LaneDisplay(gc, isVert, 0, side);
        LaneDisplay l2 = new LaneDisplay(gc, isVert, 1, side);
        LaneDisplay l3 = new LaneDisplay(gc, isVert, 2, side);
        LaneDisplay l4 = new LaneDisplay(gc, isVert, 3, side);
        LaneDisplay l5 = new LaneDisplay(gc, isVert, 4, side);

        l1.setIntersection(neighbor); // sets all the lanes neighbor as the Intersection
        l2.setIntersection(neighbor);
        l3.setIntersection(neighbor);
        l4.setIntersection(neighbor);
        l5.setIntersection(neighbor);

        lanes.add(l1);
        lanes.add(l2);
        lanes.add(l3);
        lanes.add(l4);
        lanes.add(l5);
    }
}

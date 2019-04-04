package Graphics.Grounds;
import Graphics.Direction;
import Graphics.Traffic.Pedestrian;
import Primary.Lights;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;


public class Crossing extends Ground {

    private GraphicsContext gc;
    private RoadDisplay road; // Reference to the road the crosswalks over
    private double roadLength;
    private double crosswalkDepth = 15;

    private Corner start;
    private Corner dest;
    private Lights signal;

    public Crossing(GraphicsContext gc, RoadDisplay road, Corner start, Corner dest){
        this.gc = gc;
        this.road = road;
        this.side = road.side;
        this.roadLength = (gc.getCanvas().getWidth() - 100) / 2;
        this.start = start;
        this.dest = dest;
        setSignal();
    }

    // Used py ped to check if it can cross the road
    //
    public Lights getSignal(){
        return signal;
    }


    // Set up reference to the signal the crosswalk needs
    // to check to see if peds can use it
    //
    private void setSignal(){
        if (side == Direction.NORTH){
            signal = Lights.NORTH;
        } else if (side == Direction.SOUTH){
            signal = Lights.SOUTH;
        } else if (side == Direction.EAST){
            signal = Lights.EAST;
        } else if (side == Direction.WEST){
            signal = Lights.WEST;
        }

        start.setSignal(signal);
        dest.setSignal(signal);
    }

    // Returns a pedestrian spawned from the start Corner
    //
    public Pedestrian spawn(Ground dst, double speed){
        return start.spawnPed(dst, speed);
    }



    // Used by pedestrians to check which Corner is the destination
    // depending on the direction it's going
    //
    public Corner getDest(Direction dir){
        switch (side) {
            case NORTH:
                if (dir == Direction.EAST) return dest; else return start;
            case WEST:
                if (dir == Direction.SOUTH) return start; else return dest;
            case EAST:
                if (dir == Direction.NORTH) return start; else return dest;
            case SOUTH:
                if (dir == Direction.EAST) return start; else return dest;
        }
        return dest;
    }



    // Draws the crosswalk
    //
    public void draw(){
        gc.setFill(Paint.valueOf("#33334d"));

        switch (road.side){
            case NORTH:
                this.x = road.x;
                this.y = road.y + roadLength - crosswalkDepth;
                gc.fillRect(road.x, road.y + roadLength - crosswalkDepth, 100, crosswalkDepth);
                break;
            case SOUTH:
                this.x = road.x;
                this.y = road.y + 4;
                gc.fillRect(road.x, road.y + 4, 100, crosswalkDepth);
            case EAST:
                this.x = road.x + 4;
                this.y = road.y;
                gc.fillRect(road.x + 4, road.y, crosswalkDepth, 100);
                break;
            case WEST:
                this.x = road.x + roadLength - crosswalkDepth;
                this.y = road.y;
                gc.fillRect(road.x + roadLength - crosswalkDepth, road.y, crosswalkDepth, 100);
                break;
        }

        drawDashes();
        start.draw();

    }

    // Draws the crosswalk dashes
    //
    private void drawDashes(){
        gc.setFill(Paint.valueOf("#ffffff"));
        for (int i = 0; i < 10; i ++) {
            if (road.side == Direction.NORTH){
                gc.fillRect((road.x + (10 * i) + 4), road.y + roadLength - 15, 3, 15);
            } else if(road.side == Direction.SOUTH){
                gc.fillRect((road.x + (10 * i) + 4), road.y + 4, 3, 15);
            }else if (road.side == Direction.EAST){
                gc.fillRect(road.x + 4, (road.y + (10 * i) + 4), 15, 3);
            } else if(road.side == Direction.WEST){
                gc.fillRect(road.x + roadLength - 15, (road.y + (10 * i) + 4) , 15, 3);
            }
        }
    }
}

package Graphics.Grounds;
import Graphics.Direction;
import Graphics.Simulation;
import Graphics.Traffic.Pedestrian;
import Primary.Lights;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Corner extends Ground{

    private RoadDisplay road;
    private GraphicsContext gc;
    private double roadLength;
    private int size = 40;
    private int crossDepth = 15;

    private Lights signalNS; // crosswalk signal for north south traffic
    private Lights signalEW; // crosswalk signal for east west traffic

    public Corner(GraphicsContext gc, RoadDisplay road, double roadLength){
        this.gc = gc;
        this.road = road;
        this.side = road.side; // if north that's top left box, east is top right, south is bottom right, etc.
        this.roadLength = roadLength;
    }


    // returns a pedestrian spawned at this corner with a crosswalk destination
    //
    public Pedestrian spawnPed(Ground dst, double speed){
        return new Pedestrian(gc, this, dst, speed);
    }

    // Tells the Lights enum signal reference that there is a ped
    // waiting on the corner depending on the ped's direction
    //
    public void setPedOnSensor(Boolean on, Direction dir){

        switch (side) {
            case NORTH:
                if (dir == Direction.EAST) signalEW.setPedestrianAt(on); else signalNS.setPedestrianAt(on);
                break;
            case EAST:
                if (dir == Direction.SOUTH) signalNS.setPedestrianAt(on); else signalEW.setPedestrianAt(on);
                break;
            case SOUTH:
                if (dir == Direction.WEST) signalEW.setPedestrianAt(on); else signalNS.setPedestrianAt(on);
                break;
            case WEST:
                if (dir == Direction.NORTH) signalNS.setPedestrianAt(on); else signalEW.setPedestrianAt(on);
                break;
        }
    }

    // Give corner reference to the TC ped light it should be checking for changes
    //
    public void setSignal(Lights signal){
        switch (side){
            case NORTH:
                if (signal == Lights.NORTH) signalEW = signal;
                else signalNS = signal;
                break;
            case SOUTH:
                if (signal == Lights.SOUTH) signalEW = signal; else signalNS = signal;
                break;
            case EAST:
                if (signal == Lights.EAST) signalNS = signal; else signalEW = signal;
                break;
            case WEST:
                if (signal == Lights.WEST) signalNS = signal; else signalEW = signal;
                break;
        }
    }

    // Draw the corner and the signals it has
    //
    public void draw(){
        gc.setFill(Paint.valueOf("#d9d9d9"));
        x = road.x;
        y = road.y;

        if (road.side == Direction.NORTH){
            y += roadLength - size;
            x -= size;
        } else if (road.side == Direction.EAST){
            y -= size;
        } else if (road.side == Direction.SOUTH) {
            x += Simulation.size; // size of intersection square
        } else if (road.side == Direction.WEST){
            x += roadLength - size;
            y += Simulation.size;
        }

        gc.fillRect(x, y, size, size);

        if (signalEW != null && signalNS != null){
            drawSignal(true);
            drawSignal(false);
        }

    }

    // Draw corner light signals for crosswalks
    //
    private void drawSignal(Boolean first){
        double sigX = x;
        double sigY = y;

        Paint nsColor;
        Paint ewColor;

        if (signalNS.getGreen()){
            nsColor = Paint.valueOf("#009900");
        } else {
            nsColor = Paint.valueOf("#990000");
        }

        if (signalEW.getGreen()){
            ewColor = Paint.valueOf("#009900");
        } else {
            ewColor = Paint.valueOf("#990000");
        }

        if (first){
            // always drawing the east west signal
            gc.setFill(ewColor);
            if (side == Direction.NORTH || side == Direction.WEST) sigX += size - 5;
            if (side == Direction.WEST || side == Direction.SOUTH) sigY += size - crossDepth;
            gc.fillRect(sigX, sigY, 5, crossDepth);
        } else {
            // always drawing the north south ped signal
            gc.setFill(nsColor);
            if (side == Direction.NORTH) sigY += size - 5;
            if (side == Direction.EAST) sigY += size - 5;
            if (side == Direction.EAST || side == Direction.SOUTH) sigX += size - crossDepth;
            gc.fillRect(sigX, sigY, crossDepth, 5);
        }

    }
}

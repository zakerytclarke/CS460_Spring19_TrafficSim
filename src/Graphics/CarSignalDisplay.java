package Graphics;

import Primary.SignalColor;

public class CarSignalDisplay {

    private SignalColor color;  // just holds an x, y, and color
    public double x;           // to draw light on gui depending
    public double y;           // on enum value

    public CarSignalDisplay(){}

    // Used by cars to check if red
    //
    public SignalColor getColor(){
        return color;
    }

    // Each light is given a position based off the laneDisplay it's for
    //
    public void setPosition(double x, double y, double offset, Boolean isVert){
        if (isVert) {
            this.x = x + offset;
            this.y = y;
        } else {
            this.x = x;
            this.y = y + offset;
        }
    }

    public void changeColor(SignalColor color)
    {
        this.color = color;
    }
}

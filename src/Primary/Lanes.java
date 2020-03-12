package Primary;

public enum Lanes {
    N1 (false, false, SignalColor.RED),
    N2 (false, false, SignalColor.RED),
    N3 (false, false, SignalColor.RED),
    E1 (false, false, SignalColor.RED),
    E2 (false, false, SignalColor.RED),
    E3 (false, false, SignalColor.RED),
    S1 (false, false, SignalColor.RED),
    S2 (false, false, SignalColor.RED),
    S3 (false, false, SignalColor.RED),
    W1 (false, false, SignalColor.RED),
    W2 (false, false, SignalColor.RED),
    W3 (false, false, SignalColor.RED);

    private boolean carOnLane;
    private boolean emergencyOnLane;
    private SignalColor color;

    Lanes(boolean carOnLane, boolean emergencyOnLane, SignalColor color) {
        this.carOnLane = carOnLane;
        this.emergencyOnLane = emergencyOnLane;
        this.color = color;
    }

    public boolean isCarOnLane(){
        return carOnLane;
    }

    public boolean getEmergencyOnLane(){
        return emergencyOnLane;
    }

    public void setCarOnLane(boolean carOnLane){
        this.carOnLane = carOnLane;
    }

    public void setEmergencyOnLane(boolean emergencyOnLane){
        this.emergencyOnLane = emergencyOnLane;
    }

    public void setColor(SignalColor color){
        this.color = color;
        //System.out.println("Changed color of " + this + " to " + color);
    }

    public SignalColor getSignal(){
        return color;
    }

}

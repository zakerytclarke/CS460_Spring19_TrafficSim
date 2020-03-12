package Primary;

public enum Lights {
    NORTH (0, false, false),
    EAST (1, false, false),
    SOUTH (2, false, false),
    WEST (3, false, false);

    private final int number;
    private boolean pedestrianAt;
    private Boolean isGreen;

    Lights(int i, boolean pedestrianAt, Boolean isGreen) {
        this.number = i;
        this.pedestrianAt = pedestrianAt;
        this.isGreen = isGreen;
    }

    public void setPedestrianAt(boolean pedestrianAt){
        this.pedestrianAt = pedestrianAt;
    }

    public void setColor(SignalColor c){
        if (c == SignalColor.GREEN){
            isGreen = true;
        }else {
            isGreen = false;
        }
    }

    public Boolean getGreen(){
        return isGreen;
    }

    public boolean isPedestrianAt() {
        return pedestrianAt;
    }
}

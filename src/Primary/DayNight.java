package Primary;

public enum DayNight {
    DAY(true);

    private boolean day;

    DayNight(boolean day){
        this.day = day;
    }

    public void setDay(boolean day){
        this.day = day;
    }

    public boolean getDay(){
        return this.day;
    }
}


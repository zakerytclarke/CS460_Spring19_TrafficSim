package Primary;

public class IntervalStateController {
    private DayNight dayNightMode = DayNight.DAY;//DayNight Sensor
    public void IntervalStateController(){

    }
    public boolean isDay(){
        return dayNightMode.getDay();
    }
}

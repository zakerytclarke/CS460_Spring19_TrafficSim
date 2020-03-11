package Primary;

/**
 * Class: IntervalStateController
 * IntervalStateController will control the interval of the intersection
 */
public class IntervalStateController {
    private DayNight dayNightMode = DayNight.DAY;//DayNight Sensor
    public void IntervalStateController(){

    }

    /**
     * Method: isDay
     * returns the time of day
     * @return if it is day time
     */
    boolean isDay(){
        return dayNightMode.getDay();
    }
}

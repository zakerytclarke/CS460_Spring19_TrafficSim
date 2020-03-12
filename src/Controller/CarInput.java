/**
 * CarInput has one method: getStatus() which returns an array of 12 integers.
 * The integers are be either 1 or 0, if there is or isn’t (respectively) a car on the lane.
 * When getStatus is called, Car Input searches through all the Lane enums and checks if
 * there is a car on that Lane and set the array values accordingly.
 */
package Controller;

import Primary.Lanes;

public class CarInput {

    private int length = Lanes.values().length;

    /**
     * Searches through all lane enums and obtains whether a car is on
     * a lane, no need to reset values because states are not stored.
     *
     * @return
     */
    public int[] getStatus() {
        // Init array was created based on length of the Lanes of enum.
        int[] result = initArray();
        // Used to keep track of array, assuming enums are static so we never sfault.
        int count = 0;
        // Goes over all enum values and sets an integer based on boolean value.
        for (Lanes lane : Lanes.values()) {
            result[count] = lane.isCarOnLane() ? 1 : 0;
            count++;
        }
        return result;
    }

    /**
     * Initializes array with zeros.
     */
    private int[] initArray(){
        return new int[Lanes.values().length];
    }

}
/**
 * PedestrianInput has one method: getStatus() which returns an array of 4 integers.
 * The integers are be either 1 or 0, if there is or isn’t (respectively) a pedestrian at that corner.
 * When getStatus is called, Car Input searches through all the pedestrian enums to check if
 * there is a ped and set the array value accordingly.
 */
package Controller;
import Primary.Lights;
public class PedestrianInput {
    private int length = Lights.values().length;
    public int[] getStatus(){
        int[] result = initArray();
        int count = 0;
        //iterate and set whether pedestrian is in one of four corners of intersection
        for (Lights light : Lights.values()) {
            result[count] = light.isPedestrianAt() ? 1 : 0;
            count++;
        }
        return result;
    }
    private int[] initArray(){
        return new int[Lights.values().length];
    }
}

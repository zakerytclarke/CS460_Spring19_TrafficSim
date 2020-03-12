# TrafficSimulator

## All credit for this simulator goes to Andrew Morin, Jacob Hurst, Beau Kujath, and Alex Schmidt-Gonzales

### Sensors

There are sensors on each incoming lane to detect cars waiting and "button" sensors for each crosswalk to detect pedestrians waiting. The sensors for each will set a boolean value to true for each lane/crosswalk as soon as the ped/car comes to a stop waiting at the intersection/corner. As soon as the crosswalk or lane signal is set to green the boolean value will be reset to false.

### Signals

Each of the 12 lanes is referenced as one of the Lanes enum values, and each of the four crosswalks is referenced as one of the Lights enum values as shown here:

### Project Details
* The simulation was created with IntelliJ and the Java 10 JDK so it might throw warnings if using another JDK but should still work
* Uncomment the two lines that include the collision variable in the Controller class if you don't care about detecting collision that will reset the sim
* Change or add to the randomly selected car colors from within Graphics/Traffic/Car class 
* If you wanted to test with cars being able to make right turns uncomment the four lines within the getRandomDest function in Graphics/Grounds/Intersection, but cars will not wait for pedestrians to move before turning if added

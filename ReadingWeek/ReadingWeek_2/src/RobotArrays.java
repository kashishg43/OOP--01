public class RobotArrays {
    public static void main (String[] args) {
        Robot[] robotsA = new Robot[3]; //instantiate array of references to 3 Robot objects
        System.out.println(robotsA[0]); //at start, array locations carry null
        robotsA[0] = new Robot("C3PO"); //initialise entry at index 0
        robotsA[1] = new Robot("C4PO"); //initialise entry at index 1
//initialise with same reference as index 0
//neat initialisation syntax using {...}
        robotsA[2] = robotsA[0];
        Robot[] robotsB = {
                new Robot("C5PO"),
                robotsA[0],
                robotsA[1]
        };
        System.out.println(robotsB.length); //print size of array robotsB
        for (Robot robot : robotsB)
            System.out.println(robot.name);
//loop through entries, assign current to robot
//print name of current element
    }
}

package codelagos.classdemo;

public class SimpleCar {
    // Number of tyres of car
    private int numType;
    private int numSit;

    public boolean isEngineOn() {
        return engineOn;
    }

    private boolean engineOn;

    public SimpleCar(int numType, int numSit) {

        this.numType = numType;
        this.numSit = numSit;
    }

    public void stateEngine() {
        // Start the car's engine
        engineOn = true;
        System.out.println("Stating Engine");
    }

    public void stopEngine() {
        System.out.println("Stopping Engine");
        engineOn = false;
    }

    public void move() {
        if (isEngineOn()) {
            System.out.println("Moving Car");
        }else {
            System.out.println("Car cannot due to the current state of the Engine");
        }
    }

    public void drive() {
        stateEngine();
        move();
    }

    public void stop() {
        System.out.println("Stopping Car");
        stopEngine();
    }

}

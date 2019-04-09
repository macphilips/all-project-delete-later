package codelagos.classdemo;

/*
* Working with variable and data types. But we are separating the class definition of SimpleCar into a
* separate file and it is there referenced with is this Java class
* */
public class SimpleCarDemo {
    public static void main(String[] args) {
        // Let Drive a Simple car
        SimpleCar car = new SimpleCar(4, 5);
        car.drive();
        car.stop();
    }
}

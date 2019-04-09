package codelagos.classdemo;

public class SizeOfDataTypes {
    public static void main(String[] args) {
        int int_max = Integer.MAX_VALUE;
        int int_min = Integer.MIN_VALUE;
        float float_max = Float.MAX_VALUE;
        float float_min = Float.MIN_VALUE;
        double double_max = Double.MAX_VALUE;
        double double_min = Double.MIN_VALUE;
        long long_max = Long.MAX_VALUE;
        long long_min = Long.MIN_VALUE;


        System.out.println("Min value a int can hold is => " + int_min);
        System.out.println("Max value a int can hold is => " + int_max);

        System.out.println("Min value a long can hold is => " + long_min);
        System.out.println("Max value a long can hold is => " + long_max);

        System.out.println("Min value a float can hold is => " + float_min);
        System.out.println("Max value a float can hold is => " + float_max);

        System.out.println("Min value a double can hold is => " + double_min);
        System.out.println("Max value a double can hold is => " + double_max);
 
    }
}

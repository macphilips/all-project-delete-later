package codelagos.operationdemo;

class UnaryDemo {

    public static void main(String[] args) {

        int result = +1;
        // result is now 1
        System.out.println(result);

        result--;
        // result is now 0
        System.out.println(result);

        result++;
        // result is now 1
        System.out.println(result);

        result = -result; // This statement is the same thing as saying -1 * result. That's why -1 is being printed out.
        // result is now -1
        System.out.println(result);
        //Another Example
        int two = 2;
        two = -two;
        System.out.println(two);

        boolean success = false;
        // false
        System.out.println(success);
        // true ! can also been used to negate a boolean value from true to false and vice versa.
        System.out.println(!success);
    }
}

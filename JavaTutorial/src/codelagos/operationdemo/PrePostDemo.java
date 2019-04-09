package codelagos.operationdemo;

class PrePostDemo {
    public static void main(String[] args){
        int i = 3;
        i++;
        // prints 4
        System.out.println(i);
        ++i;
        // prints 5
        System.out.println(i);
        // prints 6.  ++i means get and increment the value and then it uses the value that why 6 is printed out.
        System.out.println(++i);
        // prints 6. i++ mean used the value first that why 6 is being printed out and then increment the value.
        // And is why on the next System.out.println prints out 7
        System.out.println(i++);
        // prints 7
        System.out.println(i);
    }
}

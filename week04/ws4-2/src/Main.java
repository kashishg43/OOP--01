import java.io.*;
import java.util.*;

public class Main {
    public static <T> List<T> reverse(ArrayList<T> l) {
        return (Collections.reverse(l));

    }
    public static void main(String[] args) {
        ArrayList<Integer> number = new ArrayList<>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        System.out.println(reverse(number));
    }
}

//TASK 3

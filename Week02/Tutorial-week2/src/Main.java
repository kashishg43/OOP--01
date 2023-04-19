import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Peak finding (outputs the index of a peak)");

        Integer[] xs = {3,9,10,14,15,7,2};
        System.out.print("xs: ");
        System.out.println(Arrays.toString(xs));

        System.out.print("findPeak: ");     System.out.println(findPeak(xs));
        System.out.print("findPeakFast: "); System.out.println(findPeakFast(xs));

        Integer[] ys = {3,7,22,47,36,33,31,30,25,21,20,15,7,4,10,22};
        System.out.print("ys: ");
        System.out.println(Arrays.toString(ys));

        System.out.print("findPeak: ");     System.out.println(findPeak(ys));
        System.out.print("findPeakFast: "); System.out.println(findPeakFast(ys));
    }

    public static int findPeak(Integer [] A) {
        int n  = A.length;
        if (A[0] >= A[1]) {
                return 0;
        }
        if (A[n-1] >= A[n-2]) {
            return n-1;
        }
        for (int i = 1; i < n; i++) {
            if (A[i] >= A[i-1] && A[i] >= A[i+1]) {
                    return i;
            }
        }
        return -1;
    }

    public static int findPeakFast(Integer[] A) {
        int n = A.length;

        if (n == 1) {
            return 0;
        }
        else if (n == 2) {
            if (A[0] >= A[1]) {
                return 0;
            } else {
                return 1;
            }
        }
        else {
            if ((A[n / 2] >= A[(n / 2) - 1]) && (A[n / 2] >= A[(n / 2) + 1])) {
                return n/2;
            } else if (A[(n - 2) - 1] >= A[n / 2]) {
                return findPeakFast(Arrays.copyOfRange(A, 0, (n / 2) - 1));
            } else {
                return findPeakFast(Arrays.copyOfRange(A, (n / 2) + 1,n));
            }
        }
    }
}
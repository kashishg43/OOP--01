public class ExceptionalCalculator {
    public static void main (String[] args) {
        Adder adder = new Adder();
        try {
            for (String arg : args) {
                adder.add(Integer.parseInt(arg));
            }
            System.out.println("Sum:" + adder.sum);
            throw (new Exception("The exception was that an integer is not provided"));
        } catch (Exception e) {
            System.out.println("Exception Description: " + e.getMessage());
        }
}}

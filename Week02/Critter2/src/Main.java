public class Main {
    public static void main(String[] args) {
        Critter Cow = new Critter ("Cow");
        Cow.poke();

        Critter Bug = new Critter ("Bug");

        Cow.eat(Bug);

    }
}
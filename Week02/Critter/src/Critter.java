public class Critter {
    String name;

    Critter (String name) {
        this.name = name;
    }

    public void poke(String [] args) {
        System.out.println(name + "was poked");
    }

    public void eat(Critter c) {
        System.out.println(name+"ate"+c.name);
    }
}

package sheet2;

public class Chocolate extends Food {

    @Override
    public String eaten(Animal animal) {return "animal eats chocolate";}
    public String eaten(Dog dog) {return "dog eats chocolate";}
    public String eaten(Cat cat) {return "cat eats chocolate";}

}

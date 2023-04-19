package sheet2;

public class Fruit extends Food {

    @Override
    public String eaten(Animal animal) {return "animal eats fruit";}
    public String eaten(Dog dog) {return "dog eats fruit";}
    public String eaten(Cat cat) {return "cat eats fruit";}



}

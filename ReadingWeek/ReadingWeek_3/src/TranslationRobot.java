public class TranslationRobot extends Robot {
    // class has everything that Robot has implicitly
    String substitute; //and more features

    TranslationRobot(String name, String substitute) {
        super (name);
        this.substitute = substitute;
    }
    void translate(String phrase) { //added method
        this.talk(phrase.replaceAll("a", substitute));
    }
    @Override
    void charge(float amount) { //overriding
        System.out.println(name + " charges double.");
        powerLevel = powerLevel + 2 * amount;
    } }

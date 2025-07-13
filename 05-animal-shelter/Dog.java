public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public String getAnimalType() {
        return "Dog";
    }
}
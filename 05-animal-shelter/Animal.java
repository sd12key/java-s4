public abstract class Animal {
    private final String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public abstract String getAnimalType();
    
    @Override
    public String toString() {
        return this.name + " (" + this.getAnimalType() + ")";
    }

}

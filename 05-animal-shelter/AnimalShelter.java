public class AnimalShelter {
    private final Queue<Animal> animals = new Queue<>();
    
    public void enqueue(Animal animal) {
        if (animal == null) return;
        animals.enqueue(animal);
    }
    
    public Animal dequeueAny() {
        return animals.dequeue();
    }
    
    public Dog dequeueDog() {
        return dequeueSpecific(Dog.class);
    }
    
    public Cat dequeueCat() {
        return dequeueSpecific(Cat.class);
    }
    
    private <T extends Animal> T dequeueSpecific(Class<T> type) {
        for (int i = 0; i < animals.size(); i++) {
            Animal current = animals.get(i);
            if (current != null && type.isInstance(current)) {
                animals.delete(i);
                return type.cast(current); 
            }
        }
        return null;
    }
    
    public boolean isEmpty() {
        return animals.isEmpty();
    }
    
    public int size() {
        return animals.size();
    }
    
     public int getDogCount() {
        return countAnimals(Dog.class);
    }
    
    public int getCatCount() {
        return countAnimals(Cat.class);
    }
    
    private <T extends Animal> int countAnimals(Class<T> type) {
        int count = 0;
        for (int i = 0; i < animals.size(); i++) {
            Animal current = animals.get(i);
            if (current != null && type.isInstance(current)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        String base = "Animal Shelter " + animals;
        return (this.size() > 0) 
            ? base + "\nTotal " + this.size() + " animals (" 
            + this.getDogCount() + " dogs, " 
            + this.getCatCount() + " cats)"
            : base;
    }

}
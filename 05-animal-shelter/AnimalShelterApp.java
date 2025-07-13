public class AnimalShelterApp {
    private static final String DOG_STR = "dog";
    private static final String CAT_STR = "cat";
    private static final String ANY_STR = "any";    

    public static void main(String[] args) {
        AnimalShelter shelter = new AnimalShelter();

        System.out.println("\n\n== Animal Shelter Simulation ==");        

        shelter.enqueue(new Dog("Buddy"));
        shelter.enqueue(new Dog("Ukka"));
        shelter.enqueue(new Cat("Whiskers"));
        shelter.enqueue(new Dog("Max"));
        shelter.enqueue(new Cat("Mittens"));

        System.out.println("\n--> Initial state:");
        System.out.println(shelter);

        Animal adoptedDog1 = shelter.dequeueDog();
        printAdoption(DOG_STR, adoptedDog1);
        System.out.println(shelter);

        Animal adoptedCat1 = shelter.dequeueCat();
        printAdoption(CAT_STR, adoptedCat1);
        System.out.println(shelter);

        Animal adoptedAny1 = shelter.dequeueAny();
        printAdoption(ANY_STR, adoptedAny1);        
        System.out.println(shelter);

        Animal adoptedDog2 = shelter.dequeueDog();
        printAdoption(DOG_STR, adoptedDog2);                
        System.out.println(shelter);

        Animal adoptedDog3 = shelter.dequeueDog();
        printAdoption(DOG_STR, adoptedDog3);                
        System.out.println(shelter);

        Animal adoptedAny2 = shelter.dequeueAny();
        printAdoption(ANY_STR, adoptedAny2);
        System.out.println(shelter);        

        Animal adoptedAny3 = shelter.dequeueAny();
        printAdoption(ANY_STR, adoptedAny3);
        System.out.println(shelter);

        System.out.println();

    }

    private static void printAdoption(String requestedType, Animal animal) {
        if (animal == null) {
            switch(requestedType.toLowerCase()) {
                case DOG_STR: 
                    System.out.println("\n--> No dogs left for adoption");
                    break;
                case CAT_STR:
                    System.out.println("\n--> No cats left for adoption");
                    break;
                default:
                    System.out.println("\n--> No animals left for adoption");
            }
        } else {
            System.out.println("\n--> Adopted: " + animal);
        }
    }    
}

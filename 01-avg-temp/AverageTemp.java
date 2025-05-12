import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class AverageTemp {

    // constant for end input
    public static final String END_INPUT = "-end";

    public static void main(String[] args) {
        System.out.println("\n*** Welcome to the Temperature Calculator ***");
        
        // crate a new array list to store temperatures
        List<Double> temperatures = new ArrayList<>();
        // create a new instance of TemperatureInputReader
        TemperatureInputReader reader = new TemperatureInputReader();

        // read temperatures
        Double temp;
        int count = 0;
        System.out.println("\nPlease enter the temperatures below (" + END_INPUT + " to finish):");

        // loop to read temperatures, until user enters -end, adding each one to the list
        while ((temp = reader.readTemperature("\n--> Value " + (count + 1) + ": ")) != null) {
            count++;
            temperatures.add(temp);
            System.out.println("Temperature " + count + " added: " + temp);
        }

        if (temperatures.size() == 0) {  
            System.out.println("\nNo temperature values were entered, goodbye!\n");
            return;
        } else {
            System.out.println("\nYou entered total of [" + count + "] temperatures.");
            System.out.println("The values are:   " + temperatures);
            System.out.println("Sorted ascending: " + TemperatureCalculator.sortTemperatures(temperatures));
        }

        // no real reason to have a try here, as the input is already validated, but do it anyway
        try {
            double mean = TemperatureCalculator.calculateMean(temperatures);
            double median = TemperatureCalculator.calculateMedian(temperatures);
            int aboveMeanCount = TemperatureCalculator.countAboveMean(temperatures, mean);

            System.out.println("\n--> Results:");
            System.out.printf("    Mean (average) temperature:  %.2f degrees\n", mean);
            System.out.printf("    Median temperature:          %.2f degrees\n", median);
            System.out.printf("    Number of values above mean: %d\n\n", aboveMeanCount);
        } catch (IllegalArgumentException e) {
            System.err.println("Input error: " + e.getMessage());
        } 

    }
}

// helper class to read temperature inputs
class TemperatureInputReader {
    private Scanner scanner;
    
    public TemperatureInputReader() {
        this.scanner = new Scanner(System.in);
    }
    
    // I need to use Double instead of double to allow null return value
    public Double readTemperature(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        
        // return null if user enters -end
        if (input.equalsIgnoreCase(AverageTemp.END_INPUT)) {
            return null;
        }
        
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number (or " + AverageTemp.END_INPUT + ")");
            // recursive call to readTemperature to allow user to re-enter
            return readTemperature(message);
        }
    }
}

// helper class to calculate average temperature and count above average
class TemperatureCalculator {
    
    private static final String ERROR_MESSAGE = "Temperature array cannot be null or empty";

    // method to calculate the average temperature
    public static double calculateMean(List<Double> temperatures) {
        
        validateTemperatureList(temperatures);
        
        double sum = 0;
        for (double temp : temperatures) {
            sum += temp;
        }
        return sum / temperatures.size();
    }

    // method to count the number of temperature entries which are above the average
    public static int countAboveMean(List<Double> temperatures, double mean) {

        validateTemperatureList(temperatures);

        int count = 0;
        for (double temp : temperatures) {
            if (temp > mean) count++;
        }
        return count;
    }

    // overload to ascending sort to be default
    public static List<Double> sortTemperatures(List<Double> temperatures) {
        return sortTemperatures(temperatures, false);
    }

    // sort with order control
    public static List<Double> sortTemperatures(List<Double> temperatures, boolean descending) {
        
        validateTemperatureList(temperatures);
        
        // copy to a new array 
        List<Double> sorted = new ArrayList<>(temperatures);

        int n = sorted.size();

        // bubble sort
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                boolean shouldSwap = descending ? 
                    // descending
                    (sorted.get(j) < sorted.get(j+1)) : 
                    // ascending
                    (sorted.get(j) > sorted.get(j+1)); 
                    
                if (shouldSwap) {
                    // swap elements
                    Double temp = sorted.get(j);
                    sorted.set(j, sorted.get(j+1));
                    sorted.set(j+1, temp);
                }
            }
        }

        return sorted;
    }

    // helper method to calculate median
    public static double calculateMedian(List<Double> temperatures) {

        validateTemperatureList(temperatures);

        List<Double> sorted = sortTemperatures(temperatures);
        
        int size = sorted.size();
        if (size % 2 == 1) {
            return sorted.get(size / 2);
        } else {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        }
    }

    // method to validate the temperature list
    private static void validateTemperatureList(List<Double> temperatures) {
        if (temperatures == null || temperatures.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

}

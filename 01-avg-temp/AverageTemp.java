import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


public class AverageTemp {

    // constant for end input
    public static final String END_INPUT = "-end";

    public static void main(String[] args) {
        System.out.println("\n*** Welcome to the Temperature Calculator ***");
        
        // crate a new list to store temperatures (user input)
        List<Double> tempList = new ArrayList<>();
        // create a new instance of TemperatureInputReader
        TemperatureInputReader reader = new TemperatureInputReader();

        // read temperatures
        Double temp;
        int count = 0;
        System.out.println("\nPlease enter the temperatures below (" + END_INPUT + " to finish):");        

        // loop to read temperatures, until user enters -end, adding each one to the list
        while ((temp = reader.readTemperature("\n--> Value " + (count + 1) + ": ")) != null) {
            count++;
            tempList.add(temp);
            System.out.println("Temperature " + count + " added: " + temp);
        }

        // validate
        if (tempList.isEmpty()) {  
            System.out.println("\nNo temperature values were entered, goodbye!\n");
            return;
        }
        
        // convert to a primitive array of doubles
        double[] temperatures = new double[tempList.size()];
        for (int i = 0; i < temperatures.length; i++) {
            temperatures[i] = tempList.get(i);
        }

        System.out.println("\nYou entered total of [" + temperatures.length + "] temperatures.");
        System.out.println("The values are:   " + Arrays.toString(temperatures));
        
        // no real reason to have a try here, as the input is already validated, but do it anyway
        try {
            double sortedTemperatures[] = TemperatureCalculator.sortTemperatures(temperatures);
            System.out.println("Sorted ascending: " + Arrays.toString(sortedTemperatures));

            double meanTemp = TemperatureCalculator.calculateMean(temperatures);
            double medianTemp = TemperatureCalculator.calculateMedian(sortedTemperatures);
            int aboveMeanCount = TemperatureCalculator.countAboveMean(temperatures, meanTemp);

            System.out.println("\n--> Results:");
            System.out.printf("    Mean (average) temperature:  %.2f degrees\n", meanTemp);
            System.out.printf("    Median temperature:          %.2f degrees\n", medianTemp);
            System.out.printf("    Number of values above mean: %d\n\n", aboveMeanCount);
        } catch (IllegalArgumentException e) {
            System.err.println("<!> Error: " + e.getMessage());
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
    public static double calculateMean(double[] temperatures) {
        
        validateTemperatureList(temperatures);
        
        double sum = 0;
        for (double temp : temperatures) {
            sum += temp;
        }
        return sum / temperatures.length;
    }

    // method to count the number of temperature entries which are above the average
    public static int countAboveMean(double[] temperatures, double mean) {

        validateTemperatureList(temperatures);

        int count = 0;
        for (double temp : temperatures) {
            if (temp > mean) count++;
        }
        return count;
    }

    // overload to ascending sort to be default
    public static double[] sortTemperatures(double[] temperatures) {
        return sortTemperatures(temperatures, false);
    }

    // sort with order control
    public static double[] sortTemperatures(double[] temperatures, boolean descending) {
        
        validateTemperatureList(temperatures);
        
        // make a copy of the array
        double[] sorted = Arrays.copyOf(temperatures, temperatures.length);

        int n = sorted.length;

        // bubble sort, proably not optimal, but simple classic sort algorithm
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                boolean shouldSwap = descending ? 
                    // descending
                    (sorted[j] < sorted[j+1]) : 
                    // ascending
                    (sorted[j] > sorted[j+1]); 
                    
                if (shouldSwap) {
                    // swap elements
                    double temp = sorted[j];
                    sorted[j] = sorted[j+1];
                    sorted[j+1] =  temp;
                }
            }
        }

        return sorted;
    }

    // helper method to calculate median, assumes the array is already sorted
    public static double calculateMedian(double[] sortedTemperatures) {

        validateTemperatureList(sortedTemperatures);
        
        int n = sortedTemperatures.length;

        if (n % 2 == 1) {
            // n odd, return the middle value
            return sortedTemperatures[n / 2];
        } else {
            // n even, average of the two middle values
            return (sortedTemperatures[n / 2 - 1] + sortedTemperatures[n / 2]) / 2.0;
        }
    }

    // method to validate the temperature list
    private static void validateTemperatureList(double[] temperatures) {
        if (temperatures == null || temperatures.length == 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

}

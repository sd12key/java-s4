import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieTheatreApp {

    private static Theatre theatre;
    private static ReservationService service;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeTheatre();
        showMainMenu();
    }

    // helper method to initialize theatre, getting user input for rows and seats
    private static void initializeTheatre() {

        final String CANCEL_MESSAGE = "\n\n<!> Theatre setup cancelled. Goodbye!\n";

        System.out.println("\n=====> Theatre Setup (-1 to cancel) <=====");
        Integer numberOfRows = getValidInput("--> Enter number of rows (1-30, enter=10): ", 1, 20, true, 10);
        if (numberOfRows == null) {
            exitApp(CANCEL_MESSAGE);
        }
        System.out.println("==> Number of rows: " + numberOfRows);
        Integer seatsPerRow = getValidInput("--> Enter seats per row (1-30, enter=20): ", 1, 30, true, 20);
        if (seatsPerRow == null) {
            exitApp(CANCEL_MESSAGE);
        }
        System.out.println("==> Number of seats per row: " + seatsPerRow);

        theatre = new Theatre(numberOfRows, seatsPerRow);
        service = new ReservationService(theatre);
        System.out.println("==> Theatre created with " + theatre.getNumberOfRows() + " rows and " + theatre.getSeatsPerRow() + " seats per row.");
    }

    // main menu method
     private static void showMainMenu() {
        while (true) {
            System.out.println("\n\n=====> Theatre Main Menu <=====\n");
            System.out.println("  (1) Reserve seat");
            System.out.println("  (2) Cancel reservation");
            System.out.println("  (3) Display availability/reservation chart");
            System.out.println("  (4) Quit");
            
            int choice = getValidInput("\n--> Enter your choice (1-4): ", 1, 4, false);
            
            switch (choice) {
                case 1:
                    reserveSeat(1);
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    displayTheatreChart();
                    break;
                case 4:
                    exitApp("\n=====> Goodbye! <=====\n");
                    return;
            }
        }
    }

    // helper method to get seat position, returning row and seat numbers
    // returns null if cancelled (that's why Integer[] instead of int[])
    private static Integer[] getSeatPosition(String message) {
        System.out.println("\n=====> " + message + " (-1 to cancel) <=====");
        
        Integer row = getValidInput("--> Enter row number (1-" + theatre.getNumberOfRows() + "): ", 1, theatre.getNumberOfRows(), true);
        if (row == null) return null;
        
        Integer seat = getValidInput("--> Enter seat number (1-" + theatre.getSeatsPerRow() + "): ", 1, theatre.getSeatsPerRow(), true);
        if (seat == null) return null;

        // confirm operation
        System.out.printf("\nEntered: Row %d, Seat %d.%n", row, seat);
        if (!confirmOperation("--> Confirm? (y/n): ")) {
            return null; 
        }
        
        return new Integer[]{row, seat};
    }

    // helper method to reserve seat
    private static void reserveSeat(int distance) {
        Integer[] seatPosition = getSeatPosition("Reserve Seat");
            if (seatPosition != null) {
                System.out.println("\n" + service.reserveSeat(seatPosition[0], seatPosition[1], distance));
            }
    }

    // helper method to cancel reservation
    private static void cancelReservation() {
        Integer[] seatPosition = getSeatPosition("Cancel Reservation");
            if (seatPosition != null) {
                System.out.println("\n" + service.cancelReservation(seatPosition[0], seatPosition[1]));
            }
    }

    // helper method to display theatre chart
    private static void displayTheatreChart() {
            System.out.println("\n=====> Theatre Seat Chart <=====\n");
            System.out.println(service.viewSeatingChart());
        }

    // helper method to exit the application
    private static void exitApp(String exitMessage) {
        System.out.println(exitMessage);
        scanner.close();
        System.exit(0);
    }

    // helper method for y/n confirmation
    private static boolean confirmOperation(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim().toUpperCase();
            
            if (input.equals("-1")) {
                return false;
            }
            if (input.equals("Y")) {
                return true;
            }
            if (input.equals("N")) {
                return false;
            }
            
            System.out.println("<!> Please enter Y/y (Yes), N/n (No), or -1 (Cancel)");
        }
    }

    // overload without default value support
    private static Integer getValidInput(String prompt, int min, int max, boolean allowCancel) {
        return getValidInput(prompt, min, max, allowCancel, null); // Delegate to new method
    }

    // get user input methid (integer with bounds, default value on enter, -1 to cancel)
    // use Integer instead of primitive int to allow null return value
    private static Integer getValidInput(String prompt, int min, int max, boolean allowCancel, Integer defaultValue) {

        final String CANCEL = "-1";

        while (true) {
           
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            // empty input = default, if default not provided, continue loop
            if (input.isEmpty() && defaultValue != null) {
                return defaultValue;
            }
            
            // handle cancellation (if allowed)
            if (allowCancel && input.equals(CANCEL)) {
                return null;
            }
            
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("<!> Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("<!> Invalid input. Please enter a number" + 
                                (allowCancel ? ", " + CANCEL +" to cancel" : "") +
                                (defaultValue != null ? ", or Enter for default (" + defaultValue + ")" : "") + ".");
            }
        }
    }
}

// ========================= CLASSES ===========================
// ===== single seat class =========
class Seat {
    private int rowNumber;
    private int seatNumber;
    private boolean isReserved;

    // constructor
    public Seat(int rowNumber, int seatNumber) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }

    // is seat reserved getter
    public boolean isReserved() {
        return this.isReserved;
    }

    // reserve setter
    public void reserve() {
        this.isReserved = true;
    }

    // cancel reserve setter
    public void cancel() {
        this.isReserved = false;
    }

    // overloaded toString method
    public String toString(boolean verbose) {
        return verbose 
            ? "Row: " + this.rowNumber + ", Seat: " + this.seatNumber + ", Reserved: " + (isReserved ? "Yes" : "No")
            : "R" + this.rowNumber + "-S" + this.seatNumber;
    }

    // toString method for seat
    @Override
    public String toString() {
        // default to verbose details
        return toString(true);
    }

}

// ======= movie theatre class ========================
// theatre class, contaiting single seats
class Theatre {
    private int numberOfRows;
    private int seatsPerRow;
    private Seat[][] seats;

    // constructor rows-x-columns
    public Theatre(int numberOfRows, int seatsPerRow) {
        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;
        this.seats = new Seat[this.numberOfRows][this.seatsPerRow];

        // initialize seats
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                this.seats[i][j] = new Seat(i + 1, j + 1);
            }
        }
    }

    // getter for number of rows
    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    // getter for number of seats per row
    public int getSeatsPerRow() {
        return this.seatsPerRow;
    }

    // reserve seat method
    public boolean reserveSeat(int rowNumber, int seatNumber) {
        if (isValidPosition(rowNumber, seatNumber)) {
            Seat seat = this.seats[rowNumber-1][seatNumber-1];
            if (!seat.isReserved()) {
                seat.reserve();
                return true;
            }
        }
        return false;
    }

    // cancel reservation method
    public boolean cancelReservation(int rowNumber, int seatNumber) {
        if (isValidPosition(rowNumber, seatNumber)) {
            Seat seat = this.seats[rowNumber-1][seatNumber-1];
            if (seat.isReserved()) {
                seat.cancel();
                return true;
            }
        }
        return false;
    }

    // overload find nearby available seats method without bounds
    public List<Seat> findNearbyAvailableSeats(int preferredRow, int preferredColumn) {
        return findNearbyAvailableSeats(preferredRow, preferredColumn, 1);
    }

    // find nearest available seats, returns list for nearby available seats
    public List<Seat> findNearbyAvailableSeats(int preferredRow, int preferredColumn, int distance) {
        List<Seat> availableSeats = new ArrayList<>();
        
        // checking 3x3 grid around preferred seat
        for (int i = preferredRow - distance; i <= preferredRow + distance; i++) {
            for (int j = preferredColumn - distance; j <= preferredColumn + distance; j++) {
                
                // skip the preferred seat itself (since it's already taken anyway)
                if (i == preferredRow && j == preferredColumn) continue;
                
                if (isValidPosition(i, j) && !seats[i-1][j-1].isReserved()) {
                    availableSeats.add(seats[i-1][j-1]);
                }
            }
        }
        
        return availableSeats;
    }

    // private mathod to ensure order of values, swap if needed
    private void ensureOrder(int[] values) {
        if (values[0] > values[1]) {
            int temp = values[0];
            values[0] = values[1];
            values[1] = temp;
        }
    }

    // overload get seating chart method without bounds
    public String getSeatingChart() {
        return getSeatingChart(1, this.numberOfRows, 1, this.seatsPerRow);
    }
    
    // get seating chart method with bounds
    public String getSeatingChart(int beginRow, int endRow, int beginSeat, int endSeat) {

        // validate and adjust bounds
        int startRow = Math.max(1, Math.min(beginRow, numberOfRows));
        int stopRow = Math.max(1, Math.min(endRow, numberOfRows));
        int startSeat = Math.max(1, Math.min(beginSeat, seatsPerRow));
        int stopSeat = Math.max(1, Math.min(endSeat, seatsPerRow));
        
        // ensure start <= stop for both rows and seats
        ensureOrder(new int[]{startRow, stopRow});
        ensureOrder(new int[]{startSeat, stopSeat});

        final String CHART_OFFSET = "    ";
        
        String chart = "";
                
        // print top seat numbers (column headers)
        chart += CHART_OFFSET + " ";
        for (int j = startSeat; j <= stopSeat; j++) {
            chart += String.format(" %-2d", j);
        }
        chart += "\n";
        
        // top border
        chart += CHART_OFFSET + "-" + "---".repeat(stopSeat - startSeat + 1) + "-\n";
        
        // print each row with row numbers on both sides (from top to bottom)
        for (int i = stopRow; i >= startRow; i--) {
            // Left row number
            chart += String.format("%2d | ", i);
            
            // seat status
            for (int j = startSeat; j <= stopSeat; j++) {
                chart += seats[i - 1][j - 1].isReserved() ? "(X)" : " O ";
            }
            
            // right row number
            String format = numberOfRows < 10 ? "%1d" : "%2d";
            chart += String.format(" | " + format, i);
            chart += "\n";
        }
        
        // bottom border
        chart += CHART_OFFSET + "-" + "---".repeat(stopSeat - startSeat + 1) + "-\n";
        
        // print bottom seat numbers (column footers)
        chart += CHART_OFFSET + " ";
        for (int j = startSeat; j <= stopSeat; j++) {
            chart += String.format(" %-2d", j);
        }        
        
        return chart;
    }

    // valid position checker, helper method
    public boolean isValidPosition(int rowNumber, int seatNumber) {
        return rowNumber > 0 && rowNumber <= this.numberOfRows && seatNumber > 0 && seatNumber <= this.seatsPerRow;
    }
 
}

// =========== reservation service class ==============================
class ReservationService {
    private Theatre theatre;

    public ReservationService(Theatre theatre) {
        this.theatre = theatre;
    }

    // overload reserve seats method without distance
    public String reserveSeat(int rowNumber, int seatNumber) {
        return reserveSeat(rowNumber, seatNumber, 1);
    }

    // reserve seats method with distance
    public String reserveSeat(int rowNumber, int seatNumber, int distance) {

        final String SEAT_POSITION_FORMAT = "Seat %d in row %d";
        final String MESSAGE_PREFIX = "\n--> ";

        if (!theatre.isValidPosition(rowNumber, seatNumber)) {
            return "Invalid seat position.";
        }

        if (theatre.reserveSeat(rowNumber, seatNumber)) {
            return String.format(MESSAGE_PREFIX + SEAT_POSITION_FORMAT + " has been successfully reserved!", seatNumber, rowNumber);
        } else {
            List<Seat> availableSeats = theatre.findNearbyAvailableSeats(rowNumber, seatNumber, distance);
            
            if (availableSeats.isEmpty()) {
                return String.format(MESSAGE_PREFIX + SEAT_POSITION_FORMAT + " is taken, and no available seats nearby.", seatNumber, rowNumber);
            } else {

                String result = String.format(MESSAGE_PREFIX + SEAT_POSITION_FORMAT + " is taken.\n\nAvailable nearby seats: ", seatNumber, rowNumber);

                int i = 0;
                for (Seat seat : availableSeats) {
                    result += seat.toString(false);
                    if (i < availableSeats.size() - 1) {
                        result += ", ";
                    }
                    i++;
                }

                result += "\n\n" 
                    + theatre.getSeatingChart(rowNumber - distance, rowNumber + distance, seatNumber - distance, seatNumber + distance) 
                    + "\n";

                return result;
            }
        }
    }

    // cancel reservation method
    public String cancelReservation(int rowNumber, int seatNumber) {

        if (!theatre.isValidPosition(rowNumber, seatNumber)) {
            return "<!> Invalid seat position.";
        }

        if (theatre.cancelReservation(rowNumber, seatNumber)) {
            return "--> Reservation cancelled successfully!";
        } else {
            return "<!> No reservation found at this seat.";
        }
    }

    // view seating chart method
    public String viewSeatingChart() {
        return theatre.getSeatingChart();
    }
}
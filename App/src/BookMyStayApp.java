import java.util.*;

/**
 * CLASS - InvalidBookingException
 */
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * CLASS - Reservation
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * CLASS - RoomInventory
 */
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public boolean isValidRoomType(String type) {
        return rooms.containsKey(type);
    }

    public boolean isAvailable(String type) {
        return rooms.getOrDefault(type, 0) > 0;
    }
}

/**
 * CLASS - BookingRequestQueue
 */
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }
}

/**
 * CLASS - ReservationValidator
 */
class ReservationValidator {

    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Check availability
        if (!inventory.isAvailable(roomType)) {
            throw new InvalidBookingException("Selected room type is not available.");
        }
    }
}

/**
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Validation\n");

        Scanner scanner = new Scanner(System.in);

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Get user input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input
            validator.validate(guestName, roomType, inventory);

            // If valid → add to queue
            Reservation reservation = new Reservation(guestName, roomType);
            bookingQueue.addRequest(reservation);

            System.out.println("Booking request added successfully.");

        } catch (InvalidBookingException e) {

            // Handle validation errors
            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}
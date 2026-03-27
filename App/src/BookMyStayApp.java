import java.util.*;

/**
 * CLASS - RoomInventory
 * Manages room availability
 */
class RoomInventory {

    private Map<String, Integer> roomStock;

    public RoomInventory() {
        roomStock = new HashMap<>();
        roomStock.put("Single", 6);
        roomStock.put("Double", 4);
        roomStock.put("Suite", 2);
    }

    public void addRoomType(String type, int count) {
        roomStock.put(type, count);
    }

    public void increaseRoom(String type) {
        roomStock.put(type, roomStock.getOrDefault(type, 0) + 1);
    }

    public int getAvailableRooms(String type) {
        return roomStock.getOrDefault(type, 0);
    }
}

/**
 * CLASS - CancellationService
 * Use Case 10: Booking Cancellation & Inventory Rollback
 */
class CancellationService {

    /** Stack that stores recently released reservation IDs */
    private Stack<String> releasedRoomIds;

    /** Maps reservation ID to room type */
    private Map<String, String> reservationRoomTypeMap;

    /** Initializes cancellation tracking structures */
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels a confirmed booking and restores inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid cancellation request: " + reservationId);
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        // Restore inventory
        inventory.increaseRoom(roomType);

        // Track rollback
        releasedRoomIds.push(reservationId);

        // Remove from active bookings
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    /**
     * Displays recently cancelled reservations
     */
    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");
        for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
        }
    }
}

/**
 * MAIN CLASS - UseCase10BookingCancellation
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation\n");

        // Initialize inventory and cancellation service
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // Simulate confirmed bookings
        String reservationId = "Single-1";
        String roomType = "Single";

        cancellationService.registerBooking(reservationId, roomType);

        // Cancel booking
        cancellationService.cancelBooking(reservationId, inventory);

        // Show rollback history
        cancellationService.showRollbackHistory();

        // Display updated room availability
        System.out.println("\nUpdated " + roomType + " Room Availability: " +
                inventory.getAvailableRooms(roomType));
    }
}
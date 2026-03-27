import java.util.*;

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
 * Maintains available room count.
 */
class RoomInventory {

    private Map<String, Integer> roomStock;

    public RoomInventory() {
        roomStock = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        roomStock.put(type, count);
    }

    public boolean isAvailable(String type) {
        return roomStock.getOrDefault(type, 0) > 0;
    }

    public void reduceRoom(String type) {
        roomStock.put(type, roomStock.get(type) - 1);
    }
}

/**
 * CLASS - RoomAllocationService
 * Use Case 6: Reservation Confirmation & Room Allocation
 */
class RoomAllocationService {

    /** Stores all allocated room IDs */
    private Set<String> allocatedRoomIds;

    /** Stores assigned room IDs by room type */
    private Map<String, Set<String>> assignedRoomsByType;

    /** Initializes allocation tracking structures */
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms booking and assigns room
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        // Check availability
        if (!inventory.isAvailable(roomType)) {
            System.out.println("No rooms available for " + reservation.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Store allocation
        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Update inventory
        inventory.reduceRoom(roomType);

        // Confirmation output
        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: " + roomId);
    }

    /**
     * Generates unique room ID
     */
    private String generateRoomId(String roomType) {

        assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());
        int count = assignedRoomsByType.get(roomType).size() + 1;

        String roomId;
        do {
            roomId = roomType + "-" + count++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}

/**
 * MAIN CLASS - UseCase6RoomAllocation
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Suite", 1);

        // Create booking queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Abhi", "Single"));
        queue.offer(new Reservation("Subha", "Single"));
        queue.offer(new Reservation("Vanmathi", "Suite"));

        // Initialize allocation service
        RoomAllocationService allocator = new RoomAllocationService();

        // Process queue
        while (!queue.isEmpty()) {
            allocator.allocateRoom(queue.poll(), inventory);
        }
    }
}
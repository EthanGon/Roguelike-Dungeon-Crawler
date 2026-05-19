// Level Logic class used to handle when boss door should open
public class LevelLogic {
    private static int roomsCleared = 0;
    private static int roomsNeededCleared = 0;

    public static void increaseClearedRooms() {
        roomsCleared++;
        System.out.println(roomsCleared + "/" + getRoomsNeededCleared());

        if (roomsNeededCleared == roomsCleared) {
            System.out.println("Boss Door Opened");
            Room.findBossDoor().hasCollision = false;
        }
    }

    public static int getNumClearedRooms() {
        return roomsCleared;
    }

    public static int getRoomsNeededCleared() {
        return roomsNeededCleared;
    }

    public static void increaseNeededClears() {
        roomsNeededCleared++;
    }

    public static void restartStats() {
        roomsCleared = 0;
        roomsNeededCleared = 0;
        Room.resetNumRooms();
    }

}

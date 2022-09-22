package national_gallery_route_finder;

public class Room {

    private String roomName;
    private int xCoord;
    private int yCoord;

    public Room(String roomName, int xCoord, int yCoord) {
        this.roomName = roomName;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord(){
        return yCoord;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }
}

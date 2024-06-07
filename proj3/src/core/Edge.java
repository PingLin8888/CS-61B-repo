package core;

public class Edge {
    private Room room1;
    private Room room2;
    private double distance;

    public Edge(Room room1, Room room2, double distance) {
        this.room1 = room1;
        this.room2 = room2;
        this.distance = distance;
    }

    public Room getRoom1() {
        return room1;
    }

    public void setRoom1(Room room1) {
        this.room1 = room1;
    }

    public Room getRoom2() {
        return room2;
    }

    public void setRoom2(Room room2) {
        this.room2 = room2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

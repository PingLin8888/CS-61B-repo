package core;

import org.checkerframework.checker.units.qual.A;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room implements Comparable<Room> {

    private int width;
    private int height;
    private int positionX;
    private int positionY;

    public Room(int width, int height, int positionX, int positionY) {
        this.width = width;
        this.height = height;
        this.positionX = positionX;
        this.positionY = positionY;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return width == room.width && height == room.height && positionX == room.positionX && positionY == room.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, positionX, positionY);
    }


    @Override
    public int compareTo(Room o) {
        if (this.positionY != o.positionY) {
            return Integer.compare(this.positionY, o.positionY);
        }
        return Integer.compare(this.positionX,o.positionX);
    }

    public double calculateDistance(Room room2) {
        int x1 = this.getPositionX() + this.getWidth() / 2;
        int y1 = this.getPositionY() + this.getHeight() / 2;
        int x2 = room2.getPositionX() + room2.getWidth() / 2;
        int y2 = room2.getPositionY() + room2.getHeight() / 2;
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public Iterable<Point> roomPoints() {
        List<Point> points = new ArrayList<>();
        int x = this.getPositionX();
        int y = this.getPositionY();
        int width = this.getWidth();
        int height = this.getHeight();
        for (int i = x; i <= x + width; i++) {
            for (int j = y; j <= y + height; j++) {
                points.add(new Point(i, j));
            }
        }
        return points;
    }

    @Override
    public String toString() {
        return "Room{" +
                "width=" + width +
                ", height=" + height +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
    }
}

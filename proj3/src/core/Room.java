package core;

import java.util.Objects;

public class Room {
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

    public boolean isOverlap(Room other){
        int x1 = this.positionX;
        int y1 = this.positionY;
        int width1 = this.width;
        int height1 = this.height;
        int x2 = other.positionX;
        int y2 = other.positionY;
        int width2 = other.width;
        int height2 = other.height;
        return (x1 < x2 + width2) && (x1 + width1 > x2) && (y1 < y2 + height2) && (y1 + height1 > y2);
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
}

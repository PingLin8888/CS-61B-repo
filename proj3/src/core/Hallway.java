package core;

import java.util.Objects;

public class Hallway{
    protected int startX, startY;
    protected int endX, endY;

    public Hallway() {
    }

    public Hallway(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public boolean isHorizontal(){
        return startY == endY;
    }

    public boolean isVertical(){
        return startX == endX;
    }


    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hallway hallway = (Hallway) o;
        return startX == hallway.startX && startY == hallway.startY && endX == hallway.endX && endY == hallway.endY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startX, startY, endX, endY);
    }
}

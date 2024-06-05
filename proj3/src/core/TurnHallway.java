package core;

import java.util.Objects;

public class TurnHallway extends Hallway{
    private int midX;
    private int midY;

    public TurnHallway(int startX, int startY, int midX, int midY, int endX, int endY) {
        super(startX, startY, endX, endY);
        this.midX = midX;
        this.midY = midY;

    }


    public int getMidX() {
        return midX;
    }

    public void setMidX(int midX) {
        this.midX = midX;
    }

    public int getMidY() {
        return midY;
    }

    public void setMidY(int midY) {
        this.midY = midY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TurnHallway that = (TurnHallway) o;
        return midX == that.midX && midY == that.midY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), midX, midY);
    }
}

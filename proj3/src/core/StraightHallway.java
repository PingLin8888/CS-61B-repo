package core;

import java.util.Objects;

public class StraightHallway extends Hallway{


    public StraightHallway(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
    }


    @Override
    public boolean isOverlaping(Hallway other) {
        if (isHorizontal() && other.isHorizontal()) {
            if (startY + 3 > other.startY && startY < other.startY + 3) {
                return startX <= other.endX && endX >= other.startX;
            }
        } else if (isVertical() && other.isVertical()) {
            if (startX + 3 > other.startX && startX < other.startX + 3) {
                return startY <= other.endY && endY >= other.startY;
            }
        }
        return false;
    }



}

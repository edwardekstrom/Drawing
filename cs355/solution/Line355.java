package cs355.solution;

import java.awt.*;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Line355 extends Shape355 {
    Color color = null;
    Point start = null;
    Point end = null;

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
}

package cs355.solution;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Line355 extends Shape355 {
    Point2D.Double start = null;
    Point2D.Double end = null;

    public Point2D.Double getStart() {
        return start;
    }

    public void setStart(Point2D.Double start) {
        this.start = start;
    }

    public Point2D.Double getEnd() {
        return end;
    }

    public void setEnd(Point2D.Double end) {
        this.end = end;
    }
}

package cs355.solution;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Square355 extends Shape355 {
    private Point2D.Double topLeft = null;
    private double size = 0;

    public Point2D.Double getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point2D.Double topLeft) {
        this.topLeft = topLeft;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}

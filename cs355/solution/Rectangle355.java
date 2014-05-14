package cs355.solution;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Rectangle355 extends Shape355 {
    private Point2D.Double topLeft = null;
    private double height = 0;
    private double width = 0;

    public Point2D.Double getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point2D.Double topLeft) {
        this.topLeft = topLeft;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}

package cs355.solution;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public abstract class Shape355 {
    private Color color;
    private Point2D.Double center;
    private double rotation;

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    public Point2D.Double getCenter() { return center; }
    public void setCenter(Point2D.Double center) { this.center = center; }
    public double getRotation() { return rotation; }
    public void setRotation(double center) { this.rotation = rotation; }
}

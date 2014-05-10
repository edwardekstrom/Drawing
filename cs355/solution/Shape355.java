package cs355.solution;

import java.awt.*;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public abstract class Shape355 {
    private Color color;
    private Point center;

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    public Point getCenter() { return center; }
    public void setCenter(Point center) { this.center = center; }
}

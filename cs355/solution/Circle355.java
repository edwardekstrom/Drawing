package cs355.solution;

import java.awt.*;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Circle355 extends Shape355 {
    private Color color = null;
    private Point center = null;
    private int r = 0;

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}

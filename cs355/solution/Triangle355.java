package cs355.solution;

import java.awt.*;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Triangle355 implements Shape355 {
    private Color color = null;
    private Point p1 = null;
    private Point p2 = null;
    private Point p3 = null;

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP3() {
        return p3;
    }

    public void setP3(Point p3) {
        this.p3 = p3;
    }

    @Override
    public Color getColor() {
        return color;

    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}

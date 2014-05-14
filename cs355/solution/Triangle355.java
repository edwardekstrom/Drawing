package cs355.solution;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Triangle355 extends Shape355 {
    private Point2D.Double p1 = null;
    private Point2D.Double p2 = null;
    private Point2D.Double p3 = null;

    public Point2D.Double getP1() {
        return p1;
    }

    public void setP1(Point2D.Double p1) {
        this.p1 = p1;
    }

    public Point2D.Double getP2() {
        return p2;
    }

    public void setP2(Point2D.Double p2) {
        this.p2 = p2;
    }

    public Point2D.Double getP3() { return p3; }

    public void setP3(Point2D.Double p3) {
        this.p3 = p3;
    }
}

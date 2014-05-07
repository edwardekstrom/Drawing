package cs355.solution;

import java.awt.*;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Square355 extends Shape355 {
    private Color color = null;
    private Point topLeft = null;
    private int size = 0;

    public Point getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

package cs355.solution;

import java.awt.*;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Rectangle355 extends Shape355 {
    private Color color = null;
    private Point topLeft = null;
    private int height = 0;
    private int width = 0;

    public Point getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

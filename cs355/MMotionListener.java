package cs355;

import cs355.solution.Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class MMotionListener implements MouseMotionListener {
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
//        System.out.println("Mouse dragged to (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
        Point2D.Double p = new Point2D.Double(mouseEvent.getPoint().getX(),mouseEvent.getPoint().getY());
        Controller.getInstance().update(p);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
//        System.out.println("Mouse moved to (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
    }
}

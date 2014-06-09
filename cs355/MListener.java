package cs355;

import cs355.solution.Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class MListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
//        System.out.println("Mouse clicked at (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
        Point2D.Double p = new Point2D.Double(mouseEvent.getPoint().getX(),mouseEvent.getPoint().getY());
//        Controller.getInstance().clickAt(p);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
//        System.out.println("Mouse pressed at (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
        Point2D.Double p = new Point2D.Double(mouseEvent.getPoint().getX(),mouseEvent.getPoint().getY());
//        Controller.getInstance().start(p);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
//        System.out.println("Mouse released at (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
//        Controller.getInstance().end();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

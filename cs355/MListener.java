package cs355;

import cs355.solution.Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class MListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
//        System.out.println("Mouse clicked at (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
        Controller.getInstance().clickAt(mouseEvent.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
//        System.out.println("Mouse pressed at (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
        Controller.getInstance().start(mouseEvent.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
//        System.out.println("Mouse released at (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
        Controller.getInstance().end();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

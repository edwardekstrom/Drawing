package cs355;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class MMotionListener implements MouseMotionListener {
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
//        System.out.println("Mouse dragged to (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
//        System.out.println("Mouse moved to (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ").");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.solution;

import cs355.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author [your name here]
 */
public class CS355 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	// Fill in the parameters below with your controller, view, 
    	//   mouse listener, and mouse motion listener
        CS355Controller controller = Controller.getInstance();
        ViewRefresher refresher = new Refresher();
        MouseListener mouseListener = new MListener();
        MouseMotionListener motionListener = new MMotionListener();

        GUIFunctions.createCS355Frame(controller,refresher,mouseListener,motionListener);
        controller.zoomOutButtonHit();
        GUIFunctions.refresh();        
    }
}
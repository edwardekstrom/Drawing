package cs355;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Refresher implements ViewRefresher
{
    @Override
    public void refreshView(Graphics2D g2d) {
        Shape r = new Rectangle(2,2,20,20);
        g2d.draw(r);
        Shape c = new Ellipse2D.Double(2,2,20,20);
        g2d.draw(c);
    }
}

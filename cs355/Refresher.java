package cs355;

import cs355.solution.*;
import cs355.solution.Line3D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Refresher implements ViewRefresher
{
    @Override
    public void refreshView(Graphics2D g2d) {
        Controller c = Controller.getInstance();

        if (c.displayHouse){

            WireFrame house = new HouseModel(0,-3,30);
            Iterator<Line3D> it = house.getLines();
            Matrix matrix = new Matrix();
            while(it.hasNext())
            {
                Line3D line = it.next();
                List<Point> points = matrix.transformFromCameraToWorld(line);
                if(points.size() > 1)
                {
                    g2d.setColor(Color.BLUE);
                    g2d.drawLine(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);
                }
            }
        }

    }
}

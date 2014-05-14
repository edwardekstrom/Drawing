package cs355;

import cs355.solution.*;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Refresher implements ViewRefresher
{
    @Override
    public void refreshView(Graphics2D g2d) {
        Controller c = Controller.getInstance();
        for(Shape355 s: c.model){
            drawShape(s,g2d);
        }

        Shape355 s = c.cur;
        if (s!=null) drawShape(s, g2d);

        if (c.selectedShape != null) {
            System.out.println("line selected");
            outlineShape(c.selectedShape, g2d);

        }

    }

    private void outlineShape(Shape355 s, Graphics2D g2d) {
        if(s.getColor() != Color.white) g2d.setColor(Color.white);
        else g2d.setColor(Color.red);

        if(s instanceof Line355){
            g2d.drawLine( (int)((Line355) s).getStart().getX(),(int)((Line355) s).getStart().getY(),
                    (int)((Line355) s).getEnd().getX(),(int)((Line355) s).getEnd().getY());
        }else if(s instanceof Square355){
//            int x = (int)((Square355) s).getTopLeft().getX();
            int x = (int)(s.getCenter().getX() - ((Square355) s).getSize()/2);
            int width = (int)((Square355) s).getSize();
//            int y = (int)((Square355) s).getTopLeft().getY();
            int y = (int)(s.getCenter().getY() - ((Square355) s).getSize()/2);
            int height = (int)((Square355) s).getSize();
            g2d.drawRect(x, y, width, height);
        }else if(s instanceof Rectangle355){
//            int x = (int)((Rectangle355) s).getTopLeft().getX();
            int x = (int)s.getCenter().getX() - (int)((Rectangle355) s).getWidth()/2;
            int width = (int)((Rectangle355) s).getWidth();
//            int y = (int)((Rectangle355) s).getTopLeft().getY();
            int y = (int)(s.getCenter().getY() - ((Rectangle355) s).getHeight()/2);
            int height = (int)((Rectangle355) s).getHeight();
            g2d.drawRect(x, y, width, height);
        }else if(s instanceof Circle355){
            int x = (int)s.getCenter().getX();
            int width = (int)(((Circle355) s).getR()*2);
            int y = (int)s.getCenter().getY();
            int height = (int)(((Circle355) s).getR()*2);
            x -= width/2;
            y -= height/2;
            g2d.drawOval(x,y,width,height);
        }else if(s instanceof Ellipse355){
            int x = (int)s.getCenter().getX();
            int width = (int)((Ellipse355) s).getWidth();
            int y = (int) s.getCenter().getY();
            int height = (int)((Ellipse355) s).getHeight();
            x -= width/2;
            y -= height/2;
            g2d.drawOval(x,y,width,height);
        }else if(s instanceof Triangle355){
            if(((Triangle355) s).getP3() != null) {
                Point2D.Double p1 = ((Triangle355) s).getP1();
                Point2D.Double p2 = ((Triangle355) s).getP2();
                Point2D.Double p3 = ((Triangle355) s).getP3();
                int[] xPoints = new int[3];
                int centerX = (int) s.getCenter().getX();
                int centerY = (int) s.getCenter().getY();
                xPoints[0] = (int) p1.getX() + centerX;
                xPoints[1] = (int) p2.getX() + centerX;
                xPoints[2] = (int) p3.getX() + centerX;
                int[] yPoints = new int[3];
                yPoints[0] = (int) p1.getY() + centerY;
                yPoints[1] = (int) p2.getY() + centerY;
                yPoints[2] = (int) p3.getY() + centerY;
                g2d.drawPolygon(xPoints, yPoints, 3);
            }
        }
    }

    private void drawShape(Shape355 s, Graphics2D g2d){
        g2d.setColor(s.getColor());
        if(s instanceof Line355){
            g2d.drawLine( (int)((Line355) s).getStart().getX(),(int)((Line355) s).getStart().getY(),
                    (int)((Line355) s).getEnd().getX(),(int)((Line355) s).getEnd().getY());
        }else if(s instanceof Square355){
//            int x = (int)((Square355) s).getTopLeft().getX();
            int x = (int)(s.getCenter().getX() - ((Square355) s).getSize()/2);
            int width = (int)((Square355) s).getSize();
//            int y = (int)((Square355) s).getTopLeft().getY();
            int y = (int)(s.getCenter().getY() - ((Square355) s).getSize()/2);
            int height = (int)((Square355) s).getSize();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Rectangle355){
//            int x = (int)((Rectangle355) s).getTopLeft().getX();
            int x = (int)s.getCenter().getX() - (int)((Rectangle355) s).getWidth()/2;
            int width = (int)((Rectangle355) s).getWidth();
//            int y = (int)((Rectangle355) s).getTopLeft().getY();
            int y = (int)(s.getCenter().getY() - ((Rectangle355) s).getHeight()/2);
            int height = (int)((Rectangle355) s).getHeight();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Circle355){
            int x = (int)s.getCenter().getX();
            int width = (int)(((Circle355) s).getR()*2);
            int y = (int)s.getCenter().getY();
            int height = (int)(((Circle355) s).getR()*2);
            x -= width/2;
            y -= height/2;
            g2d.fillOval(x,y,width,height);
        }else if(s instanceof Ellipse355){
            int x = (int)s.getCenter().getX();
            int width = (int)((Ellipse355) s).getWidth();
            int y = (int) s.getCenter().getY();
            int height = (int)((Ellipse355) s).getHeight();
            x -= width/2;
            y -= height/2;
            g2d.fillOval(x,y,width,height);
        }else if(s instanceof Triangle355){
            if(((Triangle355) s).getP3() != null) {
                Point2D.Double p1 = ((Triangle355) s).getP1();
                Point2D.Double p2 = ((Triangle355) s).getP2();
                Point2D.Double p3 = ((Triangle355) s).getP3();
                int[] xPoints = new int[3];
                int centerX = (int) s.getCenter().getX();
                int centerY = (int) s.getCenter().getY();
                xPoints[0] = (int) p1.getX() + centerX;
                xPoints[1] = (int) p2.getX() + centerX;
                xPoints[2] = (int) p3.getX() + centerX;
                int[] yPoints = new int[3];
                yPoints[0] = (int) p1.getY() + centerY;
                yPoints[1] = (int) p2.getY() + centerY;
                yPoints[2] = (int) p3.getY() + centerY;
                g2d.fillPolygon(xPoints, yPoints, 3);
            }
        }
    }
}

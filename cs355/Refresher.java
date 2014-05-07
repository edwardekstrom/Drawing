package cs355;

import cs355.solution.*;

import java.awt.*;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Refresher implements ViewRefresher
{
    @Override
    public void refreshView(Graphics2D g2d) {
        Controller c = Controller.getInstance();
        for(Shape355 s: c.model){
            if(s instanceof Line355){
                g2d.setColor(s.getColor());
                g2d.drawLine( (int)((Line355) s).getStart().getX(),(int)((Line355) s).getStart().getY(),
                        (int)((Line355) s).getEnd().getX(),(int)((Line355) s).getEnd().getY());
            }else if(s instanceof Square355){
                g2d.setColor(s.getColor());
                int x = (int)((Square355) s).getTopLeft().getX();
                int width = (int)((Square355) s).getSize();
                int y = (int)((Square355) s).getTopLeft().getY();
                int height = (int)((Square355) s).getSize();
                g2d.fillRect(x, y, width, height);
            }else if(s instanceof Rectangle355){
                g2d.setColor(s.getColor());
                int x = (int)((Rectangle355) s).getTopLeft().getX();
                int width = (int)((Rectangle355) s).getWidth();
                int y = (int)((Rectangle355) s).getTopLeft().getY();
                int height = (int)((Rectangle355) s).getHeight();
                g2d.fillRect(x, y, width, height);
            }else if(s instanceof Triangle355){
                g2d.setColor(s.getColor());
                Point p1 = ((Triangle355) s).getP1();
                Point p2 = ((Triangle355) s).getP2();
                Point p3 = ((Triangle355) s).getP3();
                int[] xPoints = new int[3];
                xPoints[0] = (int)p1.getX(); xPoints[1] = (int)p2.getX(); xPoints[2] = (int)p3.getX();
                int[] yPoints = new int[3];
                yPoints[0] = (int)p1.getY(); yPoints[1] = (int)p2.getY(); yPoints[2] = (int)p3.getY();
                g2d.fillPolygon(xPoints,yPoints,3);
            }else if(s instanceof Circle355){
                g2d.setColor(s.getColor());
                int x = (int)((Circle355) s).getCenter().getX();
                int width = (int)(((Circle355) s).getR()*2);
                int y = (int)((Circle355) s).getCenter().getY();
                int height = (int)(((Circle355) s).getR()*2);
                x -= width/2;
                y -= height/2;
                g2d.fillOval(x,y,width,height);
            }else if(s instanceof Ellipse355){
                g2d.setColor(s.getColor());
                int x = (int)((Ellipse355) s).getCenter().getX();
                int width = (int)((Ellipse355) s).getWidth();
                int y = (int)((Ellipse355) s).getCenter().getY();
                int height = (int)((Ellipse355) s).getHeight();
                x -= width/2;
                y -= height/2;
                g2d.fillOval(x,y,width,height);
            }
        }

        Shape355 s = c.cur;
        if(s instanceof Line355){
            g2d.setColor(s.getColor());
            g2d.drawLine( (int)((Line355) s).getStart().getX(),(int)((Line355) s).getStart().getY(),
                    (int)((Line355) s).getEnd().getX(),(int)((Line355) s).getEnd().getY());
        }else if(s instanceof Square355){
            g2d.setColor(s.getColor());
            int x = (int)((Square355) s).getTopLeft().getX();
            int width = (int)((Square355) s).getSize();
            int y = (int)((Square355) s).getTopLeft().getY();
            int height = (int)((Square355) s).getSize();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Rectangle355){
            g2d.setColor(s.getColor());
            int x = (int)((Rectangle355) s).getTopLeft().getX();
            int width = (int)((Rectangle355) s).getWidth();
            int y = (int)((Rectangle355) s).getTopLeft().getY();
            int height = (int)((Rectangle355) s).getHeight();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Circle355){
            g2d.setColor(s.getColor());
            int x = (int)((Circle355) s).getCenter().getX();
            int width = (int)(((Circle355) s).getR()*2);
            int y = (int)((Circle355) s).getCenter().getY();
            int height = (int)(((Circle355) s).getR()*2);
            x -= width/2;
            y -= height/2;
            g2d.fillOval(x,y,width,height);
        }else if(s instanceof Ellipse355){
            g2d.setColor(s.getColor());
            int x = (int)((Ellipse355) s).getCenter().getX();
            int width = (int)((Ellipse355) s).getWidth();
            int y = (int)((Ellipse355) s).getCenter().getY();
            int height = (int)((Ellipse355) s).getHeight();
            x -= width/2;
            y -= height/2;
            g2d.fillOval(x,y,width,height);
        }

    }
}

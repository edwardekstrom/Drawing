package cs355;

import cs355.solution.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Refresher implements ViewRefresher
{

    public static double HANDLE_LENGTH = 30;
    @Override
    public void refreshView(Graphics2D g2d) {
        Controller c = Controller.getInstance();
        double cZoom = c.getZoom();
        for(Shape355 s: c.model){
            AffineTransform zoomTransform = new AffineTransform(cZoom,0,0,cZoom,-c.getHor(),-c.getVert());
            AffineTransform affine = c.objectToWorld(s);
            affine.concatenate(zoomTransform);
            g2d.setTransform(affine);
            drawShape(s, g2d);
            g2d.setTransform(new AffineTransform());
        }

        Shape355 s = c.cur;
        if (s!=null){
            AffineTransform zoomTransform = new AffineTransform(cZoom,0,0,cZoom,-c.getHor(),-c.getVert());
            AffineTransform affine = c.objectToWorld(s);
            affine.concatenate(zoomTransform);
            g2d.setTransform(affine);
            drawShape(s, g2d);
            g2d.setTransform(new AffineTransform());
        }

        if (c.selectedShape != null) {
            AffineTransform zoomTransform = new AffineTransform(cZoom,0,0,cZoom,-c.getHor(),-c.getVert());
            AffineTransform affine = c.objectToWorld(s);
            affine.concatenate(zoomTransform);
            g2d.setTransform(affine);
            outlineShape(c.selectedShape, g2d);
            g2d.setTransform(new AffineTransform());
        }

    }

    private void outlineShape(Shape355 s, Graphics2D g2d) {
        g2d.setColor(Color.RED);

        if(s instanceof Line355){
            drawLittleCircle(((Line355) s).getStart().getX(),((Line355) s).getStart().getY(),g2d);
            drawLittleCircle(((Line355) s).getEnd().getX(),((Line355) s).getEnd().getY(),g2d);
        }else if(s instanceof Square355){
            int x = (int)(0 - ((Square355) s).getSize()/2);
            int width = (int)((Square355) s).getSize();
            int y = (int)(0 - ((Square355) s).getSize()/2);
            int height = (int)((Square355) s).getSize();
            g2d.drawRect(x, y, width, height);
            drawLittleCircle(x,y,g2d);
            drawLittleCircle(x+width,y,g2d);
            drawLittleCircle(x, y + height, g2d);
            drawLittleCircle(x+width,y+height,g2d);
            drawLittleCircle(x+width,y+height,g2d);
            drawLittleCircle(x+width/2,y-HANDLE_LENGTH,g2d);
        }else if(s instanceof Rectangle355){
            int x = 0 - (int)((Rectangle355) s).getWidth()/2;
            int width = (int)((Rectangle355) s).getWidth();
            int y = (int)(0 - ((Rectangle355) s).getHeight()/2);
            int height = (int)((Rectangle355) s).getHeight();
            g2d.drawRect(x, y, width, height);
            drawLittleCircle(x,y,g2d);
            drawLittleCircle(x+width,y,g2d);
            drawLittleCircle(x,y+height,g2d);
            drawLittleCircle(x+width,y+height,g2d);
            drawLittleCircle(x+width/2,y-HANDLE_LENGTH,g2d);
        }else if(s instanceof Circle355){
            int x = 0;
            int width = (int)(((Circle355) s).getR()*2);
            int y = 0;
            int height = (int)(((Circle355) s).getR()*2);
            x -= width/2;
            y -= height/2;
            g2d.drawOval(x, y, width, height);
            drawLittleCircle(x,y,g2d);
            drawLittleCircle(x+width,y,g2d);
            drawLittleCircle(x,y+height,g2d);
            drawLittleCircle(x+width,y+height,g2d);
        }else if(s instanceof Ellipse355){
            int x = 0;
            int width = (int)((Ellipse355) s).getWidth();
            int y = (int) 0;
            int height = (int)((Ellipse355) s).getHeight();
            x -= width/2;
            y -= height/2;
            g2d.drawOval(x, y, width, height);
            drawLittleCircle(x,y,g2d);
            drawLittleCircle(x+width,y,g2d);
            drawLittleCircle(x,y+height,g2d);
            drawLittleCircle(x+width,y+height,g2d);
            drawLittleCircle(x+width/2,y-HANDLE_LENGTH,g2d);
        }else if(s instanceof Triangle355){
            if(((Triangle355) s).getP3() != null) {
                Point2D.Double p1 = ((Triangle355) s).getP1();
                Point2D.Double p2 = ((Triangle355) s).getP2();
                Point2D.Double p3 = ((Triangle355) s).getP3();
                int[] xPoints = new int[3];
                int centerX = 0;
                int centerY = 0;
                xPoints[0] = (int) p1.getX() + centerX;
                xPoints[1] = (int) p2.getX() + centerX;
                xPoints[2] = (int) p3.getX() + centerX;
                int[] yPoints = new int[3];
                yPoints[0] = (int) p1.getY() + centerY;
                yPoints[1] = (int) p2.getY() + centerY;
                yPoints[2] = (int) p3.getY() + centerY;
                g2d.drawPolygon(xPoints, yPoints, 3);
                drawLittleCircle(p1.getX() + centerX,p1.getY() + centerY,g2d);
                drawLittleCircle(p2.getX() + centerX,p2.getY() + centerY,g2d);
                drawLittleCircle(p3.getX() + centerX,p3.getY() + centerY,g2d);
                Double minY = (double)yPoints[0];
                if(yPoints[1] < minY) minY = (double)yPoints[1];
                if(yPoints[2] < minY) minY = (double)yPoints[2];
                drawLittleCircle(centerX,minY-HANDLE_LENGTH,g2d);

            }
        }
    }

    private void drawLittleCircle(double x, double y, Graphics2D g2d){
        g2d.drawOval((int)x - 5,(int)y - 5,10,10);
    }

    private void drawShape(Shape355 s, Graphics2D g2d){
        g2d.setColor(s.getColor());
        if(s instanceof Line355){
            g2d.drawLine( (int)((Line355) s).getStart().getX(),(int)((Line355) s).getStart().getY(),
                    (int)((Line355) s).getEnd().getX(),(int)((Line355) s).getEnd().getY());
        }else if(s instanceof Square355){
            int x = (int)(0 - ((Square355) s).getSize()/2);
            int width = (int)((Square355) s).getSize();
            int y = (int)(0 - ((Square355) s).getSize()/2);
            int height = (int)((Square355) s).getSize();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Rectangle355){
            int x = -(int)((Rectangle355) s).getWidth()/2;
            int width = (int)((Rectangle355) s).getWidth();
            int y = -(int)((Rectangle355) s).getHeight()/2;
            int height = (int)((Rectangle355) s).getHeight();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Circle355){
            int x = 0;
            int width = (int)(((Circle355) s).getR()*2);
            int y = 0;
            int height = (int)(((Circle355) s).getR()*2);
            x -= width/2;
            y -= height/2;
            g2d.fillOval(x,y,width,height);
        }else if(s instanceof Ellipse355){
            int x = 0;
            int width = (int)((Ellipse355) s).getWidth();
            int y = 0;
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
                int centerX = 0;
                int centerY = 0;
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

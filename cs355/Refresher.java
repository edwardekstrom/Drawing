package cs355;

import cs355.solution.*;
import cs355.solution.Line3D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Refresher implements ViewRefresher
{
    public static double HANDLE_LENGTH = 30;
    private double zoomLevel = 1;
    @Override
    public void refreshView(Graphics2D g2d) {
        Controller c = Controller.getInstance();
        zoomLevel = c.getZoom();
        for(Shape355 s: c.model){
            AffineTransform zoomTransform = c.worldToView();
            AffineTransform affine = c.objectToWorld(s);
            zoomTransform.concatenate(affine);
            g2d.setTransform(zoomTransform);
            drawShape(s, g2d);
            g2d.setTransform(new AffineTransform());
        }

        Shape355 s = c.cur;
        if (s!=null){
            AffineTransform zoomTransform = c.worldToView();
            AffineTransform affine = c.objectToWorld(s);
            zoomTransform.concatenate(affine);
            g2d.setTransform(zoomTransform);
            drawShape(s, g2d);
            g2d.setTransform(new AffineTransform());
        }

        if (c.selectedShape != null) {
            AffineTransform zoomTransform = c.worldToView();
            AffineTransform affine = c.objectToWorld(c.selectedShape);
            zoomTransform.concatenate(affine);
            g2d.setTransform(zoomTransform);
            outlineShape(c.selectedShape, g2d);
            g2d.setTransform(new AffineTransform());
        }
        if (c.threeDMode) {
            for (WireFrame house : c._houses) {
                Iterator<Line3D> it = house.getLines();
                Perspective355 perspective = new Perspective355();
                while (it.hasNext()) {
                    Line3D line = it.next();
                    Point[] pointArray = perspective.worldToView(line);
                    if (pointArray != null) {
                        g2d.setColor(Color.BLUE);
                        g2d.drawLine(pointArray[0].x, pointArray[0].y, pointArray[1].x, pointArray[1].y);
                    }
                }
            }
        }

        if (c._image != null){
            BufferedImage bi = c.getBufferedImage();
            g2d.scale(c.getZoom()*4, c.getZoom()*4);
            g2d.translate(256 - c._imgCols/2, 256 - c._imgRows/2);
            g2d.translate(-c.getHor(), -c.getVert());
            g2d.drawImage(bi, null, 0, 0);
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
        if(false){
            g2d.drawOval((int)x - 10,(int)y - 10,20,20);
        }else {
            double unit = 10*(1/zoomLevel);
            g2d.drawOval((int) x - (int)(unit/2), (int) y - (int)(unit/2), (int)unit, (int)unit);
        }
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

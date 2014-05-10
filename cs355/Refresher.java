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
            drawShape(s,g2d);
        }
        Shape355 s = c.cur;
        drawShape(s,g2d);

    }

    private void drawShape(Shape355 s, Graphics2D g2d){
        if(s instanceof Line355){
            g2d.setColor(s.getColor());
            g2d.drawLine( (int)((Line355) s).getStart().getX(),(int)((Line355) s).getStart().getY(),
                    (int)((Line355) s).getEnd().getX(),(int)((Line355) s).getEnd().getY());
        }else if(s instanceof Square355){
            g2d.setColor(s.getColor());
            int x = (int)((Square355) s).getTopLeft().getX();
            int width = ((Square355) s).getSize();
            int y = (int)((Square355) s).getTopLeft().getY();
            int height = ((Square355) s).getSize();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Rectangle355){
            g2d.setColor(s.getColor());
            int x = (int)((Rectangle355) s).getTopLeft().getX();
            int width = ((Rectangle355) s).getWidth();
            int y = (int)((Rectangle355) s).getTopLeft().getY();
            int height = ((Rectangle355) s).getHeight();
            g2d.fillRect(x, y, width, height);
        }else if(s instanceof Circle355){
            g2d.setColor(s.getColor());
            int x = (int)((Circle355) s).getCenter().getX();
            int width = (((Circle355) s).getR()*2);
            int y = (int)((Circle355) s).getCenter().getY();
            int height = (((Circle355) s).getR()*2);
            x -= width/2;
            y -= height/2;
            g2d.fillOval(x,y,width,height);
        }else if(s instanceof Ellipse355){
            g2d.setColor(s.getColor());
            int x = (int)((Ellipse355) s).getCenter().getX();
            int width = ((Ellipse355) s).getWidth();
            int y = (int)((Ellipse355) s).getCenter().getY();
            int height = ((Ellipse355) s).getHeight();
            x -= width/2;
            y -= height/2;
            g2d.fillOval(x,y,width,height);
        }else{

        }
    }
}

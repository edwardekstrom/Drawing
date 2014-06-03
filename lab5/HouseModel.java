package lab5;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Brennan Smith
 */
public class HouseModel extends WireFrame
{
    List<Line3D> lines = new ArrayList<Line3D>();
    
    public HouseModel(float xCenter, float yCenter, float zCenter){
        //Floor
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter,zCenter-5), new Point3D(xCenter+5,yCenter,zCenter-5)));
        lines.add(new Line3D(new Point3D(xCenter+5,yCenter,zCenter-5), new Point3D(xCenter+5,yCenter,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter+5,yCenter,zCenter+5), new Point3D(xCenter-5,yCenter,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter,zCenter+5), new Point3D(xCenter-5,yCenter,zCenter-5)));
        //Ceiling
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter+5,zCenter-5), new Point3D(xCenter+5,yCenter+5,zCenter-5)));
        lines.add(new Line3D(new Point3D(xCenter+5,yCenter+5,zCenter-5), new Point3D(xCenter+5,yCenter+5,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter+5,yCenter+5,zCenter+5), new Point3D(xCenter-5,yCenter+5,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter+5,zCenter+5), new Point3D(xCenter-5,yCenter+5,zCenter-5)));
        //Walls
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter+5,zCenter-5), new Point3D(xCenter-5,yCenter,zCenter-5)));
        lines.add(new Line3D(new Point3D(xCenter+5,yCenter+5,zCenter-5), new Point3D(xCenter+5,yCenter,zCenter-5)));
        lines.add(new Line3D(new Point3D(xCenter+5,yCenter+5,zCenter+5), new Point3D(xCenter+5,yCenter,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter+5,zCenter+5), new Point3D(xCenter-5,yCenter,zCenter+5)));
        //Roof
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter+5,zCenter-5), new Point3D(xCenter,yCenter+8,zCenter-5)));
        lines.add(new Line3D(new Point3D(xCenter,yCenter+8,zCenter-5), new Point3D(xCenter+5,yCenter+5,zCenter-5)));
        lines.add(new Line3D(new Point3D(xCenter-5,yCenter+5,zCenter+5), new Point3D(xCenter,yCenter+8,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter,yCenter+8,zCenter+5), new Point3D(xCenter+5,yCenter+5,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter,yCenter+8,zCenter-5), new Point3D(xCenter,yCenter+8,zCenter+5)));
        //Door
        lines.add(new Line3D(new Point3D(xCenter+1,yCenter,zCenter+5), new Point3D(xCenter+1,yCenter+3,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter-1,yCenter,zCenter+5), new Point3D(xCenter-1,3,zCenter+5)));
        lines.add(new Line3D(new Point3D(xCenter+1,yCenter+3,zCenter+5), new Point3D(xCenter-1,yCenter+3,zCenter+5)));
    }
    

    public Iterator<Line3D> getLines()
    {
        return lines.iterator();
    }
}

package lab5;

import java.util.ArrayList;
import java.util.Iterator;
import lab5.Point3D;
import lab5.Line3D;

/**
 *
 * @author Brennan Smith
 */
public class WireFrame 
{
    public Iterator<Line3D> getLines()
    {
        return new ArrayList<Line3D>().iterator();
    }
}

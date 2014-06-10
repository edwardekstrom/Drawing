package cs355.solution;

import cs355.Point3D;

import java.awt.Point;
import java.util.ArrayList;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.List;

public class ClipMatrix355
{

    private Camera355 _cam = Controller.getInstance().get_cam();
    private double _camRot = _cam.get_zRotation();
    private double _camX = _cam.get_loc().x;
    private double _camY = _cam.get_loc().y;
    private double _camZ = _cam.get_loc().z;
    private double _scale = Controller.getInstance().getZoom();
    private double _hor = Controller.getInstance().getHor();
    private double _vert = Controller.getInstance().getVert();

    public List<Point> camToView(Line3D next)
    {
        List<Point> allPoints = new ArrayList<Point>();
        Point3D startPoint = clip(next.start);
        Point3D endPoint = clip(next.end);

        if(clipMatrix(startPoint, endPoint))
        {
            return allPoints;
        }

        Point start = worldToView(startPoint);
        Point end = worldToView(endPoint);

        allPoints.add(start);
        allPoints.add(end);

        return allPoints;
    }
    private Point3D clip(Point3D start)
    {
        double x = -sqrt(3) * _camX * cos(_camRot) + sqrt(3) * start.x * cos(_camRot) - sqrt(3) * _camZ *
                sin(_camRot) + sqrt(3) * start.z * sin(_camRot);
        x /= -_camZ * cos(_camRot) + start.z * cos(_camRot) + _camX * sin(_camRot) - start.x * sin(_camRot);
        double y = sqrt(3) * start.y - sqrt(3) * _camY;
        y /= -_camZ * cos(_camRot) + start.z * cos(_camRot) + _camX * sin(_camRot) - start.x * sin(_camRot);
        double z = -2 - _camZ  * cos(_camRot) + start.z * cos(_camRot) + _camX  * sin(_camRot) - start.x * sin(_camRot);
        z /= ( -_camZ * cos(_camRot) + start.z * cos(_camRot) + _camX * sin(_camRot) - start.x * sin(_camRot));
        return new Point3D(x,y,z);
    }

    private Point worldToView(Point3D point)
    {
        double tempX = -_hor + (1024 + 1024 * point.x) * _scale;
        double tempY = -_vert + 1024 * _scale - 1024 * point.y * _scale;
        Point newPoint = new Point((int) tempX, (int) tempY);
        return newPoint;
    }

    private boolean clipMatrix(Point3D startPoint, Point3D endPoint)
    {
        if((startPoint.x < -1 && endPoint.x < -1) || (startPoint.x > 1 && endPoint.x > 1))
            return true;
        if((startPoint.y < -1 && endPoint.y < -1) || (startPoint.y > 1 && endPoint.y > 1))
            return true;
        if(startPoint.z < -1 || endPoint.z < -1 || startPoint.z > 1 || endPoint.z > 1)
            return true;
        return false;
    }

}

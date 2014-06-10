package cs355.solution;

import cs355.Point3D;

import java.awt.Point;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Perspective355
{
    private Controller _controller = Controller.getInstance();
    private Camera355 _cam = _controller.get_cam();
    private double _scale = _controller.getZoom();
    private double _hor = _controller.getHor();
    private double _vert = _controller.getVert();
    private double _camRot = _cam.get_zRotation();
    private double _camX = _cam.get_loc().x;
    private double _camY = _cam.get_loc().y;
    private double _camZ = _cam.get_loc().z;

    public Point[] worldToView(Line3D next)
    {
        Point[] allPoints = new Point[2];
        Point3D startPoint = toViewingFrutum(next.start);
        Point3D endPoint = toViewingFrutum(next.end);

        if(clip(startPoint, endPoint))
        {
            return null;
        }

        Point start = toView(startPoint);
        Point end = toView(endPoint);

        allPoints[0] = start;
        allPoints[1] = end;

        return allPoints;
    }
    private Point3D toViewingFrutum(Point3D start)
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

    private Point toView(Point3D point)
    {
        double xOff = -_hor + _scale * (1024 + 1024 * point.x);
        double yOff = -_vert + _scale * (1024 - 1024 * point.y);
        Point p = new Point((int) xOff, (int) yOff);
        return p;
    }

    private boolean clip(Point3D p0, Point3D p1)
    {
        if((p0.x < -1 && p1.x < -1) || (p0.x > 1 && p1.x > 1))
            return true;
        if((p0.y < -1 && p1.y < -1) || (p0.y > 1 && p1.y > 1))
            return true;
        if(p0.z < -1 || p1.z < -1 || p0.z > 1 || p1.z > 1)
            return true;
        return false;
    }

}

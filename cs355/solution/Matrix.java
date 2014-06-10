package cs355.solution;

import cs355.Point3D;

import java.awt.Point;
import java.util.ArrayList;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.List;

public class Matrix
{

    private Camera355 camera = Controller.getInstance().getCamera();
    private double camRolate = camera.getZRotation();
    private double cameraX = camera.getLocation().x;
    private double cameraY = camera.getLocation().y;
    private double cameraZ = camera.getLocation().z;
    private double scale = Controller.getInstance().getZoom();
    private double horizontal = Controller.getInstance().getHor();
    private double vertical = Controller.getInstance().getVert();

    public List<Point> transformFromCameraToWorld(Line3D next)
    {
        List<Point> points = new ArrayList<Point>();
        Point3D startPoint = this.getCanonical(next.start);
        Point3D endPoint = this.getCanonical(next.end);

        if(this.validatePoints(startPoint, endPoint))
        {
            return points;
        }

        Point start = convertToView(startPoint);
        Point end = convertToView(endPoint);

        points.add(start);
        points.add(end);

        return points;
    }

    private Point convertToView(Point3D point)
    {
        double tempX = -horizontal + (1024 + 1024 * point.x) * scale;
        double tempY = -vertical + 1024 * scale - 1024 * point.y * scale;
        Point newPoint = new Point((int) tempX, (int) tempY);
        return newPoint;
    }

    private boolean validatePoints(Point3D startPoint, Point3D endPoint)
    {
        double sX = startPoint.x;
        double sY = startPoint.y;
        double sZ = startPoint.z;

        double eX = endPoint.x;
        double eY = endPoint.y;
        double eZ = endPoint.z;

        if((sX < -1 && eX < -1) || (sX > 1 && eX > 1))
            return true;
        if((sY < -1 && eY < -1) || (sY > 1 && eY > 1))
            return true;
        if((sZ < -1 && eZ < -1) || (sZ > 1 && eZ > 1))
            return true;
        if(sZ < -1 || eZ < -1)
            return true;
//        if(cameraZ * cos(camRolate))
        return false;
    }

    private Point3D getCanonical(Point3D start)
    {
        double x = -sqrt(3) * this.cameraX * cos(this.camRolate) +
                sqrt(3) * start.x * cos(this.camRolate) - sqrt(3) * this.cameraZ *
                sin(this.camRolate) + sqrt(3) * start.z * sin(this.camRolate);

        x /= -this.cameraZ * cos(this.camRolate) + start.z * cos(this.camRolate) +
                this.cameraX * sin(this.camRolate) - start.x * sin(this.camRolate);

        double y = sqrt(3) * start.y - sqrt(3) * this.cameraY;
        y /= -this.cameraZ * cos(this.camRolate) + start.z * cos(this.camRolate) +
                this.cameraX * sin(this.camRolate) - start.x * sin(this.camRolate);

        double z = ((- 200 / 99) - cameraZ * (101 / 99) * cos(this.camRolate) +
                (101 / 99) * start.z * cos(this.camRolate) + cameraX * (101 / 99) *
                sin(this.camRolate) - (101 / 99) * start.x * sin(this.camRolate));
        z /= ( - cameraZ * cos(this.camRolate) + start.z * cos(this.camRolate) + cameraX * sin(this.camRolate) - start.x * sin(this.camRolate));

        return new Point3D(x,y,z);
    }
}

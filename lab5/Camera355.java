package lab5;

/**
 * Created by edwardekstrom on 6/2/14.
 */
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class Camera355 {
    private Point3D _location;
    private float _zRotation;

    public Camera355() {
        _location = new Point3D(-30, -2.5f, -15);
        _zRotation = 0.0f;
    }

    public Point3D getLocation()
    {
        return _location;
    }

    public void changeYBy(double y) { this._location.y += y; }

    public float getZRotation()
    {
        return _zRotation;
    }

    public void changeZRotationBy(float rotation)
    {
        this._zRotation += rotation;
    }

    public void walkForward(float dist) {
        _location.x -= dist * (float)sin(toRadians(_zRotation));
        _location.z += dist * (float)cos(toRadians(_zRotation));
    }

    public void walkBackward(float dist) {
        _location.x += dist * (float)sin(toRadians(_zRotation));
        _location.z -= dist * (float)cos(toRadians(_zRotation));
    }

    public void sidestepLeft(float dist) {
        _location.x -= dist * (float)sin(toRadians(_zRotation -90));
        _location.z += dist * (float)cos(toRadians(_zRotation -90));
    }

    public void sidestepRight(float dist) {
        _location.x -= dist * (float)sin(toRadians(_zRotation +90));
        _location.z += dist * (float)cos(toRadians(_zRotation +90));
    }
}
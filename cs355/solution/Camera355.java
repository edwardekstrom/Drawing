package cs355.solution;

import cs355.Point3D;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class Camera355 {
    private Point3D _location;
    private float _zRotation;

    public Camera355() {
        _location = new Point3D(0, 0, 0);
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
    
    public void sidestep(float distance) {
        int negate = distance >= 0 ? -1 : 1;
        float offset = 90 * negate;

        _location.x += negate * distance * (float) Math.sin(Math.toRadians(_zRotation + offset));
        _location.z += -negate * distance * (float) Math.cos(Math.toRadians(_zRotation + offset));
    }
}
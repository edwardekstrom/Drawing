package cs355.solution;

import cs355.Point3D;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class Camera355 {
    private Point3D _loc;
    private float _zRotation;

    public Camera355() {
        _loc = new Point3D(0.0d, 3d, -25d);
        _zRotation = 0.0f;
    }

    public Point3D get_loc() { return _loc; }

    public float get_zRotation() { return (float) toRadians(_zRotation); }

    public void changeZRotationBy(float yaw) {
        _zRotation += yaw;
    }

    public void changeYBy(float distance) {
        _loc.y += distance;
    }

    public void walkForward(float distance) {
        _loc.x -= distance * (float) sin(toRadians(_zRotation));
        _loc.z += distance * (float) cos(toRadians(_zRotation));
    }

    public void walkBackward(float distance) {
        _loc.x += distance * (float) sin(toRadians(_zRotation));
        _loc.z -= distance * (float) cos(toRadians(_zRotation));
    }

    public void sidestep(float distance) {
        int negate = distance >= 0 ? -1 : 1;
        float offset = 90 * negate;

        _loc.x += negate * distance * (float) sin(toRadians(_zRotation + offset));
        _loc.z += -negate * distance * (float) cos(toRadians(_zRotation + offset));
    }
}
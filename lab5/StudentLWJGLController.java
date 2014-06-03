package lab5;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import java.util.List;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController{
    private List<WireFrame> _houses = new ArrayList<WireFrame>();
    private Camera355 _cam = new Camera355();

    private static final float MOVE_RATE = 1;
    private static final float ROTATION_CONSTANT = 1;
    private static final float ORTHOG_CONSTANT = 10;
    private static final float NEAR_PLANE = 1;
    private static final float FAR_PLANE = 500;
    private static final float FIELD_OF_VIEW = 50;
    private static final float ASPECT_RATIO = (float)LWJGLSandbox.DISPLAY_WIDTH/(float)LWJGLSandbox.DISPLAY_HEIGHT;

    public StudentLWJGLController(){
        for(int i = 0; i < 61; i+=15){
            WireFrame house = new HouseModel((float)i,0,0);
            _houses.add(house);
        }

        for(int i = 0; i < 61; i+=15){
            WireFrame house = new HouseModel((float)i,0,30);
            _houses.add(house);
        }
    }

    @Override
    public void resizeGL(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(FIELD_OF_VIEW, ASPECT_RATIO, NEAR_PLANE, FAR_PLANE);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    @Override
    public void update(){
    }

    @Override
    public void updateKeyboard(){
        if(Keyboard.isKeyDown(Keyboard.KEY_H)) _cam = new Camera355();

        if(Keyboard.isKeyDown(Keyboard.KEY_W)) _cam.walkForward(MOVE_RATE);
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) _cam.walkBackward(MOVE_RATE);
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) _cam.sidestepLeft(MOVE_RATE);
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) _cam.sidestepRight(MOVE_RATE);
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)) _cam.changeZRotationBy(-MOVE_RATE * ROTATION_CONSTANT);
        if(Keyboard.isKeyDown(Keyboard.KEY_E)) _cam.changeZRotationBy(MOVE_RATE * ROTATION_CONSTANT);
        if(Keyboard.isKeyDown(Keyboard.KEY_R)) _cam.changeYBy(-MOVE_RATE);
        if(Keyboard.isKeyDown(Keyboard.KEY_F)) _cam.changeYBy(MOVE_RATE);

        if(Keyboard.isKeyDown(Keyboard.KEY_O)){
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-ORTHOG_CONSTANT, ORTHOG_CONSTANT, -ORTHOG_CONSTANT, ORTHOG_CONSTANT, NEAR_PLANE, FAR_PLANE);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_P)){
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            gluPerspective(FIELD_OF_VIEW, ASPECT_RATIO, NEAR_PLANE, FAR_PLANE);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
        }
    }

    @Override
    public void render(){
        glColor3f(0, 0, 255);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        translateToCamera();
        glBegin(GL_LINES);
        for(WireFrame house : _houses) {
            Iterator<Line3D> lineIterator = house.getLines();
            while (lineIterator.hasNext()) {
                Line3D l = lineIterator.next();
                glVertex3d(l.start.x, l.start.y, l.start.z);
                glVertex3d(l.end.x, l.end.y, l.end.z);
            }
        }
        glEnd();
    }

    public void translateToCamera(){
        Point3D pos = _cam.getLocation();
        float zRotation = _cam.getZRotation();
        glRotatef(zRotation, (float)0.0, (float)1.0, (float)0.0);
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
    }
}
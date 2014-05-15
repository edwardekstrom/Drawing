package cs355.solution;

import cs355.CS355Controller;
import cs355.GUIFunctions;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by edwardekstrom on 5/6/14.
 */
public class Controller implements CS355Controller {

    public Stack<Shape355> model = new Stack<Shape355>();
    public Deque<Shape355> modelDeque = new ArrayDeque<Shape355>();
    public Color color;
//    0 = line
//    1 = square
//    2 = rectangle
//    3 = circle
//    4 = ellipses
//    5 = triangel
    public int state = 0;
    public Shape355 cur = null;
    public Shape355 selectedShape = null;

    public static int LINE_ERROR = 4;
    public static double HANDLE_LENGTH = 30;

    private static volatile Controller instance = null;

    private Point2D.Double point = null;
    private Point2D.Double auxPoint0 = null;
    private Point2D.Double auxPoint1 = null;
    private Point2D.Double handlePoint = null;

    private Controller() {}
    public static Controller getInstance(){
        if (instance == null){
            synchronized (Controller.class) {
                if(instance == null) {
                    instance = new Controller();
                }
            }
        }
        return instance;
    }

    public void clickAt(Point2D.Double p){
        switch (state){
            case 5:
                triangle(p);
                break;
            default:
                break;
        }
        GUIFunctions.refresh();
    }

    private void checkIfShapeSelected(Point2D.Double p) {
        this.cur = null;
        this.point = null;
        this.auxPoint0 = null;
        this.auxPoint1 = null;
        for(Shape355 s: modelDeque){
            Point2D.Double pPrime = null;
            if(!(s instanceof Line355)) {
                pPrime = worldToObject(p, s);
            }

            if(s instanceof Line355){
                selectedShape = lineHitTest((Line355)s, p);
            }else if(s instanceof Square355){
                selectedShape = squareHitTest((Square355)s,pPrime);
            }else if(s instanceof Rectangle355){
                selectedShape = rectangleHitTest((Rectangle355)s,pPrime);
            }else if(s instanceof Circle355){
                selectedShape = circleHitTest((Circle355)s,pPrime);
            }else if(s instanceof Ellipse355){
                selectedShape = ellipseHitTest((Ellipse355)s,pPrime);
            }else if(s instanceof Triangle355){
                selectedShape = triangleHitTest((Triangle355)s,pPrime);
            }

            if (selectedShape != null && handlePoint == null) {
                if(selectedShape instanceof Line355){
                    auxPoint0 = new Point2D.Double(((Line355) selectedShape).getStart().getX()-p.getX(),
                            ((Line355) selectedShape).getStart().getY()-p.getY());
                    auxPoint1 = new Point2D.Double(((Line355) selectedShape).getEnd().getX()-p.getX(),
                            ((Line355) selectedShape).getEnd().getY()-p.getY());
                }else{
                    auxPoint0 = new Point2D.Double(selectedShape.getCenter().getX()-p.getX(),
                            selectedShape.getCenter().getY()-p.getY());
                }
                break;
            }
        }

    }

    private void triangle(Point2D.Double p){
        if(point == null){
            point = p;
            cur = new Triangle355();
            cur.setColor(color);
            ((Triangle355)cur).setP1(p);
        }else if(auxPoint0 == null){
            auxPoint0 = p;
            ((Triangle355)cur).setP2(p);
        }else if(auxPoint1 == null){
            auxPoint1 = p;
            ((Triangle355)cur).setP3(p);
            double totalX = ((Triangle355) cur).getP1().getX() + ((Triangle355) cur).getP2().getX() + ((Triangle355) cur).getP3().getX();
            double averageX = totalX / 3;
            double totalY = ((Triangle355) cur).getP1().getY() + ((Triangle355) cur).getP2().getY() + ((Triangle355) cur).getP3().getY();
            double averageY = totalY / 3;
            cur.setCenter(new Point2D.Double(averageX, averageY));
            ((Triangle355) cur).setP1(new Point2D.Double((((Triangle355) cur).getP1().getX() - cur.getCenter().getX()), (((Triangle355) cur).getP1().getY() - cur.getCenter().getY())));
            ((Triangle355) cur).setP2(new Point2D.Double((((Triangle355) cur).getP2().getX() - cur.getCenter().getX()), (((Triangle355) cur).getP2().getY() - cur.getCenter().getY())));
            ((Triangle355) cur).setP3(new Point2D.Double((((Triangle355) cur).getP3().getX() - cur.getCenter().getX()), (((Triangle355) cur).getP3().getY() - cur.getCenter().getY())));
            model.push(cur);
            modelDeque.push(cur);
            point = null;
            auxPoint0 = null;
            auxPoint1 = null;
            cur = null;
        }else{
            System.out.println("Should never get here...");
        }
    }

    public void start(Point2D.Double p){
        if(state != 5 && state != 6){
            point = p;
            switch(state){
                case 0:
                    startLine(p);
                    break;
                case 1:
                    startSquare(p);
                    break;
                case 2:
                    startRectangle(p);
                    break;
                case 3:
                    startCircle(p);
                    break;
                case 4:
                    startEllipses(p);
                    break;
                default:
                    break;
            }
            GUIFunctions.refresh();
        }else if(state == 6){
            if(selectedShape != null){
                Point2D.Double pPrime = worldToObject(p, selectedShape);
                Point2D.Double handle = null;
                if(selectedShape instanceof Line355){
                }else if(selectedShape instanceof Square355){
                    int x = (int)(0 - ((Square355) selectedShape).getSize()/2);
                    int width = (int)((Square355) selectedShape).getSize();
                    int y = (int)(0 - ((Square355) selectedShape).getSize()/2);
                    int height = (int)((Square355) selectedShape).getSize();
                    handle = new Point2D.Double(x+width/2,y-HANDLE_LENGTH);
                    //handle = worldToObject(handle,selectedShape);
                }else if(selectedShape instanceof Rectangle355){
                    int x = 0 - (int)((Rectangle355) selectedShape).getWidth()/2;
                    int width = (int)((Rectangle355) selectedShape).getWidth();
                    int y = (int)(0 - ((Rectangle355) selectedShape).getHeight()/2);
                    int height = (int)((Rectangle355) selectedShape).getHeight();
                    handle = new Point2D.Double(x+width/2,y-HANDLE_LENGTH);
                    //handle = worldToObject(handle,selectedShape);
                }else if(selectedShape instanceof Circle355){
                    int x = 0;
                    int width = (int)(((Circle355) selectedShape).getR()*2);
                    int y = 0;
                    int height = (int)(((Circle355) selectedShape).getR()*2);
                    x -= width/2;
                    y -= height/2;
                    handle = new Point2D.Double(x+width/2,y-HANDLE_LENGTH);
                    //handle = worldToObject(handle,selectedShape);
                }else if(selectedShape instanceof Ellipse355){
                    int x = 0;
                    int width = (int)((Ellipse355) selectedShape).getWidth();
                    int y = 0;
                    int height = (int)((Ellipse355) selectedShape).getHeight();
                    x -= width/2;
                    y -= height/2;
                    handle = new Point2D.Double(x+width/2,y-HANDLE_LENGTH);
                    //handle = worldToObject(handle,selectedShape);
                }else if(selectedShape instanceof Triangle355) {
                    if (((Triangle355) selectedShape).getP3() != null) {
                        Point2D.Double p1 = ((Triangle355) selectedShape).getP1();
                        Point2D.Double p2 = ((Triangle355) selectedShape).getP2();
                        Point2D.Double p3 = ((Triangle355) selectedShape).getP3();
                        int[] xPoints = new int[3];
                        int centerX = (int) 0;
                        int centerY = (int) 0;
                        xPoints[0] = (int) p1.getX() + centerX;
                        xPoints[1] = (int) p2.getX() + centerX;
                        xPoints[2] = (int) p3.getX() + centerX;
                        int[] yPoints = new int[3];
                        yPoints[0] = (int) p1.getY() + centerY;
                        yPoints[1] = (int) p2.getY() + centerY;
                        yPoints[2] = (int) p3.getY() + centerY;
                        Double minY = (double) yPoints[0];
                        if (yPoints[1] < minY) minY = (double) yPoints[1];
                        if (yPoints[2] < minY) minY = (double) yPoints[2];
                        handle = new Point2D.Double(centerX, minY - HANDLE_LENGTH);
//                        handle = worldToObject(handle, selectedShape);
                    }
                }
                if(handle != null && handle.distanceSq(pPrime) <= 100){
                    handlePoint = p;
                }
            }

            if(handlePoint==null)
                checkIfShapeSelected(p);

//            if(selectedShape instanceof Line355){
//                if(p.distanceSq(((Line355) selectedShape).getStart()) <= 100){
//                    cur = selectedShape;
//                    Point2D.Double oldStart = ((Line355) cur).getStart();
//                    ((Line355) cur).setStart(((Line355) cur).getEnd());
//                    ((Line355) cur).setEnd(oldStart);
//                }else if(p.distanceSq(((Line355) selectedShape).getEnd()) <= 100){
//                    cur = selectedShape;
//                }
//            }
        }
    }

    private void startLine(Point2D.Double p) {
        cur = new Line355();
        cur.setColor(color);
        ((Line355) cur).setStart(p);
        ((Line355) cur).setEnd(p);
    }

    private void startSquare(Point2D.Double p) {
        cur = new Square355();
        cur.setColor(color);
        ((Square355)cur).setTopLeft(p);
        cur.setCenter(p);
    }

    private void startRectangle(Point2D.Double p) {
        cur = new Rectangle355();
        cur.setColor(color);
        ((Rectangle355)cur).setTopLeft(p);
        cur.setCenter(p);
    }

    private void startCircle(Point2D.Double p) {
        cur = new Circle355();
        cur.setColor(color);
        cur.setCenter(p);
    }

    private void startEllipses(Point2D.Double p) {
        cur = new Ellipse355();
        cur.setColor(color);
        cur.setCenter(p);
    }

    public void update(Point2D.Double p){
        if(cur instanceof Line355){
            updateLine(p);
        }else if(cur instanceof Square355){
            updateSquare(p);
        }else if(cur instanceof Rectangle355){
            updateRectangle(p);
        }else if(cur instanceof Circle355){
            updateCircle(p);
        }else if(cur instanceof Ellipse355){
            updateEllipses(p);
        }

        if(selectedShape instanceof Line355){
            ((Line355) selectedShape).getStart().x = p.getX() + auxPoint0.getX();
            ((Line355) selectedShape).getStart().y = p.getY() + auxPoint0.getY();
            ((Line355) selectedShape).getEnd().x = p.getX() + auxPoint1.getX();
            ((Line355) selectedShape).getEnd().y = p.getY() + auxPoint1.getY();

        }else if(selectedShape != null && handlePoint == null){
            selectedShape.getCenter().x = p.getX()+auxPoint0.getX();
            selectedShape.getCenter().y = p.getY()+auxPoint0.getY();
        }

        if(handlePoint != null){
            double x = selectedShape.getCenter().x - p.x;
            double y = selectedShape.getCenter().y - p.y;
            selectedShape.setRotation(Math.atan2(y, x) - Math.PI / 2);
            GUIFunctions.refresh();
        }
        GUIFunctions.refresh();
    }

    private void updateLine(Point2D.Double p) {
        ((Line355) cur).setEnd(p);
    }

    private void updateSquare(Point2D.Double p) {
        double nWidth = Math.abs(p.getX() - point.getX());
        double nHeight = Math.abs(p.getY() - point.getY());
        double topX = Math.min(point.getX(), p.getX());
        double topY = Math.min(point.getY(), p.getY());
        double nSize = Math.min(nWidth,nHeight);
        if(p.getX() < point.getX()){
            topX = point.getX() - nSize;
        }
        if(p.getY() < point.getY()){
            topY = point.getY() - nSize;
        }
        Point2D.Double nTopLeft = new Point2D.Double( topX, topY);
        double centerX = nTopLeft.getX() + nSize/2;
        double centerY = nTopLeft.getY() + nSize/2;
        ((Square355)cur).setSize(nSize);
        ((Square355)cur).setTopLeft(nTopLeft);
        cur.setCenter(new Point2D.Double(centerX, centerY));
    }

    private void updateRectangle(Point2D.Double p) {
        Point2D.Double nTopLeft = new Point2D.Double( Math.min(point.getX(), p.getX()),
                                    Math.min(point.getY(), p.getY())
                                  );
        double nWidth = Math.abs(p.getX() - point.getX());
        double nHeight = Math.abs(p.getY() - point.getY());
        double centerX = nTopLeft.getX() + nWidth/2;
        double centerY = nTopLeft.getY() + nHeight/2;
        ((Rectangle355)cur).setWidth(nWidth);
        ((Rectangle355)cur).setHeight(nHeight);
        ((Rectangle355)cur).setTopLeft(nTopLeft);
        cur.setCenter(new Point2D.Double(centerX,centerY));
    }

    private void updateCircle(Point2D.Double p) {
        double topX = Math.min(point.getX(), p.getX());
        double topY = Math.min(point.getY(), p.getY());

        Point2D.Double nCenter;
        double nWidth = Math.abs(p.getX() - point.getX());
        double nHeight = Math.abs(p.getY() - point.getY());
        double nRadius = Math.min(nWidth / 2, nHeight / 2);
        if(p.getX() < point.getX()){
            topX = point.getX() - nRadius*2;
        }
        if(p.getY() < point.getY()){
            topY = point.getY() - nRadius*2;
        }
        nCenter = new Point2D.Double((topX + nRadius), (topY + nRadius));
        ((Circle355)cur).setR(nRadius);
        cur.setCenter(nCenter);
    }

    private void updateEllipses(Point2D.Double p) {
        double topX = Math.min(point.getX(), p.getX());
        double topY = Math.min(point.getY(), p.getY());
        Point2D.Double nCenter;
        double nWidth = Math.abs(p.getX() - point.getX());
        double nHeight = Math.abs(p.getY() - point.getY());
        nCenter = new Point2D.Double((topX + Math.floor(nWidth/2)), (topY + Math.floor(nHeight / 2)));
        ((Ellipse355)cur).setWidth(nWidth);
        ((Ellipse355)cur).setHeight(nHeight);
        cur.setCenter(nCenter);
    }

    public void end() {
        if(state != 5 && state != 6) {
            model.push(cur);
            modelDeque.push(cur);
            point = null;
            cur = null;
        }else if(state == 6){
            cur = null;
            auxPoint0 = null;
            auxPoint1 = null;
            handlePoint = null;
        }
    }

    public Point2D.Double worldToObject(Point2D.Double p, Shape355 s){
//        AffineTransform affine = new AffineTransform(Math.cos(s.getRotation()), -Math.sin(s.getRotation()),
//                Math.sin(s.getRotation()), Math.cos(s.getRotation()), -s.getCenter().x, -s.getCenter().y);
        if(s instanceof Line355){
            return p;
        }else {
            AffineTransform affine = new AffineTransform();
            affine.rotate(-s.getRotation());
            affine.translate(-s.getCenter().getX(), -s.getCenter().getY());


            Point2D.Double newPoint = new Point2D.Double();
            affine.transform(p, newPoint);
            return newPoint;
        }
    }

    public AffineTransform objectToWorld(Shape355 s){
        if(s instanceof Line355 || s.getCenter() == null){
            return new AffineTransform();
        }else {
            AffineTransform affine = new AffineTransform(Math.cos(s.getRotation()), Math.sin(s.getRotation()),
                    -Math.sin(s.getRotation()), Math.cos(s.getRotation()), s.getCenter().getX(), s.getCenter().getY());
            return affine;
        }
    }

    private Line355 lineHitTest(Line355 line, Point2D.Double p){
        double lineX = line.getEnd().getX() - line.getStart().getX();
        double lineY = line.getEnd().getY() - line.getStart().getY();
        double startToPointX = line.getStart().getX() - p.getX();
        double startToPointY = line.getStart().getY() - p.getY();
        double endToPointX = line.getEnd().getX() - p.getX();
        double endToPointY = line.getEnd().getY() - p.getY();

        double numerator = Math.abs(lineX * startToPointY - startToPointX * lineY);
        double denominator = Math.sqrt(Math.pow(lineX, 2) + Math.pow(lineY, 2));

        double distLine = line.getStart().distance(line.getEnd()) + LINE_ERROR;
        double distAlongLineFromStart = Math.abs(startToPointX*(lineX/distLine) + startToPointY*(lineY/distLine));
        double distAlongLineFromEnd = Math.abs(endToPointX*(lineX/distLine) + endToPointY*(lineY)/distLine);
        double totalDist = distAlongLineFromStart + distAlongLineFromEnd;
        boolean lineIsPointAndCloseToPoint = (denominator == 0 && p.distanceSq(line.getStart()) < Math.pow(LINE_ERROR,2));

        if(numerator/denominator <= LINE_ERROR || lineIsPointAndCloseToPoint) {
            if(totalDist < distLine) {
                return line;
            }
        }
        return null;
    }

    private Square355 squareHitTest(Square355 s, Point2D.Double p){
        if(Math.abs(p.getX()) <= s.getSize()/2 && Math.abs(p.getY()) <= s.getSize()/2)
            return s;
        else
            return null;
    }

    private Rectangle355 rectangleHitTest(Rectangle355 s, Point2D.Double p){
        if(Math.abs(p.getX()) <= s.getWidth()/2 && Math.abs(p.getY()) <= s.getHeight()/2)
            return s;
        else
            return null;
    }

    private Circle355 circleHitTest(Circle355 s, Point2D.Double p){
        if(p.distanceSq(0,0) <= Math.pow(s.getR(), 2))
            return s;
        else
            return null;
    }

    private Ellipse355 ellipseHitTest(Ellipse355 s, Point2D.Double p){
        if(Math.pow(p.getX()/(s.getWidth()/2),2) + Math.pow(p.getY()/(s.getHeight()/2),2) <= 1)
            return s;
        else
            return null;
    }

    private Triangle355 triangleHitTest(Triangle355 s, Point2D.Double p){
        if(sideOfLine(p,s.getP1(),s.getP2()) == sideOfLine(p,s.getP2(),s.getP3())
                && sideOfLine(p,s.getP1(),s.getP2()) == sideOfLine(p,s.getP3(),s.getP1()))
            return s;
        else
            return null;
    }

    private boolean sideOfLine(Point2D.Double testPoint, Point2D.Double p1, Point2D.Double p2){
        if((testPoint.x - p2.x) * (p1.y - p2.y) - (p1.x - p2.x) * (testPoint.y - p2.y)<0)
            return true;
        else
            return false;
    }

    private void shapeButtonHit(){
        selectedShape = null;
        GUIFunctions.refresh();
    }

    @Override
    public void colorButtonHit(Color c) {
        color = c;
        GUIFunctions.changeSelectedColor(color);
        if(selectedShape != null){
            selectedShape.setColor(color);
        }
        GUIFunctions.refresh();
    }

    @Override
    public void triangleButtonHit() {
        state = 5;
        shapeButtonHit();
    }

    @Override
    public void squareButtonHit() {
        state = 1;
        shapeButtonHit();
    }

    @Override
    public void rectangleButtonHit() {
        state = 2;
        shapeButtonHit();
    }

    @Override
    public void circleButtonHit() {
        state = 3;
        shapeButtonHit();
    }

    @Override
    public void ellipseButtonHit() {
        state = 4;
        shapeButtonHit();
    }

    @Override
    public void lineButtonHit() {
        state = 0;
        shapeButtonHit();
    }

    @Override
    public void selectButtonHit() {
        state = 6;
        shapeButtonHit();
    }

    @Override
    public void zoomInButtonHit() {

    }

    @Override
    public void zoomOutButtonHit() {

    }

    @Override
    public void hScrollbarChanged(int value) {

    }

    @Override
    public void vScrollbarChanged(int value) {

    }

    @Override
    public void toggle3DModelDisplay() {

    }

    @Override
    public void keyPressed(Iterator<Integer> iterator) {

    }

    @Override
    public void doEdgeDetection() {

    }

    @Override
    public void doSharpen() {

    }

    @Override
    public void doMedianBlur() {

    }

    @Override
    public void doUniformBlur() {

    }

    @Override
    public void doChangeContrast(int contrastAmountNum) {

    }

    @Override
    public void doChangeBrightness(int brightnessAmountNum) {

    }

    @Override
    public void doLoadImage(BufferedImage openImage) {

    }

    @Override
    public void toggleBackgroundDisplay() {

    }
}


package cs355.solution;

import cs355.CS355Controller;
import cs355.GUIFunctions;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.event.KeyEvent;
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
    public int triPointSelected = -1;
    public Shape355 cur = null;
    public Shape355 selectedShape = null;

    public double zoomLevel = .25;
    public int viewSize = 128;

    public Point2D.Double oldCenter = null;

    public static int LINE_ERROR = 4;
    public static double HANDLE_LENGTH = 30;

    private float step = 1;
    private static volatile Controller instance = null;

    public Camera355 getCamera() {
        return camera;
    }

    private Camera355 camera = new Camera355();
    private double horizontal = viewSize;
    private double vertical = viewSize;
    public boolean displayHouse = false;
    private Point2D.Double startPoint = null;
    private Point2D.Double auxPoint0 = null;
    private Point2D.Double auxPoint1 = null;
    private Point2D.Double handlePoint = null;
    private static boolean UPDATING = false;

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
        p = viewToWorld(p);
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
        cur = null;
        startPoint = null;
        auxPoint0 = null;
        auxPoint1 = null;
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
        if(startPoint == null){
            startPoint = viewToWorld(p);
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
            startPoint = null;
            auxPoint0 = null;
            auxPoint1 = null;
            cur = null;
        }else{
            System.out.println("Should never get here...");
        }
    }

    public void start(Point2D.Double p){
        p = viewToWorld(p);
        if(state != 5 && state != 6){
            startPoint = p;
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
            checkHandleSelected(p);
            checkResizeSelected(p);

            if(handlePoint==null && cur==null)
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

    private void checkResizeSelected(Point2D.Double p) {
        if(selectedShape != null) {
            Point2D.Double pPrime = worldToObject(p, selectedShape);
            ArrayList<Point2D.Double> corners = new ArrayList<Point2D.Double>();

            if (selectedShape instanceof Line355) {
                checkLineUpdate(p);
            } else if (selectedShape instanceof Square355) {
                double x = (0 - ((Square355) selectedShape).getSize() / 2);
                double width = ((Square355) selectedShape).getSize();
                double y = (0 - ((Square355) selectedShape).getSize() / 2);
                double height = ((Square355) selectedShape).getSize();
                corners.add(new Point2D.Double(x, y));
                corners.add(new Point2D.Double(x + width, y));
                corners.add(new Point2D.Double(x, y + height));
                corners.add(new Point2D.Double(x + width, y + height));
            } else if (selectedShape instanceof Rectangle355) {
                double x = 0 - ((Rectangle355) selectedShape).getWidth() / 2;
                double width = ((Rectangle355) selectedShape).getWidth();
                double y = (0 - ((Rectangle355) selectedShape).getHeight() / 2);
                double height = ((Rectangle355) selectedShape).getHeight();
                corners.add(new Point2D.Double(x, y));
                corners.add(new Point2D.Double(x + width, y));
                corners.add(new Point2D.Double(x, y + height));
                corners.add(new Point2D.Double(x + width, y + height));
            } else if (selectedShape instanceof Circle355) {
                double x = 0;
                double width = (((Circle355) selectedShape).getR() * 2);
                double y = 0;
                double height = (((Circle355) selectedShape).getR() * 2);
                x -= width / 2;
                y -= height / 2;
                corners.add(new Point2D.Double(x, y));
                corners.add(new Point2D.Double(x + width, y));
                corners.add(new Point2D.Double(x, y + height));
                corners.add(new Point2D.Double(x + width, y + height));
            } else if (selectedShape instanceof Ellipse355) {
                double x = 0;
                double width = (int) ((Ellipse355) selectedShape).getWidth();
                double y = 0;
                double height = (int) ((Ellipse355) selectedShape).getHeight();
                x -= width / 2;
                y -= height / 2;
                corners.add(new Point2D.Double(x, y));
                corners.add(new Point2D.Double(x + width, y));
                corners.add(new Point2D.Double(x, y + height));
                corners.add(new Point2D.Double(x + width, y + height));
            } else if (selectedShape instanceof Triangle355) {
                corners.add(((Triangle355) selectedShape).getP1());
                corners.add(((Triangle355) selectedShape).getP2());
                corners.add(((Triangle355) selectedShape).getP3());

            }
            if (selectedShape instanceof Triangle355) {
                int i = 0;
                for (Point2D.Double corner : corners) {
                    i++;
                    if (corner.distanceSq(pPrime) < 101) {
                        cur = selectedShape;
                        triPointSelected = i;
                        UPDATING = true;
                    }
                }
            } else if (selectedShape instanceof Line355) {
            } else {
                Point2D.Double tempPoint = corners.get(0);
                for (Point2D.Double corner : corners) {
                    if (corner.distanceSq(pPrime) < 101) {
                        cur = selectedShape;
                        startPoint = new Point2D.Double(corner.getX(), corner.getY());
                        for (Point2D.Double opositeCorner : corners) {
                            if (opositeCorner.distanceSq(pPrime) > tempPoint.distanceSq(pPrime)) {
                                tempPoint = opositeCorner;
                            }
                        }
                        UPDATING = true;
                    }
                }
                if (UPDATING) {
                    startPoint = new Point2D.Double();
                    AffineTransform objToWor = objectToWorld(selectedShape);
                    objToWor.transform(tempPoint, startPoint);
                }
            }
        }
//        System.out.println(triPointSelected);
    }

    private void checkLineUpdate(Point2D.Double p) {
        if(p.distanceSq(((Line355) selectedShape).getStart()) <= 100){
            cur = selectedShape;
            Point2D.Double oldStart = ((Line355) cur).getStart();
            ((Line355) cur).setStart(((Line355) cur).getEnd());
            ((Line355) cur).setEnd(oldStart);
            UPDATING = true;
        }else if(p.distanceSq(((Line355) selectedShape).getEnd()) <= 100){
            cur = selectedShape;
            UPDATING = true;
        }
    }

    private void checkHandleSelected(Point2D.Double p) {
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
            }else if(selectedShape instanceof Rectangle355){
                int x = 0 - (int)((Rectangle355) selectedShape).getWidth()/2;
                int width = (int)((Rectangle355) selectedShape).getWidth();
                int y = (int)(0 - ((Rectangle355) selectedShape).getHeight()/2);
                int height = (int)((Rectangle355) selectedShape).getHeight();
                handle = new Point2D.Double(x+width/2,y-HANDLE_LENGTH);
            }else if(selectedShape instanceof Circle355){
                int x = 0;
                int width = (int)(((Circle355) selectedShape).getR()*2);
                int y = 0;
                int height = (int)(((Circle355) selectedShape).getR()*2);
                x -= width/2;
                y -= height/2;
                handle = new Point2D.Double(x+width/2,y-HANDLE_LENGTH);
            }else if(selectedShape instanceof Ellipse355){
                int x = 0;
                int width = (int)((Ellipse355) selectedShape).getWidth();
                int y = 0;
                int height = (int)((Ellipse355) selectedShape).getHeight();
                x -= width/2;
                y -= height/2;
                handle = new Point2D.Double(x+width/2,y-HANDLE_LENGTH);
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
                }
            }
            if(handle != null && handle.distanceSq(pPrime) <= Math.pow(10 * 1/zoomLevel, 2)){
                handlePoint = p;
            }
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
        cur.setCenter(p);
    }

    private void startRectangle(Point2D.Double p) {
        cur = new Rectangle355();
        cur.setColor(color);
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
        p = viewToWorld(p);
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
        }else if(cur instanceof Triangle355){
            updateTriangle(p);
        }

        if(!UPDATING) {
            if (selectedShape instanceof Line355) {
                ((Line355) selectedShape).getStart().x = p.getX() + auxPoint0.getX();
                ((Line355) selectedShape).getStart().y = p.getY() + auxPoint0.getY();
                ((Line355) selectedShape).getEnd().x = p.getX() + auxPoint1.getX();
                ((Line355) selectedShape).getEnd().y = p.getY() + auxPoint1.getY();

            } else if (selectedShape != null && handlePoint == null) {
                selectedShape.getCenter().x = p.getX() + auxPoint0.getX();
                selectedShape.getCenter().y = p.getY() + auxPoint0.getY();
            }
        }

        if(handlePoint != null){
            double x = selectedShape.getCenter().x - p.x;
            double y = selectedShape.getCenter().y - p.y;
            selectedShape.setRotation(Math.atan2(y, x) - Math.PI / 2);
            GUIFunctions.refresh();
        }
        GUIFunctions.refresh();
    }

    private void updateTriangle(Point2D.Double p) {
        Point2D.Double pPrime = worldToObject(p, cur);
        if(triPointSelected == 1){
            ((Triangle355) cur).setP1(pPrime);
        }else if(triPointSelected==2){
            ((Triangle355) cur).setP2(pPrime);
        }else{
            ((Triangle355) cur).setP3(pPrime);
        }
    }

    private void updateLine(Point2D.Double p) {
        ((Line355) cur).setEnd(p);
    }

    private void updateSquare(Point2D.Double p) {
        double nWidth = Math.abs(p.getX() - startPoint.getX());
        double nHeight = Math.abs(p.getY() - startPoint.getY());
        double topX = Math.min(startPoint.getX(), p.getX());
        double topY = Math.min(startPoint.getY(), p.getY());
        double nSize = Math.min(nWidth,nHeight);
        if(p.getX() < startPoint.getX()){
            topX = startPoint.getX() - nSize;
        }
        if(p.getY() < startPoint.getY()){
            topY = startPoint.getY() - nSize;
        }
        Point2D.Double nTopLeft = new Point2D.Double( topX, topY);
        double centerX = nTopLeft.getX() + nSize/2;
        double centerY = nTopLeft.getY() + nSize/2;
        ((Square355)cur).setSize(nSize);
        cur.setCenter(new Point2D.Double(centerX, centerY));
    }

    private void updateRectangle(Point2D.Double p) {
        double topX = Math.min(startPoint.getX(), p.getX());
        double topY = Math.min(startPoint.getY(), p.getY());
        Point2D.Double nCenter;
        double nWidth = Math.abs(p.getX() - startPoint.getX());
        double nHeight = Math.abs(p.getY() - startPoint.getY());
        nCenter = new Point2D.Double((topX + Math.floor(nWidth/2)), (topY + Math.floor(nHeight / 2)));
        ((Rectangle355)cur).setWidth(nWidth);
        ((Rectangle355)cur).setHeight(nHeight);
        cur.setCenter(nCenter);
    }

    private void updateCircle(Point2D.Double p) {
        double topX = Math.min(startPoint.getX(), p.getX());
        double topY = Math.min(startPoint.getY(), p.getY());

        Point2D.Double nCenter;
        double nWidth = Math.abs(p.getX() - startPoint.getX());
        double nHeight = Math.abs(p.getY() - startPoint.getY());
        double nRadius = Math.min(nWidth / 2, nHeight / 2);
        if(p.getX() < startPoint.getX()){
            topX = startPoint.getX() - nRadius*2;
        }
        if(p.getY() < startPoint.getY()){
            topY = startPoint.getY() - nRadius*2;
        }
        nCenter = new Point2D.Double((topX + nRadius), (topY + nRadius));
        ((Circle355)cur).setR(nRadius);
        cur.setCenter(nCenter);
    }

    private void updateEllipses(Point2D.Double p) {
        double topX = Math.min(startPoint.getX(), p.getX());
        double topY = Math.min(startPoint.getY(), p.getY());
        Point2D.Double nCenter;
        double nWidth = Math.abs(p.getX() - startPoint.getX());
        double nHeight = Math.abs(p.getY() - startPoint.getY());
        nCenter = new Point2D.Double((topX + Math.floor(nWidth/2)), (topY + Math.floor(nHeight / 2)));
        ((Ellipse355)cur).setWidth(nWidth);
        ((Ellipse355)cur).setHeight(nHeight);
        cur.setCenter(nCenter);
    }

    public void end() {
        if(state != 5 && state != 6) {
            model.push(cur);
            modelDeque.push(cur);
            startPoint = null;
            cur = null;
        }else if(state == 6){
            cur = null;
            auxPoint0 = null;
            auxPoint1 = null;
            handlePoint = null;
            startPoint = null;
            UPDATING = false;
        }
    }

    public Point2D.Double worldToObject(Point2D.Double p, Shape355 s){
//        AffineTransform affine = new AffineTransform(Math.cos(s.getRotation()), -Math.sin(s.getRotation()),
//                Math.sin(s.getRotation()), Math.cos(s.getRotation()), -s.getCenter().x, -s.getCenter().y);
        if(s instanceof Line355){
            return p;
        }else {
            double theta = s.getRotation();
            AffineTransform affine = new AffineTransform(Math.cos(theta),-Math.sin(theta),
                    Math.sin(theta),Math.cos(theta),
                    (-Math.cos(theta) * s.getCenter().getX() - Math.sin(theta) * s.getCenter().getY()),
                    (Math.sin(theta) * s.getCenter().getX() - Math.cos(theta) * s.getCenter().getY()));
            Point2D.Double newPoint = new Point2D.Double();
            affine.transform(p,newPoint);
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

    public AffineTransform worldToView(){
        AffineTransform zoomTransform = new AffineTransform(zoomLevel,0,0,zoomLevel,-getHor() * zoomLevel,-getVert() * zoomLevel);
        return zoomTransform;
    }

    public Point2D.Double viewToWorld(Point2D.Double p){
        AffineTransform affine = new AffineTransform(1/zoomLevel,0.0d,0.0d,1/zoomLevel,horizontal,vertical);
        Point2D.Double newPoint = new Point2D.Double();
        affine.transform(p, newPoint);
        return newPoint;
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

        if(zoomLevel < 4){
            zoomLevel *= 2;
            viewSize *= 2;
        }
//        System.out.println(zoomLevel);
        GUIFunctions.setHScrollBarMax(viewSize);
        GUIFunctions.setVScrollBarMax(viewSize);
        GUIFunctions.refresh();
    }

    @Override
    public void zoomOutButtonHit() {
        if(zoomLevel > .25){
            zoomLevel /= 2;
            viewSize /= 2;
        }
//        System.out.println(zoomLevel);
        GUIFunctions.setHScrollBarMax(viewSize);
        GUIFunctions.setVScrollBarMax(viewSize);
        GUIFunctions.refresh();
    }

    public double getZoom(){
        return zoomLevel;
    }

    @Override
    public void hScrollbarChanged(int value) {
        horizontal = value;
        GUIFunctions.refresh();
    }

    public double getHor(){
        return horizontal;
    }

    @Override
    public void vScrollbarChanged(int value) {
        vertical = value;
        GUIFunctions.refresh();
    }

    public double getVert(){
        return vertical;
    }


    @Override
    public void toggle3DModelDisplay()
    {
        displayHouse = !displayHouse;
        GUIFunctions.refresh();
    }



    @Override
    public void keyPressed(Iterator<Integer> iterator)
    {
        if(displayHouse)
        {
            while (iterator.hasNext())
            {
                switch(iterator.next())
                {
                    case KeyEvent.VK_W:
                        camera.walkForward(step);
                        break;

                    case KeyEvent.VK_A:
                        camera.sidestep(-step);
                        break;

                    case KeyEvent.VK_S:
                        camera.walkBackward(step);
                        break;

                    case KeyEvent.VK_D:
                        camera.sidestep(step);
                        break;

                    case KeyEvent.VK_Q:
                        camera.changeZRotationBy(step);
                        break;

                    case KeyEvent.VK_E:
                        camera.changeZRotationBy(-step);
                        break;

                    case KeyEvent.VK_R:
                        camera.changeYBy(step);
                        break;

                    case KeyEvent.VK_F:
                        camera.changeYBy(-step);
                        break;

                    case KeyEvent.VK_H:
                        camera = new Camera355();
                        break;
                }
            }
        }
        GUIFunctions.refresh();
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




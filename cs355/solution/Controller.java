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
    private static volatile Controller instance = null;
    private Point2D.Double initialPoint = null;
    private Point2D.Double triPoint2 = null;
    private Point2D.Double triPoint3 = null;

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
            case 6:
                checkIfShapeSelected(p);
            default:
                break;
        }
    }

    private void checkIfShapeSelected(Point2D.Double p) {
        this.cur = null;
        this.initialPoint = null;
        this.triPoint2 = null;
        this.triPoint3 = null;
        for(Shape355 s: modelDeque){
            if(s instanceof Line355){
                selectedShape = lineHitTest((Line355)s, p);
            }else if(s instanceof Square355){
                selectedShape = s;
            }else if(s instanceof Rectangle355){

            }else if(s instanceof Circle355){

            }else if(s instanceof Ellipse355){

            }else if(s instanceof Triangle355){

            }

            if (selectedShape != null) break;
        }
        GUIFunctions.refresh();

    }

    private Point2D.Double worldToObject(Point2D.Double p, Shape355 s){
        AffineTransform affine = new AffineTransform(Math.cos(s.getRotation()),- Math.sin(s.getRotation()),
                Math.sin(s.getRotation()), Math.cos(s.getRotation()), -s.getCenter().x, -s.getCenter().y);
        Point2D.Double newPoint = new Point2D.Double();
        affine.transform(p, newPoint);
        return newPoint;
    }

    private Line355 lineHitTest(Line355 line, Point2D.Double p){
        double numerator =Math.abs((line.getEnd().getX() - line.getStart().getX()) * (line.getStart().getY() - p.getY())
                - (line.getStart().getX() - p.getX()) * (line.getEnd().getY() - line.getStart().getY()));
        double denominator = Math.sqrt(Math.pow(line.getEnd().getX() - line.getStart().getX(), 2) +
                Math.pow(line.getEnd().getY() - line.getStart().getY(), 2));
        if(numerator/denominator <=4) return line;
        else return null;
    }

    private void triangle(Point2D.Double p){
        if(initialPoint == null){
            initialPoint = p;
            cur = new Triangle355();
            cur.setColor(color);
            ((Triangle355)cur).setP1(p);
//            System.out.println("p1");
        }else if(triPoint2 == null){
            triPoint2 = p;
            ((Triangle355)cur).setP2(p);
//            System.out.println("p2");
        }else if(triPoint3 == null){
            triPoint3 = p;
            ((Triangle355)cur).setP3(p);
            double totalX = ((Triangle355) cur).getP1().getX() + ((Triangle355) cur).getP2().getX() + ((Triangle355) cur).getP3().getX();
            double averageX = totalX / 3;
            double totalY = ((Triangle355) cur).getP1().getY() + ((Triangle355) cur).getP2().getY() + ((Triangle355) cur).getP3().getY();
            double averageY = totalY / 3;
            cur.setCenter(new Point2D.Double(averageX, averageY));
            ((Triangle355) cur).setP1(new Point2D.Double((((Triangle355) cur).getP1().getX() - cur.getCenter().getX()), (((Triangle355) cur).getP1().getY() - cur.getCenter().getY())));
            ((Triangle355) cur).setP2(new Point2D.Double((((Triangle355) cur).getP2().getX() - cur.getCenter().getX()), (((Triangle355) cur).getP2().getY() - cur.getCenter().getY())));
            ((Triangle355) cur).setP3(new Point2D.Double((((Triangle355) cur).getP3().getX() - cur.getCenter().getX()), (((Triangle355) cur).getP3().getY() - cur.getCenter().getY())));
//            System.out.println("p3");
            model.push(cur);
            modelDeque.push(cur);
            System.out.println(model.size() + " : " + modelDeque.size());
            initialPoint = null;
            triPoint2 = null;
            triPoint3 = null;
            cur = null;
            GUIFunctions.refresh();
        }else{
            System.out.println("Should never get here...");
        }
    }

    public void start(Point2D.Double p){
        if(state != 5 && state != 6) initialPoint = p;
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
        if(state != 5 && state != 6) GUIFunctions.refresh();
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
        switch(state){
            case 0:
                updateLine(p);
                break;
            case 1:
                updateSquare(p);
                break;
            case 2:
                updateRectangle(p);
                break;
            case 3:
                updateCircle(p);
                break;
            case 4:
                updateEllipses(p);
                break;
            default:
                break;
        }
        GUIFunctions.refresh();
    }

    private void updateLine(Point2D.Double p) {
        ((Line355) cur).setEnd(p);
    }

    private void updateSquare(Point2D.Double p) {
        double nWidth = Math.abs(p.getX() - initialPoint.getX());
        double nHeight = Math.abs(p.getY() - initialPoint.getY());
        double topX = Math.min(initialPoint.getX(), p.getX());
        double topY = Math.min(initialPoint.getY(), p.getY());
        double nSize = Math.min(nWidth,nHeight);
        if(p.getX() < initialPoint.getX()){
            topX = initialPoint.getX() - nSize;
        }
        if(p.getY() < initialPoint.getY()){
            topY = initialPoint.getY() - nSize;
        }
        Point2D.Double nTopLeft = new Point2D.Double( topX, topY);
        double centerX = nTopLeft.getX() + nSize/2;
        double centerY = nTopLeft.getY() + nSize/2;
        ((Square355)cur).setSize(nSize);
        ((Square355)cur).setTopLeft(nTopLeft);
        cur.setCenter(new Point2D.Double(centerX, centerY));
    }

    private void updateRectangle(Point2D.Double p) {
        Point2D.Double nTopLeft = new Point2D.Double( Math.min(initialPoint.getX(), p.getX()),
                                    Math.min(initialPoint.getY(), p.getY())
                                  );
        double nWidth = Math.abs(p.getX() - initialPoint.getX());
        double nHeight = Math.abs(p.getY() - initialPoint.getY());
        double centerX = nTopLeft.getX() + nWidth/2;
        double centerY = nTopLeft.getY() + nHeight/2;
        ((Rectangle355)cur).setWidth(nWidth);
        ((Rectangle355)cur).setHeight(nHeight);
        ((Rectangle355)cur).setTopLeft(nTopLeft);
        cur.setCenter(new Point2D.Double(centerX,centerY));
    }

    private void updateCircle(Point2D.Double p) {
        double topX = Math.min(initialPoint.getX(), p.getX());
        double topY = Math.min(initialPoint.getY(), p.getY());

        Point2D.Double nCenter;
        double nWidth = Math.abs(p.getX() - initialPoint.getX());
        double nHeight = Math.abs(p.getY() - initialPoint.getY());
        double nRadius = Math.min(nWidth / 2, nHeight / 2);
        if(p.getX() < initialPoint.getX()){
            topX = initialPoint.getX() - nRadius*2;
        }
        if(p.getY() < initialPoint.getY()){
            topY = initialPoint.getY() - nRadius*2;
        }
        nCenter = new Point2D.Double((topX + nRadius), (topY + nRadius));
        ((Circle355)cur).setR(nRadius);
        cur.setCenter(nCenter);
    }

    private void updateEllipses(Point2D.Double p) {
        double topX = Math.min(initialPoint.getX(), p.getX());
        double topY = Math.min(initialPoint.getY(), p.getY());
        Point2D.Double nCenter;
        double nWidth = Math.abs(p.getX() - initialPoint.getX());
        double nHeight = Math.abs(p.getY() - initialPoint.getY());
        nCenter = new Point2D.Double((topX + Math.floor(nWidth/2)), (topY + Math.floor(nHeight / 2)));
        ((Ellipse355)cur).setWidth(nWidth);
        ((Ellipse355)cur).setHeight(nHeight);
        cur.setCenter(nCenter);
    }

    public void end() {
        if(state != 5 && state != 6) {
            model.push(cur);
            modelDeque.push(cur);
            System.out.println(model.size() + " : " + modelDeque.size());
            initialPoint = null;
            cur = null;
        }
    }

    @Override
    public void colorButtonHit(Color c) {
        color = c;
        GUIFunctions.changeSelectedColor(color);
//        System.out.println(c);
    }

    @Override
    public void triangleButtonHit() {
        state = 5;
//        System.out.println(state);
    }

    @Override
    public void squareButtonHit() {
        state = 1;
//        System.out.println(state);
    }

    @Override
    public void rectangleButtonHit() {
        state = 2;
//        System.out.println(state);
    }

    @Override
    public void circleButtonHit() {
        state = 3;
//        System.out.println(state);
    }

    @Override
    public void ellipseButtonHit() {
        state = 4;
//        System.out.println(state);
    }

    @Override
    public void lineButtonHit() {
        state = 0;
//        System.out.println(state);
    }

    @Override
    public void selectButtonHit() {
        state = 6;
        System.out.println(state);
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


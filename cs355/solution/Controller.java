package cs355.solution;

import cs355.CS355Controller;
import cs355.GUIFunctions;

import java.awt.*;
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
    public Color color;
//    0 = line
//    1 = square
//    2 = rectangle
//    3 = circle
//    4 = ellipses
//    5 = triangel
    public int state = 0;
    public Shape355 cur = null;
    private static volatile Controller instance = null;
    private Point initialPoint = null;
    private Point triPoint2 = null;
    private Point triPoint3 = null;

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

    public void clickAt(Point p){
        switch (state){
            case 5:
                triangle(p);
                break;
            default:
                break;
        }
    }
    private void triangle(Point p){
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
//            System.out.println("p3");
            model.push(cur);
            initialPoint = null;
            triPoint2 = null;
            triPoint3 = null;
            cur = null;
            GUIFunctions.refresh();
        }else{
            System.out.println("Should never get here...");
        }
    }

    public void start(Point p){
        if(state !=5) initialPoint = p;
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
    }

    private void startLine(Point p) {
        cur = new Line355();
        cur.setColor(color);
        ((Line355) cur).setStart(p);
        ((Line355) cur).setEnd(p);
    }

    private void startSquare(Point p) {
        cur = new Square355();
        cur.setColor(color);
        ((Square355)cur).setTopLeft(p);
    }

    private void startRectangle(Point p) {
        cur = new Rectangle355();
        cur.setColor(color);
        ((Rectangle355)cur).setTopLeft(p);
    }

    private void startCircle(Point p) {
        cur = new Circle355();
        cur.setColor(color);
        ((Circle355) cur).setCenter(p);
    }

    private void startEllipses(Point p) {
        cur = new Ellipse355();
        cur.setColor(color);
        ((Ellipse355) cur).setCenter(p);
    }

    public void update(Point p){
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

    private void updateLine(Point p) {
        ((Line355) cur).setEnd(p);
    }

    private void updateSquare(Point p) {
        int nWidth = (int)Math.abs(p.getX() - initialPoint.getX());
        int nHeight = (int)Math.abs(p.getY() - initialPoint.getY());
        int topX = (int)Math.min(initialPoint.getX(), p.getX());
        int topY = (int)Math.min(initialPoint.getY(), p.getY());
        int nSize = Math.min(nWidth,nHeight);
        if(p.getX() < initialPoint.getX()){
            topX = (int)initialPoint.getX() - nSize;
        }
        if(p.getY() < initialPoint.getY()){
            topY = (int)initialPoint.getY() - nSize;
        }
        Point nTopLeft = new Point( topX, topY);
        ((Square355)cur).setSize(nSize);
        ((Square355)cur).setTopLeft(nTopLeft);
    }

    private void updateRectangle(Point p) {
        Point nTopLeft = new Point( (int)Math.min(initialPoint.getX(), p.getX()),
                                    (int)Math.min(initialPoint.getY(), p.getY())
                                  );
        int nWidth = (int)Math.abs(p.getX() - initialPoint.getX());
        int nHeight = (int)Math.abs(p.getY() - initialPoint.getY());
        ((Rectangle355)cur).setWidth(nWidth);
        ((Rectangle355)cur).setHeight(nHeight);
        ((Rectangle355)cur).setTopLeft(nTopLeft);
    }

    private void updateCircle(Point p) {
        int topX = (int)Math.min(initialPoint.getX(), p.getX());
        int topY = (int)Math.min(initialPoint.getY(), p.getY());

        Point nCenter;
        int nWidth = (int)Math.abs(p.getX() - initialPoint.getX());
        int nHeight = (int)Math.abs(p.getY() - initialPoint.getY());
        int nRadius = Math.min(nWidth / 2, nHeight / 2);
        if(p.getX() < initialPoint.getX()){
            topX = (int)initialPoint.getX() - nRadius*2;
        }
        if(p.getY() < initialPoint.getY()){
            topY = (int)initialPoint.getY() - nRadius*2;
        }
        nCenter = new Point((int)(topX + nRadius), (int)(topY + nRadius));
        ((Circle355)cur).setR(nRadius);
        ((Circle355)cur).setCenter(nCenter);
    }

    private void updateEllipses(Point p) {
        int topX = (int)Math.min(initialPoint.getX(), p.getX());
        int topY = (int)Math.min(initialPoint.getY(), p.getY());
        Point nCenter;
        int nWidth = (int)Math.abs(p.getX() - initialPoint.getX());
        int nHeight = (int)Math.abs(p.getY() - initialPoint.getY());
        nCenter = new Point((int)(topX + Math.floor(nWidth/2)), (int)(topY + Math.floor(nHeight / 2)));
        ((Ellipse355)cur).setWidth(nWidth);
        ((Ellipse355)cur).setHeight(nHeight);
        ((Ellipse355)cur).setCenter(nCenter);
    }

    public void end() {
        if(state != 5) {
            model.push(cur);
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


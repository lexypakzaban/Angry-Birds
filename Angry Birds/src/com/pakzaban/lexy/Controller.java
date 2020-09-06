package com.pakzaban.lexy;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class Controller {
    public Pane graphPane;
    public Label attemptsLabel;

    private double targetHeight = 310;
    private Circle c;
    private double velX;
    private double velY;
    private double t;
    private double startX;
    private double startY;
    private AnimationTimer at;
    private Rectangle r3;
    private Rectangle r4;
    private ImageView pigView;
    private Image happyPig;
    private Image sadPig;
    private int attempts = 0;
    private double targetVel = -2;

    public void initialize(){
        graphPane.getChildren().clear();
        velX = 10;
        velY = -10;
        t = 0;
        startX = 0;
        startY = 0;
        c = new Circle(100,500, 15, Color.MAGENTA);
        Rectangle r1 = new Rectangle(80,510,40,5);
        Rectangle r2 = new Rectangle(97,515,6,185);
        r3 = new Rectangle(560,targetHeight,80,5);
        r4 = new Rectangle(597,targetHeight + 5, 6, 695-targetHeight);
        r1.setFill(Color.DARKTURQUOISE);
        r2.setFill(Color.DARKTURQUOISE);
        r3.setFill(Color.DARKTURQUOISE);
        r4.setFill(Color.DARKTURQUOISE);
        happyPig = new Image("happyPig.png");
        sadPig = new Image("sadPig.png");
        pigView = new ImageView(happyPig);
        pigView.setFitHeight(r3.getWidth());
        pigView.setPreserveRatio(true);
        pigView.setX(r3.getX());
        pigView.setY(targetHeight - pigView.getFitHeight());
        graphPane.getChildren().addAll(c,r1,r2,r3,r4,pigView);
    }

    public void shoot(){
        double g = 2;
        at = new AnimationTimer() {
            @Override
            public void handle(long l) {
                t+=0.1;
                c.setCenterX(c.getCenterX() + velX);
                c.setCenterY(c.getCenterY() + velY + 0.5 * g * Math.pow(t,2));
                targetHeight += targetVel;
                r3.setY(targetHeight);
                r4.setHeight(graphPane.getHeight() - targetHeight);
                r4.setY(targetHeight + 5);
                pigView.setY(targetHeight - r3.getWidth());

                if(pigView.getY() <= 0 || targetHeight >= graphPane.getHeight()){
                    targetVel = - targetVel;
                }


                if (c.getCenterX() + c.getRadius() >= graphPane.getWidth() + 50){
                    velX = -velX;
                }

                if (c.getCenterY() + c.getRadius() >= graphPane.getHeight()){
                    at.stop();
                    graphPane.getChildren().remove(c);
                    graphPane.getChildren().remove(pigView);
                    attempts++;
                    attemptsLabel.setText("Attempts: " + attempts);
                    initialize();
                }

                if (c.getCenterX() >= r3.getX() + 50 &&
                c.getCenterX() <= r3.getX() + r3.getWidth() + 50 &&
                c.getCenterY() + c.getRadius() <= r3.getY() &&
                c.getCenterY() + c.getRadius() >= r3.getY() - r3.getWidth()){
                    at.stop();
                    graphPane.getChildren().remove(c);
                    pigView.setImage(sadPig);
                }
            }
        };
        at.start();
    }

    public void mouseMoved(MouseEvent mouseEvent){
        double x = mouseEvent.getSceneX() - 50;
        double y = mouseEvent.getSceneY() - 50;
        if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED) &&
                Math.abs(x - c.getCenterX()) < c.getRadius() &&
                Math.abs(y - c.getCenterY()) < c.getRadius()){
            startX = x;
            startY = y;
            System.out.println(startX + ", " + startY);
        }

        else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
            c.setTranslateX((x - startX)/3);
            c.setTranslateY((y - startY)/3);

        }

        else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
            velX = -(x - startX)/10;
            velY = -(y - startY)/10;
            shoot();

        }
    }

    public void replay(){
        attempts = 0;
        graphPane.getChildren().remove(pigView);
        initialize();
    }

}

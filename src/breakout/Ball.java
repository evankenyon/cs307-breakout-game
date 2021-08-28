package breakout;

import javafx.scene.shape.Circle;
import java.util.Random;

public class Ball extends Circle {

    private double speedX;
    private double speedY;
    private double angle;
    private Random rand;

    public Ball(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        rand = new Random();
        angle = rand.nextDouble() * 2 * Math.PI;
        speedX = 100*Math.cos(angle);
        speedY = 100*Math.sin(angle);
    }


    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

}

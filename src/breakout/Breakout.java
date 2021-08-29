package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FROM EXAMPLE_ANIMATION: A basic example JavaFX program for the first lab.
 *
 * @author Evan Kenyon
 */
public class Breakout extends Application {

    // size, frames per second, second delay, background, highlight, offset amount, mover, paddle color, and paddle color
    // borrowed from example_animation in course gitlab
    public static final String TITLE = "Breakout Game";
    public static final int SIZE = 800;
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
    public static final int OFFSET_PADDLE_AMOUNT = 50;
    public static final int OFFSET_BALL_AMOUNT = OFFSET_PADDLE_AMOUNT + 100;
    public static final int PADDLE_SPEED = 25;
    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 20;
    public static final double BALL_CENTER_X = 200;
    public static final double BALL_CENTER_Y = 140;
    public static final double BALL_RADIUS = 15;
    public static final Paint PADDLE_COLOR = Color.BISQUE;

    private Scene mainScene;
    private Scene gameOverScene;
    private Scene winScene;
    private Rectangle paddle;
    private Ball ball;
    private Bricks bricks;
    private int lives;
    Stage primaryStage;
    Group primaryRoot;


    /**
     * Borrowed from example_animation in course gitlab
     */
    @Override
    public void start (Stage stage) {
        mainScene = setupGame(SIZE, SIZE, BACKGROUND);
        gameOverScene = setupTextScene(SIZE, SIZE, BACKGROUND, "Game Over");
        winScene = setupTextScene(SIZE, SIZE, BACKGROUND, "You win!");
        lives = 3;
        primaryStage = stage;
        primaryStage.setScene(mainScene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
        animation.play();
    }

    private Scene setupGame (int width, int height, Paint background) {
        // Rectangle constructor parameters from example_animation in course gitlab
        paddle = new Rectangle(width / 2 - PADDLE_WIDTH / 2, height - OFFSET_PADDLE_AMOUNT, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Ball(width/2, height - OFFSET_BALL_AMOUNT, BALL_RADIUS);
        bricks = new Bricks(SIZE, SIZE, 100, 100, 0.1);
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(paddle, ball, bricks);
        primaryRoot = root;
        return setupScene(root, width, height, background);
    }

    private Scene setupTextScene(int width, int height, Paint background, String message) {
        // Text construction was borrowed from https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
        Text text = new Text(message);
        text.setFont(Font.font ("Verdana", 20));
        text.setX(50);
        text.setY(50);
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(text);
        return setupScene(root, width, height, background);
    }

    private Scene setupScene(Group root, int width, int height, Paint background) {
        Scene scene = new Scene(root, width, height, background);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void step (double elapsedTime) {
        handlePaddleIntersectingBounds();
        handleBallIntersectingBounds();
        handleBallIntersectingPaddle();
        updateBallPosition(elapsedTime);
        handleBallIntersectingBrick();
        handleNoBricksRemaining();
    }

    // Borrowed from example_animation in course gitlab
    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case RIGHT -> paddle.setX(paddle.getX() + PADDLE_SPEED);
            case LEFT -> paddle.setX(paddle.getX() - PADDLE_SPEED);
        }
    }

    // Borrowed from example_animation in course gitlab
    private boolean isIntersecting (Shape a, Shape b) {
        // with images can only check bounding box (as it is calculated in container with other objects)
        return b.getBoundsInParent().intersects(a.getBoundsInParent());
        // with shapes, can check precisely (in this case, it is easy because the image is circular)
//        Shape moverBounds = new Circle(a.getX() + a.getFitWidth() / 2,
//                                       a.getY() + a.getFitHeight() / 2,
//                                       a.getFitWidth() / 2 - MOVER_SIZE / 20);
//        return ! Shape.intersect(moverBounds, b).getBoundsInLocal().isEmpty();
    }

    private boolean isSideCollision(Node stationaryNode, Ball ball) {
        return stationaryNode.getBoundsInParent().getMinY() < ball.getCenterY() && stationaryNode.getBoundsInParent().getMaxY() > ball.getCenterY();
    }

    private void handleBallIntersectingBounds() {
        if(ball.getCenterX() - BALL_RADIUS <= 0) {
            ball.setCenterX(BALL_RADIUS);
            ball.setSpeedX(-ball.getSpeedX());
        } else if (ball.getCenterX() + BALL_RADIUS >= mainScene.getWidth()) {
            ball.setCenterX(mainScene.getWidth() - BALL_RADIUS);
            ball.setSpeedX(-ball.getSpeedX());
        } else if (ball.getCenterY() - BALL_RADIUS <= 0) {
            ball.setCenterY(BALL_RADIUS);
            ball.setSpeedY(-ball.getSpeedY());
        } else if (ball.getCenterY() >= mainScene.getHeight()) {
            ball.resetPosition();
            handleLifeDecrement();
        }
    }

    private void handleLifeDecrement() {
        lives--;
        if(lives == 0) {
            primaryStage.setScene(gameOverScene);
        }
    }

    private void updateBallPosition(double elapsedTime) {
        ball.setCenterX(ball.getCenterX() + ball.getSpeedX() * elapsedTime);
        ball.setCenterY(ball.getCenterY() + ball.getSpeedY() * elapsedTime);
    }

    private void handlePaddleIntersectingBounds() {
        if(paddle.getX() <= 0) {
            paddle.setX(0);
        } else if (paddle.getX() + PADDLE_WIDTH >= mainScene.getWidth()) {
            paddle.setX(mainScene.getWidth() - PADDLE_WIDTH);
        }
    }

    private void handleBallIntersectingPaddle() {
        if(isIntersecting(paddle, ball)) {
            ball.setSpeedY(-ball.getSpeedY());
            ball.setAngle(ball.getAngle() + Math.toRadians(0.5 * ((paddle.getBoundsInParent().getMinX() + paddle.getWidth()/2) - ball.getCenterX())));
        }
    }

    private void handleBallIntersectingBrick() {
        Node intersectedBrick = bricks.getBrickIntersecting(ball);
        if(intersectedBrick != null) {
            primaryRoot.getChildren().remove(intersectedBrick);
            mainScene.setRoot(primaryRoot);
            if(isSideCollision(intersectedBrick, ball)) {
                ball.setSpeedX(-ball.getSpeedX());
            } else {
                ball.setSpeedY(-ball.getSpeedY());
            }
        }
    }

    private void handleNoBricksRemaining() {
        if(!bricks.isBrickRemaining()) {
            primaryStage.setScene(winScene);
        }
    }
}

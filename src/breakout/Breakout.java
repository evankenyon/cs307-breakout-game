package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
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
    public static final int OFFSET_AMOUNT = 50;
    public static final int PADDLE_SPEED = 10;
    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 20;
    public static final double BALL_CENTER_X = 200;
    public static final double BALL_CENTER_Y = 140;
    public static final double BALL_RADIUS = 10;
    public static final Paint PADDLE_COLOR = Color.BISQUE;

    private Scene mainScene;
    private Scene gameOverScene;
    private Rectangle paddle;
    private Ball ball;
    private Bricks bricks;
    private int lives;
    Stage primaryStage;


    /**
     * Borrowed from example_animation in course gitlab
     */
    @Override
    public void start (Stage stage) {
        mainScene = setupGame(SIZE, SIZE, BACKGROUND);
        gameOverScene = setupGameOver(SIZE, SIZE, BACKGROUND);
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
        paddle = new Rectangle(width / 2 - PADDLE_WIDTH / 2, height - OFFSET_AMOUNT , PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Ball(BALL_CENTER_X, BALL_CENTER_Y, BALL_RADIUS);
        bricks = new Bricks(SIZE, SIZE, 20, 20, 0.1);
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(paddle, ball, bricks);
        return setupScene(root, width, height, background);
    }

    private Scene setupGameOver(int width, int height, Paint background) {
        // Text construction was borrowed from https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
        Text gameOver = new Text("Game Over");
        gameOver.setFont(Font.font ("Verdana", 20));
        gameOver.setX(50);
        gameOver.setY(50);
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(gameOver);
        return setupScene(root, width, height, background);
    }

    private Scene setupScene(Group root, int width, int height, Paint background) {
        Scene scene = new Scene(root, width, height, background);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void step (double elapsedTime) {
        handlePaddleIntersectingBounds(paddle);
        handleBallIntersectingBounds(ball);
        updateBallPosition(ball, elapsedTime);
        if(isIntersecting(paddle, ball)) {
            ball.setSpeedY(-ball.getSpeedY());
        }
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

    private void handleBallIntersectingBounds(Ball ball) {
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
            ball.setCenterY(BALL_CENTER_Y);
            ball.setCenterX(BALL_CENTER_X);
            handleLifeDecrement();
        }
    }

    private void handleLifeDecrement() {
        lives--;
        if(lives == 0) {
            primaryStage.setScene(gameOverScene);
        }
    }

    private void updateBallPosition(Ball ball, double elapsedTime) {
        ball.setCenterX(ball.getCenterX() + ball.getSpeedX() * elapsedTime);
        ball.setCenterY(ball.getCenterY() + ball.getSpeedY() * elapsedTime);
    }
    private void handlePaddleIntersectingBounds(Rectangle paddle) {
        if(paddle.getX() <= 0) {
            paddle.setX(0);
        } else if (paddle.getX() + PADDLE_WIDTH >= mainScene.getWidth()) {
            paddle.setX(mainScene.getWidth() - PADDLE_WIDTH);
        }
    }
}

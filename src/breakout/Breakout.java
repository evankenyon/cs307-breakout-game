package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Purpose: The purpose of this class is to setup a breakout game, and to update the different
 * member objects and variables as necessary throughout the game (i.e. at each frame and at each
 * key input)
 * Assumptions: JavaFX installed on device, values of vars do not push past practical limits
 * (e.g. SCENE_SIZE isn't set to a value larger than the device screen itself).
 * Dependencies: This class depends on several classes from the JavaFX platform, the Ball claass, and the Bricks class.
 * Example: Use this class to set up and manage objects involved in a breakout style video game.
 * Specifically, running this class will do just that.
 * Other details: Due to JavaFX syntax, a main method is not needed for running this class. The start
 * method acts as its replacement.
 *
 * @author Evan Kenyon
 */
public class Breakout extends Application {

    // No particular reasoning behind the scene, font size, or font type besides personal preference
    // Can put reasoning behind magic values in README or in comments, only do it for important values too
    // size, frames per second, second delay, background, highlight, offset amount, mover, paddle color, and paddle color
    // borrowed from example_animation in course gitlab
    public static final int SCENE_SIZE = 800;
    public static final int FONT_SIZE = 30;
    public static final String FONT_TYPE = "Verdana";
    // Can put these comments in README, generally do when it looks crowded
    public static final int OFFSET_PADDLE_AMOUNT = 50;
    public static final int OFFSET_BALL_AMOUNT = OFFSET_PADDLE_AMOUNT + 100;
    // I found that this was the "goldilocks" size, since not too many bricks were on the screen
    // so that the game would quickly become boring, but there were enough that winning the game
    // wasn't trivial
    public static final int BRICK_SIZE = 49;
    // This made the ball small enough to fit in empty spaces in between bricks and not too small
    // to look odd visually
    public static final double BALL_RADIUS = BRICK_SIZE /3.0;
    // This made the paddle wide enough to easily fit the ball, but not too wide as to make the game
    // completely trivial to win
    public static final int PADDLE_WIDTH = (int) BALL_RADIUS * 8;
    public static final double SECOND_DELAY = 1.0 / 60;
    public static final String TITLE = "Breakout Game";
    // This paddle speed was the minimum value, based on the max X speed being 300 in the
    // Ball class, that I found where the player would almost always only miss the ball
    // due to their own fault
    public static final int PADDLE_SPEED = 25;
    public static final int TEXT_POSITION_X = 50;
    public static final int TEXT_POSITION_Y = 50;
    public static final int PADDLE_HEIGHT = 20;
    public static final double BLOCKED_ROW_OR_COL_FREQ = 0.1;

    private Scene mainScene;
    private Scene gameOverScene;
    private Scene winScene;
    private Rectangle paddle;
    private Ball ball;
    private Bricks bricks;
    private int delayIntersectionFrames;
    private IntegerProperty lives;

    // Make these private
    Stage primaryStage;
    Group primaryRoot;


    /**
     * Purpose: Start the game by calling helper methods which set up all the scenes,
     * the primary stage, and the timeline
     * Assumptions: JavaFX is installed on the device, set up scene methods are not changed in a way that
     * would remove any key objects (ex. the bricks) from the game or in a way that would return an invalid/null
     * Scene object, timeline method is not changed in such a way that would return an invalid/null timeline
     * Attributions: The setup for this method was borrowed from example_animation in course gitlab
     * @param stage the stage in which different scenes reside throughout the game
     */
    @Override
    public void start (Stage stage) {
        setupAllScenes();
        setupPrimaryStage(stage);
        setupTimeline();
    }

    private Scene setupGame() {
        setupMainSceneNodes();
        // put as a constant/global
        // Fine to make numbers and strings as constants ("magic values")
        double scoreDisplayYPos = 600;
        Label scoreDisplay = setupDynamicDataDisplay("Score: ", bricks.getScore().asString(), scoreDisplayYPos);
        Label livesDisplay = setupDynamicDataDisplay("Lives: ", lives.asString(), scoreDisplayYPos + 50);
        delayIntersectionFrames = 2;
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(paddle, ball, scoreDisplay, livesDisplay);
        root.getChildren().addAll(bricks.getBricks());
        primaryRoot = root;
        return setupScene(root);
    }

    private Scene setupTextScene(String message) {
        // Text construction was borrowed from https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
        Text text = new Text(TEXT_POSITION_X, TEXT_POSITION_Y, message);
        text.setFont(Font.font (FONT_TYPE, FONT_SIZE));
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(text);
        return setupScene(root);
    }

    private Scene setupScene(Group root) {
        Scene scene = new Scene(root, SCENE_SIZE, SCENE_SIZE, Color.AZURE);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    // Label setup code was borrowed from https://stackoverflow.com/questions/56016866/how-do-i-output-updating-values-for-my-scoreboard
    private Label setupDynamicDataDisplay(String text, StringBinding data, double yVal) {
        Label display = new Label();
        display.setFont(new Font(FONT_TYPE, FONT_SIZE));
        // Don't need to attribute for official Java documentation
        // Found concat method and SimpleStringProperty from
        // https://docs.oracle.com/javase/8/javafx/api/javafx/beans/property/SimpleStringProperty.html
        display.textProperty().bind(new SimpleStringProperty(text).concat(data));
        display.setLayoutX(TEXT_POSITION_X);
        display.setLayoutY(yVal);
        return display;
    }

    private void step (double elapsedTime) {
//        Check to see if need this or not
//        handleDelayingIntersectionFrames();
        handleIntersections();
        updateBallPosition(elapsedTime);
        handleNoBricksRemaining();
    }

    // Borrowed from example_animation in course gitlab
    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case RIGHT -> paddle.setX(paddle.getX() + PADDLE_SPEED);
            case LEFT -> paddle.setX(paddle.getX() - PADDLE_SPEED);
            case SPACE -> ball.setIsMoving(true);
        }
    }

    // Borrowed from example_animation in course gitlab
    private boolean isIntersecting (Shape a, Shape b) {
        return b.getBoundsInParent().intersects(a.getBoundsInParent());
    }

    // Borrowed idea for checking if side collision from
    // https://stackoverflow.com/questions/8866389/java-gaming-collision-detection-side-collision-with-rectangles
    private boolean isSideCollision(Node stationaryNode, Ball ball) {
        return stationaryNode.getBoundsInParent().getMinY() < ball.getCenterY() && stationaryNode.getBoundsInParent().getMaxY() > ball.getCenterY();
    }

    private boolean isBottomCollision(Node stationaryNode, Ball ball) {
        return stationaryNode.getBoundsInParent().getMaxY() < ball.getCenterY();
    }

    private void handleBallIntersectingBounds() {
        if(ball.getCenterX() - ball.getRadius() <= 0) {
            ball.setCenterX(ball.getRadius());
            ball.reverseXVelocity();
        } else if (ball.getCenterX() + ball.getRadius() >= mainScene.getWidth()) {
            ball.setCenterX(mainScene.getWidth() - ball.getRadius());
            ball.reverseXVelocity();
        } else if (ball.getCenterY() - ball.getRadius() <= 0) {
            ball.setCenterY(ball.getRadius());
            ball.reverseYVelocity();
        } else if (ball.getCenterY() >= mainScene.getHeight()) {
            ball.resetPosition();
            handleLifeDecrement();
        }
    }

    private void handleLifeDecrement() {
        lives.set(lives.get() -1);
        ball.setIsMoving(false);
        if(lives.get() == 0) {
            primaryStage.setScene(gameOverScene);
        }
    }

    private void updateBallPosition(double elapsedTime) {
        ball.setCenterX(ball.getCenterX() + ball.getXVelocity() * elapsedTime);
        ball.setCenterY(ball.getCenterY() + ball.getYVelocity() * elapsedTime);
    }

    private void handlePaddleIntersectingBounds() {
        if(paddle.getX() <= 0) {
            paddle.setX(0);
        } else if (paddle.getX() + paddle.getWidth() >= mainScene.getWidth()) {
            paddle.setX(mainScene.getWidth() - paddle.getWidth());
        }
    }

    private void handleBallIntersectingPaddle() {
        if (isIntersecting(paddle, ball) && !isBottomCollision(paddle, ball)) {
            ball.reverseYVelocity();
            if (isSideCollision(paddle, ball)) {
                ball.reverseXVelocity();
            } else if (delayIntersectionFrames == 2){
                ball.setAngle(ball.getAngle() + Math.toRadians(0.5 * ((paddle.getBoundsInParent().getMinX() + paddle.getWidth() / 2) - ball.getCenterX())));
            } else {
                handleDelayingIntersectionFrames();
            }
        }
    }

    private void handleBallIntersectingBrick() {
        Node intersectedBrick = bricks.getBrickIntersecting(ball);
        if(intersectedBrick != null) {
            primaryRoot.getChildren().remove(intersectedBrick);
            mainScene.setRoot(primaryRoot);
            if(isSideCollision(intersectedBrick, ball)) {
                ball.reverseXVelocity();
            } else {
                ball.reverseYVelocity();
            }
        }
    }

    private void handleNoBricksRemaining() {
        if(!bricks.isBrickRemaining()) {
            primaryStage.setScene(winScene);
        }
    }

    private void setupPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setScene(mainScene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
    }

    private void setupAllScenes() {
        mainScene = setupGame();
        gameOverScene = setupTextScene("Game Over");
        winScene = setupTextScene("You win!");
    }

    private void setupTimeline() {
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
        animation.play();
    }

    private void setupMainSceneNodes() {
        // Rectangle constructor parameters from example_animation in course gitlab
        paddle = new Rectangle(SCENE_SIZE / 2 - PADDLE_WIDTH / 2, SCENE_SIZE - OFFSET_PADDLE_AMOUNT, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Ball(SCENE_SIZE /2, SCENE_SIZE - OFFSET_BALL_AMOUNT, BALL_RADIUS);
        bricks = new Bricks(SCENE_SIZE, SCENE_SIZE, BRICK_SIZE, BRICK_SIZE, BLOCKED_ROW_OR_COL_FREQ);
        lives = new SimpleIntegerProperty(3);
    }

    private void handleDelayingIntersectionFrames() {
        delayIntersectionFrames--;
        if(delayIntersectionFrames == 0) {
            delayIntersectionFrames = 2;
        }
    }

    private void handleIntersections() {
        handlePaddleIntersectingBounds();
        handleBallIntersectingBounds();
        handleBallIntersectingPaddle();
        handleBallIntersectingBrick();
    }

}

package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;

import static sample.Consts.*;


public class Main extends Application {
    int speed = 2;
    int score = 0;
    List<Rectangle> snake = new ArrayList<>();
    int appleX, appleY;
    boolean isEatenApple = false;
    boolean isGameOver = false;
    boolean justDigestedApple = false;
    List<Rectangle> eatenApples = new ArrayList<>();
    Label label;
    Direction actualDirection = Direction.RIGHT;
    Direction newDirection = actualDirection;
    GraphicsContext gc;
    long tmp = 0;


    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("SNAKE");
            Pane root = new Pane();
            Scene scene = new Scene(root, WIDTH * SIZE, HEIGHT * SIZE + 50);
            Canvas canvas = new Canvas(WIDTH * SIZE, HEIGHT * SIZE);
            primaryStage.setResizable(false);
            gc = canvas.getGraphicsContext2D();
            root.getChildren().add(canvas);

            label = new Label();
            root.getChildren().add(label);

            newGame();

            AnimationTimer animationTimer = new AnimationTimer() {

                public void handle(long now) {
                    int timeUntilNextMove = 1000000000 / speed;
                    if (tmp == 0) {
                        tmp = now;
                        if (!isGameOver) {
                            goSnakeGo();
                        } else {
                            isGameOver = false;
                            newGame();
                        }
                        return;
                    }
                    if (now - tmp > timeUntilNextMove) {
                        tmp = now;
                        if (!isGameOver) {
                            goSnakeGo();
                        } else {
                            isGameOver = false;
                            newGame();
                        }
                    }
                }
            };

            animationTimer.start();
            primaryStage.setScene(scene);

            scene.setOnKeyPressed(e -> checkWhichKeyWasPressed(e));

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkWhichKeyWasPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP:
                if (actualDirection != Direction.DOWN) {
                    newDirection = Direction.UP;
                }
                break;
            case DOWN:
                if (actualDirection != Direction.UP) {
                    newDirection = Direction.DOWN;
                }
                break;
            case LEFT:
                if (actualDirection != Direction.RIGHT) {
                    newDirection = Direction.LEFT;
                }
                break;
            case RIGHT:
                if (actualDirection != Direction.LEFT) {
                    newDirection = Direction.RIGHT;
                }
                break;
        }
    }

    private void goSnakeGo() {
        actualDirection = newDirection;
        switch (newDirection) {
            case UP:
                setNewPosition(0, -SIZE);
                break;
            case DOWN:
                setNewPosition(0, SIZE);
                break;
            case LEFT:
                setNewPosition(-SIZE, 0);
                break;
            case RIGHT:
                setNewPosition(SIZE, 0);
                break;
        }
        drawAgain(gc);
    }

    private void checkIfIsGameOver() {
        int headX = (int) snake.get(0).getX();
        int headY = (int) snake.get(0).getY();
        if (headX < 0 || headY < 0 || headX > WIDTH * SIZE - SIZE || headY > WIDTH * SIZE - SIZE) {
            isGameOver = true;
        } else {
            for (int i = 1; i < snake.size(); i++) {
                if (headX == snake.get(i).getX() && headY == snake.get(i).getY()) {
                    isGameOver = true;
                    break;
                }
            }
        }
    }

    public void newGame() {
        tmp = 0;
        eatenApples.clear();
        isEatenApple = false;
        justDigestedApple = false;
        snake.clear();
        speed = 2;
        score = 0;
        actualDirection = Direction.RIGHT;
        newDirection = actualDirection;


        snake.add(new Rectangle(5 * SIZE, 5 * SIZE, SIZE, SIZE));
        snake.add(new Rectangle(4 * SIZE, 5 * SIZE, SIZE, SIZE));
        snake.add(new Rectangle(3 * SIZE, 5 * SIZE, SIZE, SIZE));
        generateApple();
        drawAgain(gc);
    }

    private void generateApple() {
        appleX = new Random().nextInt(WIDTH) * SIZE;
        appleY = new Random().nextInt(WIDTH) * SIZE;
        isEatenApple = false;
    }

    private void setNewPosition(int moveX, int moveY) {
        int oldHeadX = (int) snake.get(0).getX();
        int oldHeadY = (int) snake.get(0).getY();

        snake.get(0).setX(oldHeadX + moveX);
        snake.get(0).setY(oldHeadY + moveY);
        checkIfIsGameOver();
        if (oldHeadX + moveX == appleX && oldHeadY + moveY == appleY) {
            isEatenApple = true;
            eatenApples.add(new Rectangle(appleX, appleY, SIZE, SIZE));
        }

        if (isEatenApple) {
            score++;
            generateApple();
            if (speed < 10) {
                speed++;
            }
        }

        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
        }
        snake.get(1).setX(oldHeadX);
        snake.get(1).setY(oldHeadY);
    }

    public void drawAgain(GraphicsContext gc) {
        if (!isGameOver) {
            DrawingGameField gameField = new DrawingGameField();
            gameField.drawGameField(gc, label, score);
            gc.setLineWidth(1);
            drawApple(gc);
            DrawingSnake drawingSnake = new DrawingSnake();
            justDigestedApple = drawingSnake.drawSnake(gc, snake, eatenApples);
            if (justDigestedApple) {
                snake.add(new Rectangle(eatenApples.get(0).getX(), eatenApples.get(0).getY(), SIZE, SIZE));
                eatenApples.remove(0);
                justDigestedApple = false;
            }
        }
    }

    public void drawApple(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(appleX, appleY, SIZE, SIZE);
        gc.strokeRect(appleX, appleY, SIZE, SIZE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

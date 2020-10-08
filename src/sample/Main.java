package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    long tmp = 0;

    @Override
    public void start(Stage primaryStage) {
        try {
            GameManagement gameManagement = new GameManagement(primaryStage);
            gameManagement.newGame();

            AnimationTimer animationTimer = new AnimationTimer() {

                public void handle(long now) {
                    int timeUntilNextMove = 1000000000 / gameManagement.speed;
                    if (tmp == 0) {
                        tmp = now;
                        if (!gameManagement.isGameOver) {
                            gameManagement.goSnakeGo();
                        } else {
                            gameManagement.isGameOver = false;
                            gameManagement.newGame();
                        }
                        return;
                    }
                    if (now - tmp > timeUntilNextMove) {
                        tmp = now;
                        if (!gameManagement.isGameOver) {
                            gameManagement.goSnakeGo();
                        } else {
                            gameManagement.isGameOver = false;
                            gameManagement.newGame();
                        }
                    }
                }
            };

            animationTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

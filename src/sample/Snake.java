package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Objects;

import static sample.Consts.SIZE;

public class Snake {
    private boolean justDigestedApple;

    public void drawSnake(GraphicsContext gc, List<Rectangle> snake, List<Rectangle> eatenApples) {
        justDigestedApple = false;
        gc.setFill(Color.GREEN);
        for (int i = 0; i < snake.size(); i++) {
            int sX = (int) snake.get(i).getX();
            int sY = (int) snake.get(i).getY();
            gc.setFill(Color.GREEN);
            for (int j = 0; j < Objects.requireNonNull(eatenApples).size(); j++) {
                if (eatenApples.get(j).getX() == sX && eatenApples.get(j).getY() == sY) {
                    gc.setFill(Color.BLUE);
                    if (i == snake.size() - 1) {
                        justDigestedApple=true;
                    }
                }
            }
            gc.fillRect(sX, sY, SIZE, SIZE);
            gc.strokeRect(sX, sY, SIZE, SIZE);
        }
    }

    public boolean isJustDigestedApple(){
        return justDigestedApple;
    }
}

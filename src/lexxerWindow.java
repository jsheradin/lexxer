//There are little to no comments
//Good luck

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.util.Duration;

public class lexxerWindow extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    //TODO figure out how to do w/o global
    Text scoreText;
    Text timerText;
    int timer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("lexxer");

        Group menuGroup = new Group();
        Scene menuScene = new Scene(menuGroup, 260, 180, Color.WHITE);

        TextField wideBox = new TextField("4");
        TextField tallBox = new TextField("4");
        TextField timeBox = new TextField("120");
        TextField sizeBox = new TextField("100");
        TextField dictBox = new TextField("english.txt");

        Button start = new Button("New game");
        start.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                //Validate input
                try {
                    startGame(Integer.parseInt(wideBox.getCharacters().toString()),
                            Integer.parseInt(tallBox.getCharacters().toString()),
                            Integer.parseInt(sizeBox.getCharacters().toString()),
                            Integer.parseInt(timeBox.getCharacters().toString()),
                            dictBox.getCharacters().toString(),
                            primaryStage, menuScene, start);
                } catch (NumberFormatException e){
                    System.err.printf("\nBad input:\n" + e.getMessage());
                }
            }
        });

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));

        grid.add(new Label("Blocks Wide: "), 0, 0);
        grid.add(new Label("Blocks Tall: "), 0, 1);
        grid.add(new Label("Block Size (px): "), 0, 2);
        grid.add(new Label("Time (s): "), 0, 3);
        grid.add(new Label("Dictionary File: "), 0, 4);
        grid.add(wideBox, 1, 0);
        grid.add(tallBox, 1, 1);
        grid.add(sizeBox, 1, 2);
        grid.add(timeBox, 1, 3);
        grid.add(dictBox, 1, 4);
        grid.add(start, 0, 5);

        menuGroup.getChildren().add(grid);
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void startGame(int inputWide, int inputTall, int inputSize, int time, String dictFile, Stage stage, Scene menuScene, Button start){
        timer = time;
        GridPane gameGrid = new GridPane();
        lexxerGame game = new lexxerGame(inputWide, inputTall, 3, inputSize, "dictionary/" + dictFile);

        Group gameBoard = new Group();
        Scene gameScene = new Scene(gameBoard, inputWide*inputSize, inputTall*inputSize + inputSize/4, Color.WHITE);

        String[][] letters = game.getGrid();

        Text timeLabel = new Text("Time:  ");
        timeLabel.setStyle(String.format("-fx-font: %dpx arial", inputSize/4));
        gameGrid.setHalignment(timeLabel, HPos.RIGHT);
        gameGrid.add(timeLabel, 0, inputTall+1);

        timerText = new Text(String.format("%d s", timer));
        timerText.setStyle(String.format("-fx-font: %dpx arial", inputSize/4));
        gameGrid.add(timerText, 1, inputTall+1);

        Text scoreLabel = new Text("Score:  ");
        scoreLabel.setStyle(String.format("-fx-font: %dpx arial", inputSize/4));
        gameGrid.setHalignment(scoreLabel, HPos.RIGHT);
        gameGrid.add(scoreLabel, 2, inputTall+1);

        scoreText = new Text(String.format("%d", game.getScore()));
        scoreText.setStyle(String.format("-fx-font: %dpx arial", inputSize/4));
        gameGrid.add(scoreText, 3, inputTall+1);

        for(int x=0; x<inputWide; x++){
            for(int y=0; y<inputTall; y++){
                Rectangle rectangle = new Rectangle(inputSize, inputSize);
                rectangle.setFill(Color.TRANSPARENT);
                gameGrid.add(rectangle, x, y);
                game.setRect(x, y, rectangle);

                Text text = new Text(letters[x][y]);
                text.setMouseTransparent(true);
                gameGrid.add(text, x, y);
                text.setStyle(String.format("-fx-font: %dpx arial", inputSize/2));
                gameGrid.setHalignment(text, HPos.CENTER);
            }
        }

        gameScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(timer>0) {
                    int xExact = (int) event.getX();
                    int yExact = (int) event.getY();

                    int x = (int) Math.floor(xExact / inputSize);
                    int y = (int) Math.floor(yExact / inputSize);

                    game.clickDetected(x, y);
                    //System.out.printf("%f, %f", event.getX(), event.getY());
                }
            }
        });

        gameScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(timer>0) {
                    int xExact = (int) event.getX();
                    int yExact = (int) event.getY();

                    int x = (int) Math.floor(xExact / inputSize);
                    int y = (int) Math.floor(yExact / inputSize);

                    int dist = Math.abs(xExact - x * inputSize - inputSize / 2);

                    if (dist < inputSize / 4) {
                        game.dragDetected(x, y);
                        //System.out.printf("%d, %d\n", x, y);
                    }
                    //System.out.printf("%d, %d, %d, %s\n", xExact, x, dist, dist<inputSize/4);
                }
            }
        });

        gameScene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Increment score
                //System.out.printf("Score: %d, Word: %s\n", game.submit(), game.getWord());
                if(timer>0) {
                    game.submit();
                    for (int x = 0; x < inputWide; x++) {
                        for (int y = 0; y < inputTall; y++) {
                            game.setRectColor(x, y, Color.TRANSPARENT);
                        }
                    }

                    gameGrid.getChildren().remove(scoreText);
                    scoreText = new Text(String.format("%d", game.getScore()));
                    scoreText.setStyle(String.format("-fx-font: %dpx arial", inputSize / 4));
                    gameGrid.add(scoreText, 3, inputTall + 1);
                }
            }
        });

        for (int x = 0; x < inputWide; x++) {
            Line div = new Line(x * inputSize, 0, x * inputSize, inputSize*inputTall);
            div.setFill(Color.BLACK);
            div.toFront();
            gameBoard.getChildren().add(div);
        }

        for (int y = 0; y < inputTall+1; y++) {
            Line div = new Line(0, y * inputSize, inputWide*inputSize, y * inputSize);
            div.setFill(Color.BLACK);
            gameBoard.getChildren().add(div);
        }

        Timeline countdown = new Timeline((new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.printf("Time: %d\n", timer);
                timer--;
                gameGrid.getChildren().remove(timerText);
                timerText = new Text(String.format("%d s", timer));
                timerText.setStyle(String.format("-fx-font: %dpx arial", inputSize/4));
                gameGrid.add(timerText, 1, inputTall+1);

                if(timer <= 0){
                    Button menu = new Button("Menu");
                    menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            stage.setScene(menuScene);
                            stage.show();
                        }
                    });

                    Button restart = new Button("Restart");
                    restart.setOnMouseClicked(start.getOnMouseClicked());

                    gameGrid.add(menu, 0, 0);
                    gameGrid.setHalignment(menu, HPos.CENTER);
                    gameGrid.add(restart, 0, 1);
                    gameGrid.setHalignment(restart, HPos.CENTER);
                }
            }
        })));
        countdown.setCycleCount(timer);
        countdown.play();

        gameBoard.getChildren().add(gameGrid);
        stage.setScene(gameScene);
        stage.show();
    }

}
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class lexxerGame {
    int wide;
    int tall;
    int minLength;
    int blockSize;
    alphaTree tree;
    lexxerTile[][] grid;

    int lastx;
    int lasty;
    boolean dragStarted;
    String word;
    ArrayList<lexxerTile> used;

    ArrayList<String> words;
    int score;

    public lexxerGame(int wide, int tall, int minLength, int blockSize, String dictFile){
        this.wide = wide;
        this.tall = tall;
        this.minLength = minLength;
        this.blockSize = blockSize;
        this.tree = new alphaTree(dictFile);
        this.grid = newGrid(wide, tall);
        this.words = new ArrayList<String>();
        this.score = 0;

        //printGrid();
        //grid[1][1].printAdjacent();
        //System.out.printf("%s\n", grid[1][1].isAdjacent(grid[1][2]));
    }

    private lexxerTile[][] newGrid(int wide, int tall){
        lexxerTile[][] grid = new lexxerTile[wide][tall];
        for(int x=0; x<wide; x++){
            for(int y=0; y<tall; y++){
                grid[x][y] = new lexxerTile(randLetter());
            }
        }

        for(int x=0; x<wide; x++){
            for(int y=0; y<tall; y++){
                grid[x][y].setAdjacent(grid, x, y, wide, tall);
            }
        }

        return grid;
    }

    private String randLetter(){
        //TODO make friendly for other alphabets
        //TODO improve random distribution (lots of repeat letters, consonants, etc.)
        Random r = new Random();
        char[] letters = "abcdefghijklmnopqrstuvwxyzaeioue".toCharArray();
        Character letter = letters[r.nextInt(letters.length)];
        String str = letter.toString();
        if(str.equalsIgnoreCase("q")){
            str = "qu";
        }
        return str;
    }

    public String[][] getGrid() {
        String[][] strs = new String[wide][tall];
        for(int x=0; x<wide; x++){
            for(int y=0; y<tall; y++){
                strs[x][y] = grid[x][y].toString();
            }
        }
        return strs;
    }

    private void printGrid(){
        String[][] letters = getGrid();
        for(int y=0; y<tall; y++){
            for(int x=0; x<wide; x++){
                System.out.printf("%s ", letters[x][y]);
            }
            System.out.printf("\n");
        }
    }

    public void clickDetected(int x, int y){
        lastx = x;
        lasty = y;
        dragStarted = true;
        word = "";
        used = new ArrayList<lexxerTile>();
    }

    public void dragDetected(int x, int y){
        try {
            if (!used.contains(grid[x][y])) {
                if (grid[lastx][lasty].isAdjacent(grid[x][y]) || dragStarted) {
                    word += getGrid()[x][y];
                    lastx = x;
                    lasty = y;
                    dragStarted = false;
                    used.add(grid[x][y]);
                    setRectColor(x, y, Color.LIGHTBLUE);
                    //System.out.printf("%s\n", word);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            //mouse out of window
        }
    }

    public int submit(){
        int length = tree.isWord(word);
        //System.out.printf("Word length: %d\n", length);

        if(length >= minLength && !words.contains(word)) {
            words.add(word);
            score += length;
            //System.out.printf("Current Score: %d\n", score);
            return length;
        } else {
            return 0;
        }
    }

    public String getWord(){
        return word;
    }

    public void setRect(int x, int y, Rectangle rectangle){
        grid[x][y].setRectangle(rectangle);
    }

    public void setRectColor(int x, int y, Color color){
        try {
            grid[x][y].setColor(color);
        } catch (ArrayIndexOutOfBoundsException e){
            //mouse out of window
        }
    }

    public int getScore() {
        return score;
    }
}

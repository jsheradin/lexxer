import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class lexxerTile {
    String letter;
    ArrayList<lexxerTile> adjacent = new ArrayList<lexxerTile>();
    Rectangle rectangle;

    public lexxerTile(String letter){
        this.letter = letter;
    }

    @Override
    public String toString() {
        return this.letter;
    }

    public boolean isAdjacent(lexxerTile tile){
        return adjacent.contains(tile);
    }

    public void setAdjacent(lexxerTile[][] grid, int xpos, int ypos, int wide, int tall){
        for(int xOffset=-1; xOffset <= 1; xOffset++){
            for(int yOffset=-1; yOffset<=1; yOffset++){
                if(xOffset != 0 || yOffset != 0){
                    int x = xpos + xOffset;
                    int y = ypos + yOffset;

                    if(x>=0 && y>=0 && x<=wide-1 && y<=tall-1){ //-1??
                        adjacent.add(grid[x][y]);
                    }
                }
            }
        }
    }

    public void printAdjacent(){
        for(int i=0; i<adjacent.size(); i++){
            System.out.printf("%s", adjacent.get(i).toString());
        }
        System.out.println();
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setColor(Color color){
        rectangle.setFill(color);
    }
}

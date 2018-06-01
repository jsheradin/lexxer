import java.util.ArrayList;

public class alphaBlock {
    char letter;
    int depth;
    boolean isWord;
    ArrayList<alphaBlock> next;

    public alphaBlock(char letter, int depth, boolean isWord, ArrayList<alphaBlock> next){
        this.letter = letter;
        this.depth = depth;
        this.isWord = isWord;
        this.next = next;
    }

    public char getLetter(){
        return letter;
    }

    public ArrayList<alphaBlock> getNext() {
        return next;
    }

    public boolean isWord() {
        return isWord;
    }

    public int getDepth() {
        return depth;
    }

    public boolean hasNext() {
        return next == null;
    }

}
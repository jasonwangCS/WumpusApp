
public class WumpusPlayer{
    private int playerDirection;
    private boolean arrow;
    private boolean gold;
    private int colPosition;
    private int rowPosition;
    public static final int NORTH = 0;
    public static final int SOUTH = 2;
    public static final int EAST = 1;
    public static final int WEST = 3;
    WumpusPlayer(){
        playerDirection = NORTH;
        gold = false;
        arrow = true;
    }
    public int getPlayerDirection(){
        return playerDirection;
    }
    public boolean getArrow(){
        return arrow;
    }
    public boolean getGold(){
        return gold;
    }
    public int getCol(){
        return colPosition;
    }

    public int getRow(){
        return rowPosition;
    }

    public void setPlayerDirection(int direction){
        playerDirection = direction;
    }
    public void setArrow(boolean arrow){
        this.arrow = arrow;
    }
    public void setGold(boolean gold){
        this.gold = gold;
    }

    public void setCol(int direction){
        colPosition = direction;
    }
    public void setRow(int direction){
        rowPosition = direction;
    }
}
import java.util.Random;

public class WumpusMap {
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLUMNS = 10;
    public static final int NUM_PITS = 10;

    private WumpusSquare[][] grid;
    private int[] numbers = {0,1,2,3,4,5,6,7,8,9};

    private int ladderC;
    private int ladderR;
    private int currentC;
    private int currentR;

    WumpusMap() {
        createMap();
    }
    public void createMap() {
        grid = new WumpusSquare[NUM_ROWS][NUM_COLUMNS];
        for(int i = 0; i < NUM_ROWS; i++){
            for(int c = 0 ; c < NUM_COLUMNS; c++){
                grid[i][c] = new WumpusSquare();
            }
        }
        Random rand = new Random();

        for(int i = 0; i < 10; i++){
            int col = rand.nextInt(10);
            int row = rand.nextInt(10);
            if(!getSquare(row, col).isPit()){
                getSquare(row, col).setPit(true);
                if(!(col + 1 >9)){
                    getSquare(row, col+1).setBreeze(true);
                }
                if(!(col -1 < 0)){
                    getSquare(row, col-1).setBreeze(true);
                }

                if(!(row + 1 >9)){
                    getSquare(row+1,col ).setBreeze(true);
                }
                if(!(row -1 < 0)){
                    getSquare(row-1, col).setBreeze(true);
                }
            }
            else{
                i--;
            }
        }
        int col = rand.nextInt(10);
        int row = rand.nextInt(10);
        boolean done = false;
        while(!done){
            if(!getSquare(row, col).isPit()){
                getSquare(row, col).setGold(true);
                done = true;
                System.out.println("Set gold at " + row + " + " + col);
            }
            else{
                col = rand.nextInt(10);
                row = rand.nextInt(10);
            }
        }
        col = rand.nextInt(10);
        row = rand.nextInt(10);
        done = false;
        while(!done){
            if(!getSquare(row, col).isPit()){
                getSquare(row, col).setWumpus(true);
                done = true;
                if(!(col + 1 >9)){
                    getSquare(row, col+1).setStench(true);
                }
                if(!(col -1 < 0)){
                    getSquare(row, col-1).setStench(true);
                }

                if(!(row + 1 >9)){
                    getSquare(row+1,col ).setStench(true);
                }
                if(!(row -1 < 0)){
                    getSquare(row-1, col).setStench(true);
                }
            }
            else{
                col = rand.nextInt(10);
                row = rand.nextInt(10);
            }
        }
        done = false;
        while(!done){
            if(!getSquare(row, col).isPit() && !getSquare(row, col).isWumpus() && !getSquare(row, col).isGold()){
                getSquare(row, col).setLadder(true);
                done = true;
                ladderC = col;
                ladderR = row;
                currentC = col;
                currentR = row;
            }
            else{
                col = rand.nextInt(10);
                row = rand.nextInt(10);
            }
        }

        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if(getSquare(row, col).isPit() || getSquare(row, col).isWumpus() || getSquare(row, col).isDeadWumpus())
                {
                    getSquare(row,col).setBreeze(false);
                    getSquare(row,col).setStench(false);
                }
            }
        }
        System.out.print(toString());
    }

    public WumpusSquare getSquare(int row, int col) {
        return grid[row][col];
    }
    public void moveDown(){
        currentR++;
    }
    public void moveUp(){
        currentR--;
    }
    public void moveLeft(){
        currentC--;
    }
    public void moveRight(){
        currentC++;
    }
    public int getCurrentC() {
        return currentC;
    }

    public int getCurrentR() {
        return currentR;
    }

    public int getLadderC() {
        return ladderC;
    }

    public int getLadderR() {
        return ladderR;

    }
    public String toString(){
        StringBuilder ret = new StringBuilder("");
        for(int i = 0; i < 10; i++){
            for(int c = 0; c < 10; c++){
                if(grid[i][c].isPit()){
                    ret.append('P');
                }
                else if(grid[i][c].isWumpus()){
                    ret.append('W');
                }
                else if(grid[i][c].isGold()){
                    ret.append('G');
                }
                else if(grid[i][c].isLadder()){
                    ret.append('L');
                }
                else{
                    ret.append('*');
                }
            }
            ret.append('\n');
        }
        return ret.toString();
    }
}
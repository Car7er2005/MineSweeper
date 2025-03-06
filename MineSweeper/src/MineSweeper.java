import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class MineSweeper {
    private class MineTile extends JButton{
        int r;
        int c;
        
        public MineTile(int r, int c){
            this.r = r;
            this.c = c;            
        }
    }
    
    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;
    
    JFrame frame = new JFrame("Mine Sweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    
    int mineCount = 10;    
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random rand = new Random();
    
    int tilesClicked = 0;
    boolean gameOver = false;
    
    MineSweeper(){
        frame.setSize(boardWidth, boardHeight);
        frame.setLocation(0,0);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("MineSweeper    Mines:" + Integer.toString(mineCount));
        textLabel.setOpaque(true);
        
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
        
        boardPanel.setLayout(new GridLayout(numRows, numCols)); //8x8        
        frame.add(boardPanel);
        
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r,c);
                board[r][c] = tile;
                
                tile.setFocusable(false);
                tile.setMargin(new Insets(0,0,0,0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));                
                tile.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e){
                        if(gameOver){
                            return;
                        }
                        MineTile tile = (MineTile)e.getSource();
                        
                        if(e.getButton() == MouseEvent.BUTTON1){
                            if(tile.getText() == ""){
                                if(mineList.contains(tile)){
                                    revealMines();
                                }else{
                                    checkMine(tile.r,tile.c);
                                }
                            }
                        }else if(e.getButton() == MouseEvent.BUTTON3){
                            if(tile.getText() == "" && tile.isEnabled()){
                                tile.setText("🚩");
                            }else if(tile.getText()== "🚩"){
                                tile.setText("");
                            }
                        }
                    }
                });
                boardPanel.add(tile);
            }
        }
        
        frame.setVisible(true);
        
        setMines();
    }
    
    void setMines() {
        mineList = new ArrayList<MineTile>();
        int mineLeft = mineCount;
        while(mineLeft >0){
            int r = rand.nextInt(numRows);
            int c = rand.nextInt(numCols);
            
            MineTile tile = board[r][c];
            if(!mineList.contains(tile)){
                mineList.add(tile);
                mineLeft--;
            }
        }
        
    }
    
    void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }
        
        gameOver = true;
        textLabel.setText("Game Over!");
    }
    
    void checkMine(int r, int c){
        if(r<0 || r>= numRows || c<0 || c>= numCols){
            return;
        }
        
        MineTile tile = board[r][c];
        if(!tile.isEnabled()){
            return;
        }
        tile.setEnabled(false);
        tilesClicked++;
        
        int minesFound =0;
        
        minesFound += countMine(r-1,c-1);
        minesFound += countMine(r-1,c);
        minesFound += countMine(r-1,c+1);
        
        minesFound += countMine(r,c-1);
        minesFound += countMine(r,c+1);
        
        minesFound += countMine(r+1,c-1);
        minesFound += countMine(r+1,c);
        minesFound += countMine(r+1,c+1);
        
        if (minesFound >0){
            tile.setText(Integer.toString(minesFound));            
        }else{
            tile.setText("");
            
            checkMine(r-1,c-1);
            checkMine(r-1,c);
            checkMine(r-1,c+1);
            checkMine(r,c-1);
            checkMine(r,c+1);
            checkMine(r+1,c-1);
            checkMine(r+1,c);
            checkMine(r+1,c+1);
        }
        if(tilesClicked == numRows*numCols - mineList.size()){
            gameOver = true;
            textLabel.setText("Mines Cleared!");
        }
    }
    
    int countMine(int r, int c){
        if(r<0 || r>= numRows || c<0 || c>= numCols){
            return 0;
        }
        if (mineList.contains(board[r][c])){
            return 1;
        }
        return 0;
    }
}

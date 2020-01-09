/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author cstuser
 */
public class GameMenu extends JFrame{

    
    protected JLabel gamesWon;
    protected JLabel gamesLost;
    protected JLabel info;
    protected JButton newGame;
    protected static int gameLost = 0;
    protected static int gameWin = 0;
    
    protected static GameMenu instance = null;
    
    public GameMenu(){
        super("Mine Sweeper Menu!");
        
        gamesWon = new JLabel("Games Won: ");
        gamesLost = new JLabel("Games Lost: ");
        info = new JLabel("Game in progress!");
        newGame = new JButton("New Game");
        
        newGame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                MineSweeper game = new MineSweeper();
                newGame.setEnabled(false);
            }
        });
        
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(gamesWon,BorderLayout.LINE_START);
        pane.add(newGame,BorderLayout.CENTER);
        pane.add(gamesLost,BorderLayout.LINE_END);
        pane.add(info,BorderLayout.PAGE_END);
        
        
        instance = this;
        this.setSize(400,400);
        
    }

    public static int addGameLost(){
        gameLost++;
        return gameLost;
    }
    
    public static int addGameWin(){
        gameWin++;
        return gameWin;
    }
    public static void setNewGame(){
        instance.newGame.setEnabled(true);
    }
    
    public static void displayLoseMessage(String message){
        instance.gamesLost.setText(message);
    }
    
    public static void displayWinMessage(String m){
        instance.gamesWon.setText(m);
    }
    
    public static void main(String[] args){
        GameMenu mainMenu = new GameMenu();
        mainMenu.setVisible(true);
    }
    
}

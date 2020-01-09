// Programmer: Yutong Yang
// Date: 2018.09.12
package minesweeper;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author cstuser
 */
public class MineSweeper extends JFrame implements KeyListener {

    protected int[][] board = new int[8][8];
    JButton[] buttons;
    final private int NUM_BUTTONS = 64;
    private boolean ctrlKeyPressed = false;
    private boolean DEBUG_MODE = false;
    //private boolean isGameLose = false;
    private int unclickButton = 64 - 10;

    public MineSweeper() {
        super("Mine Sweeper Game!");
        putBomb();

        setSize(900, 900);
        Container container = getContentPane();
        container.setLayout(new GridLayout(8, 8));

        buttons = new JButton[NUM_BUTTONS];
        for (int i = 0; i < NUM_BUTTONS; ++i) {
            final int buttonIndex = i;
            final int row = i % 8;
            final int column = i / 8;

            buttons[i] = new JButton();

            if (DEBUG_MODE) {
                if (board[row][column] == -1) {
                    buttons[i].setText("Bomb");
                }
            }

            buttons[i].addKeyListener(this);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    
                    if (ctrlKeyPressed && buttons[buttonIndex].getText().equals("")) {
                        buttons[buttonIndex].setText("Mine!");
                    } else if (buttons[buttonIndex].getText().equals("Mine!") && ctrlKeyPressed) {
                        buttons[buttonIndex].setText("");
                    } else {
                        --unclickButton;
                        if (board[row][column] == -1) {
                            //isGameLose = true;
                            buttons[buttonIndex].setText("Bomb");
                            showBomb();
                            lostGame();
                            for (int x = 0; x < NUM_BUTTONS; x++) {
                                buttons[x].setEnabled(false);
                            }
                        } else if (bombCount(row, column) == 0) {
                            buttons[buttonIndex].setEnabled(false);
                            diaplayAroundZero(row, column);

                        } else {
                            buttons[buttonIndex].setEnabled(false);
                            buttons[buttonIndex].setText("" + bombCount(row, column));
                            //                      unclickButton--;
                        }
                        winGame();
                    }
                }
            });
            add(buttons[i]);
        }
        setVisible(true);

    }

    public void putBomb() {
        int i = 0;
        int j = 0;
        for (int n = 0; n < 10; n++) {
            do {
                i = (int) (Math.random() * 8);
                j = (int) (Math.random() * 8);
            } while (board[i][j] == -1);

            board[i][j] = -1;
        }
    }

    public int bombCount(int row, int column) {

        int bomb = 0;

        // This above test will work on all edges, check left
        if (column > 0 && board[row][column - 1] == -1) {
            bomb++;
        }

        //right
        if (column < 7 && board[row][column + 1] == -1) {
            bomb++;
        }

        // buttom left
        if (column > 0 && row < 7 && board[row + 1][column - 1] == -1) {
            bomb++;
        }

        //down
        if (row < 7 && board[row + 1][column] == -1) {
            bomb++;
        }

        //bottom right
        if (column < 7 && row < 7 && board[row + 1][column + 1] == -1) {
            bomb++;
        }

        //top left
        if (column > 0 && row > 0 && board[row - 1][column - 1] == -1) {
            bomb++;
        }
        
        //up
        if (row > 0 && board[row - 1][column] == -1) {
            bomb++;
        }
        
        //top right
        if (column < 7 && row > 0 && board[row - 1][column + 1] == -1) {
            bomb++;
        }

        return bomb;

    }

    public void lostGame() {
        //++gameLose;
        GameMenu.displayLoseMessage("Game Lost " + GameMenu.addGameLost());

        // enable the new game button
        GameMenu.setNewGame();
        
        // display the boom sound
        try {
            File wavFile = new File("victory.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(wavFile);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
        }
    }

    public void winGame() {
        if (unclickButton == 0) {
            //++gameWin;
            GameMenu.displayWinMessage("Game Won " + GameMenu.addGameWin());
            GameMenu.setNewGame();
            showBomb();
            for (int x = 0; x < NUM_BUTTONS; x++) {
                buttons[x].setEnabled(false);
            }

            // play the victory sound
            try {
                File wavFile = new File("boom.wav");
                AudioInputStream ais = AudioSystem.getAudioInputStream(wavFile);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (Exception e) {
            }
        }

    }

    public void showBomb() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (board[row][column] == -1) {
                    buttons[indexFromRowColumn(row, column)].setText("Bomb");
                }
            }
        }
    }

    public void diaplayAroundZero(int row, int column) {
        // click left
        if (row > 0) {
            int index = indexFromRowColumn(row - 1, column);
            buttons[index].doClick(0);
        }

        // right
        if (row < 7) {
            int index = indexFromRowColumn(row + 1, column);
            buttons[index].doClick(0);
        }
        
        // up
        if (column > 0) {
            int index = indexFromRowColumn(row, column - 1);
            buttons[index].doClick(0);
        }
        
        //down
        if (column < 7) {
            int index = indexFromRowColumn(row, column + 1);
            buttons[index].doClick(0);
        }

        // top left
        if (row > 0 && column > 0) {
            int index = indexFromRowColumn(row - 1, column - 1);
            buttons[index].doClick(0);
        }
        
        //bottom right
        if (row < 7 && column < 7) {
            int index = indexFromRowColumn(row + 1, column + 1);
            buttons[index].doClick(0);
        }
        
        //top right
        if (row > 0 && column < 7) {
            int index = indexFromRowColumn(row - 1, column + 1);
            buttons[index].doClick(0);
        }
        
        //bottom left
        if (row < 7 && column > 0) {
            int index = indexFromRowColumn(row + 1, column - 1);
            buttons[index].doClick(0);
        }

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        ctrlKeyPressed = ke.isControlDown();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        ctrlKeyPressed = ke.isControlDown();
    }

    public int indexFromRowColumn(int row, int column) {
        return row + 8 * column;
    }

}

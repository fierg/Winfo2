package structures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class Tile extends JButton implements MouseListener {

    //Farben vom Hintergrund, highlighten und dem emptytile
    private static final Color TILE_BACKGROUND_COLOR = Color.WHITE;
    private static final Color TILE_HIGHLIGHT_COLOR = Color.YELLOW;
    private static final Color TILE_EMPTY_COLOR = Color.DARK_GRAY;

    public int number, positionID;
    private Puzzle puzzle;

    /**
     * Erstellt ein Tile für das Puzzle
     * Benötigt eine Referenz zum Puzzle, um die moveTile() Methode beim anklicken aufzurufen
     * Erhält weiterhin eine Nummer und positionID
     * @param puzzle Referenz auf das Puzzle
     * @param number Nummer, die das Tile repräsentiert
     * @param ID die positionID des Tiles
     */
    public Tile(Puzzle puzzle, int number, int ID){
        this.puzzle = puzzle;
        this.number = number;
        this.positionID = ID;
        if(number == 0){
            setBackground(TILE_EMPTY_COLOR);
        } else {
            setText("" + number);
            addMouseListener(this);
            setBackground(TILE_BACKGROUND_COLOR);
        }
        setPreferredSize(new Dimension(puzzle.getWidth() / 3, puzzle.getHeight()/3));
        setFocusPainted(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Ruft einfach moveTile mit einer Referenz auf den geklickten Button auf
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        puzzle.moveTile(this);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(TILE_HIGHLIGHT_COLOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(TILE_BACKGROUND_COLOR);
    }

    public String toString(){
        return "positionID: " + positionID + ", Number: " + number;
    }

}

package structures;

import solving.Solver;
import solving.SolverStrategy;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/*
 * Simuliert daas 8Puzzle in Form einer HashMap<Integer, Tile>.
 * Ein Tile ist hierbei ein JButton mit zusätzlichen Eigenschaften.
 * Jedes Tile bekommt eine positionID, welche die Position im Spielfeld bestimmt und eine Nummer,
 * welche das Tile repräsentiert.
 *
 * Die IDs der Felder sind hierbei von 0-8 in der HaashMap gespeichert und sind wie folgt angeordnet:
 * 0 1 2
 * 3 4 5
 * 6 7 8
 *
 * Der Grund für eine HashMap statt einem Array war, dass das Abfragen an Randpositionen leichter ist
 * (siehe getEmptyNeighbourForTile())
 */
public class Puzzle extends JFrame {

    private long seed, time;
    private JPanel panel;
    private SolverStrategy strategy;
    private int size = 3;
    private int moves;

    private int emptyTileID;

    private HashMap<Integer, Tile> tiles;
    private int[] solutionNumbers = {1,2,3,8,0,4,7,6,5};

    /**
     * Erstellt ein zufälliges Puzzle mit angegebener Fesntergröße
     * @param width Breite des Fensters
     * @param height Höhe des Fensters
     */
    public Puzzle(int width, int height){
        setSize(width, height);
        time = System.nanoTime();
        createRandomTiles();
        System.out.println(tiles);
        showPuzzle();
    }

    /**
     * Erstellt ein zufälliges Puzzle mit angegebener Fesntergröße und angegebener seed
     * @param width Breite des Fensters
     * @param height Höhe des Fensters
     * @param seed Die verwendete Seed für die Zufallsgenerierung
      */
    public Puzzle(int width, int height, long seed){
        setSize(width, height);
        this.seed = seed;
        time = System.nanoTime();
        createRandomTiles();
        showPuzzle();
    }

    /*
     * Ordnet alle Teile zufällig so an, dass mindestens eine Bewegung zum lösen notwendig ist
     */
    private void createRandomTiles() {
        tiles = new HashMap<>();
        do{
            LinkedList<Integer> numbers = new LinkedList<>();

            for(int i = 0; i < size * size; i++){
                numbers.add(i);
            }

            Random r = new Random();
            if(seed != 0){
                r.setSeed(seed);
            }

            int number;
            for(int i = 0; !numbers.isEmpty(); i++){
                number = numbers.remove(r.nextInt(numbers.size()));
                if(number == 0){
                    emptyTileID = i;
                }
                tiles.put(i, new Tile(this, number, i));
            }
        }
        while(wrongTiles() == 0);
    }

    /*
     * Ruft die Funktionen zum Anzeigen des Puzzles auf
     */
    private void showPuzzle() {
        initPanel();
        initFrame();
    }

    /*
     * Initialsiert das Panel, indem alle Tiles hinzugefügt werden und ein entsprechendes Gridlayout hinzugefügt wird
     */
    private void initPanel() {
        panel = new JPanel();
        for(int i = 0; i < size * size; i++){
            panel.add(tiles.get(i));
        }
        panel.setLayout(new GridLayout(size, size));
        System.out.println("Created and added Tiles after " + ((System.nanoTime() - time) / 1000000) + " ms");
        time = System.nanoTime();
    }

    /*
     * Initialisiert das Fenster
     */
    private void initFrame() {
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setName("8Puzzle");
        pack();
        setVisible(true);
        System.out.println("Packed and showed frame after " + ((System.nanoTime() - time) / 1000000) + " ms");
        time = System.nanoTime();
    }

    /**
     * Methode zum Bewegen eines Tiles.
     * Kann das Tile bewegt werden, so wird true zurückgegeben, ansonsten false
     * @param tile Das Tile, welches bewegt werden soll
     * @return Gibt als boolean zurück, ob das Tile bewegt werden konnte
     */
    public boolean moveTile(Tile tile) {
        Tile empty = getEmptyNeighbourForTile(tile);
        if(empty != null){
            moves++;
            //switch position in HashMap
            tiles.put(tile.positionID, empty);
            tiles.put(empty.positionID, tile);

            int tmpZOrder = panel.getComponentZOrder(empty);
            panel.setComponentZOrder(empty, panel.getComponentZOrder(tile));
            panel.setComponentZOrder(tile, tmpZOrder);

            int tmpID = empty.positionID;
            empty.positionID = tile.positionID;
            emptyTileID = empty.positionID;
            tile.positionID = tmpID;

            validate();
            repaint();
            if(wrongTiles() == 0){
                JOptionPane.showMessageDialog(this,"Gewonnen!\nDu hast " + moves + " Schritte gebraucht");
            }
            return true;
        }
        return false;
    }

    /*
     * Gibt zurück, wieviele Tiles an einer falschen Position sind
     */
    public int wrongTiles() {
        int missed = 0;
        for(int i = 0; i < size*size; i++){
            if(solutionNumbers[i] != tiles.get(i).number){
                missed++;
            }
        }
        return missed;
    }

    /**
     * Überprüft für das gegebene Tile, ob das leere Tile sein Nachbar ist.
     * Falls ja, wird es zurückgegeben,
     * falls nein, wird null zurückgegeben
     * @param tile das Tile, für welches geprüft werden soll, ob das leere Teil sein Nachbar ist
     * @return Gibt das leere Tile zurück, falls es ein Nachbar ist, ansonsten null
     */
    private Tile getEmptyNeighbourForTile(Tile tile) {
        //check if tile is above or under empty tile:
        if(tile.positionID + size == emptyTileID || tile.positionID - size == emptyTileID){
            return tiles.get(emptyTileID);
        }

        //check left side of tile:
        if(tile.positionID - 1 == emptyTileID && tile.positionID % 3 != 0){
            return tiles.get(emptyTileID);
        }

        //check right side of tile:
        if(tile.positionID + 1 == emptyTileID && emptyTileID % 3 != 0){
            return tiles.get(emptyTileID);
        }

        return null;
    }

    /**
     * Löst das Puzzle mithilfe der angegebenen SolverStrategy
     * @param strategy die zu verwendende SolverStrategy
     */
    public void solve(int delay, SolverStrategy strategy) {
        Solver solver = new Solver(delay, strategy);
    }

    /**
     * Gibt das Puzzle in Form eines zweidimensionalen Arrays zurück.
     * Falls das Arbeiten mithilfe eines Arrays später leichter ist.
     * Nachdem der nächste Schritt berechnet wurde, kann weiterhin mit moveTile() das Puzzle verändert werden.
     * @return gibt die HashMap des Puzzles in Form eines zweidimensionalen Tile Arrays zurück
     */
    public Tile[][] asArray(){
        Tile[][] tileArray = new Tile[size][size];
        int c = 0;
        for(int i = 0; i < size*size; i++){
            for(int j = 0; j < size*size; j++){
                tileArray[i][j] = tiles.get(c++);
            }
        }
        return tileArray;
    }
}

package fr.lernejo.navy_battle;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    private final char[][] sea=new char[10][10];
    private final Map<String, Boolean> ships=new HashMap<>();

    public GameState(){
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                sea[i][j]='.';
            }
        }
        placeShip("porte_avion", 5, 0, 0, true);
        placeShip("croiseur", 4, 2, 2, false);
        placeShip("contre_torpilleur_1", 3, 5, 5, true);
        placeShip("contre_torpilleur_2", 3, 7, 3, false);
        placeShip("torpilleur", 2, 9, 8, true);
    }

    public void placeShip(String name, int size, int row, int col, boolean horizontal){
        if (horizontal){
            for (int i=col;i<col+size;i++){
                sea[row][i]='S';
            }
        } else {
            for (int i=row;i<row+size;i++){
                sea[i][col]='S';
            }
        }
        ships.put(name, true);
    }

    public String fireAt(String cell) {
        int row=cell.charAt(1) - '1';
        int col=cell.charAt(0)-'A';
        if (sea[row][col]=='S'){
            sea[row][col]='X';
            return checkIfSunk(row, col)? "sunk": "hit";
        }
        return "miss";
    }


    public boolean checkIfSunk(int row, int col) {
        return sea[row][col]=='X';
    }
    public boolean shipsLeft(){
        return ships.values().contains(true);
    }
}

package ru.wishback.connect3game;

import java.util.ArrayList;
import java.util.Collections;

public class GameBrain {
    private static final int dimension = 3;
    private static final String red = "red";
    private static final String yellow = "yellow";
    private static final int me = 1;
    private static final int you = -1;
    private static final int empty = 0;

    public String whoIsNext = red;
    public String lastMove = "";
    public String win = "";

    public int moves = 0;
    public GameStatus status = GameStatus.start;

    public enum GameStatus {
        start, paly, win, end
    }

    private class Cell {
        int i;
        int j;
        int value;

        Cell(int i, int j) {
            this.i = i; this.j = j; this.value = empty;
        }

        void empty() {
            this.value = empty;
        }

    }

    private class Path implements Comparable<Path> {

        ArrayList<Cell> path;

        Path() {
            path = new ArrayList<Cell>();
        }

        public int getWeight() {
            int weight = 0;
            for (int i = 0; i < path.size(); i++ ) {
                weight = weight + path.get(i).value;
            }
            return weight;
        }

        public int getZeros() {
            int zeros = 0;
            for (int i = 0; i < path.size(); i++ ) {
                if ( path.get(i).value == empty ) {
                    zeros++;
                }
            }
            return zeros;
        }

        @Override
        public int compareTo(Path path) {
           int w1 = this.getWeight();
           int z1 = this.getZeros();
           int w2 = path.getWeight();
           int z2 = path.getZeros();
           int abs1 = Math.abs(w1);
           int abs2 = Math.abs(w2);
           int absz1 = abs1 + z1;
           int absz2 = abs2 + z2;

           if (absz1 == absz2) {
               if (absz1 == dimension) {
                   if (abs1 == abs2) {
                       return w1 > w2 ? -1 : ( w1 == w2 ?  0: 1 );
                   } else if (abs1 < abs2) {
                        if (abs1 == 0 && w2 == -1) {
                            return -1;
                       } else {
                            return 1;
                        }
                   } else {
                       if (w1 == -1 && abs2 == 0) {
                           return 1;
                       } else {
                           return -1;
                       }
                   }
               } else {
                   return z1 > z2 ? -1 : ( z1 == z2 ? 0 : 1 );
               }
           } else {
               return absz1 > absz2 ? -1 : 1;
           }
        }

    }

    private ArrayList<ArrayList<Cell>> board;
    private ArrayList<Path> paths;


    GameBrain() {
        board = new ArrayList<ArrayList<Cell>>();
        paths = new ArrayList<Path>();

        for (int i = 0; i < dimension; i++) {
            board.add(new ArrayList<Cell>());
            for( int j = 0; j < dimension; j++ ) {
                board.get(i).add(new Cell(i, j));
            }
        }

        for (int i = 0; i < dimension; i++) {
            Path path = new Path();
            paths.add(path);
            for (int j = 0; j < dimension; j++ ) {
                path.path.add(board.get(i).get(j));
            }
        }

        for (int j = 0; j < dimension; j++ ) {
            Path path = new Path();
            paths.add(path);
            for (int i = 0; i < dimension; i++ ) {
                path.path.add(board.get(i).get(j));
            }
        }

        Path path = new Path();
        paths.add(path);

        for (int i = 0; i < dimension; i++ ) {
            path.path.add(board.get(i).get(i));
        }

        path = new Path();
        paths.add(path);

        for (int i = 0; i < dimension; i++ ) {
            path.path.add(board.get(i).get(dimension - 1 - i));
        }

    }

    public void newgame() {

        whoIsNext = red;
        lastMove = "";
        win = "";
        moves = 0;
        status = GameStatus.paly;

        for (int i = 0; i < dimension; i++ ) {
            for (int j = 0; j < dimension; j++ ) {
                board.get(i).get(j).value = empty;
            }
        }
    }

    public boolean isValid(String tag) {
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        return board.get(i).get(j).value == empty && whoIsNext == red ;
    }

    private boolean place() {
        int i = 0;

        while (i < paths.size()) {
            if ( paths.get(i).getZeros() > 0 ) {
                break;
            }
            i++;
        }
        if (i == paths.size()) {
            // can't place move
            win = "end of game";
            status = GameStatus.end;
            return true;
        } else {
            int j = 0;
            while (j < paths.get(i).path.size()) {
                if (paths.get(i).path.get(j).value == empty) {
                    break;
                }
                j++;
            }
            if (j == paths.get(i).path.size()) {
                return false;
            } else {
                paths.get(i).path.get(j).value = me;
                this.lastMove = Integer.toString(paths.get(i).path.get(j).i) + Integer.toString(paths.get(i).path.get(j).j);
                return true;
            }
        }
    }

    public boolean play(String tag) {
        if ( !this.isValid(tag) ) return false;
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        board.get(i).get(j).value = you;
        whoIsNext = yellow;
        moves++;

        // check if red won
        Collections.sort(paths);
        if ( paths.get(0).getWeight() == -dimension ) {
            win = red;
            whoIsNext = "";
            status = GameStatus.win;
            return true;
        }

        // plase our move
        if (!place()) {
            whoIsNext = "";
            return false;
        }

        // check if we won
        Collections.sort(paths);
        if ( paths.get(0).getWeight() == dimension ) {
            win = yellow;
            whoIsNext = "";
            status = GameStatus.win;
            return true;
        }

        whoIsNext = red;
        return true;
    }

}

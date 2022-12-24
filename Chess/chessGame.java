package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class chessGame {

    public static String[][] board;
    public static boolean whiteToMove;
    public static ArrayList<Move> moveList = new ArrayList<>();
    boolean moveMade;

    public chessGame() {
        board = new String[][]{
                {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR",},
                {"bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP",},
                {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR",}
        };

        play();
    }

    private void play() {
        ArrayList<Integer> selected_square = new ArrayList<>();
        ArrayList<ArrayList<Integer>> clicks = new ArrayList<>();
        whiteToMove = true;
        ArrayList<Move> validMoves = getValidMoves();
        moveMade = false;

        GUI.frame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {

                if (me.getButton() == MouseEvent.BUTTON1) {
                    System.out.println(me); // debug

                    int col = Math.floorDiv(me.getX(), 96);
                    int row = Math.floorDiv(me.getY(), 96);
                    System.out.println("Col: " + col + "  Row: " + row); // debug

                    if ((!selected_square.isEmpty()) && selected_square.get(0) == row && selected_square.get(1) == col){
                        selected_square.clear();
                        clicks.clear();
                    }
                    else {
                        selected_square.clear();
                        selected_square.add(row); selected_square.add(col);

                        clicks.add(new ArrayList<>(selected_square));
                    }

                    if (clicks.size() == 2){
                        Move move = new Move(clicks.get(0), clicks.get(1));
                        System.out.println(move.getChessNotation());
                        if (validMoves.contains(move)){
                            Move.makeMove(move);
                            moveMade = true;

                            for (Component component : GUI.panel.getComponents()) {
                                if (component instanceof JLabel){
                                    GUI.panel.remove(component);
                                }
                            }

                            GUI.drawPieces();

                            GUI.panel.revalidate();
                            GUI.panel.repaint();
                        }
                        selected_square.clear();
                        clicks.clear();
                    }
                }
            }
        });
    }

    private ArrayList<Move> getValidMoves(){
        return getPossibleMoves();
    }

    private ArrayList<Move> getPossibleMoves(){
        ArrayList<Move> moves = new ArrayList<>();
        char turn, piece;

        for (int r = 0; r < board.length; r++){
            for (int c = 0; c < board[r].length; c++){
                turn = board[r][c].charAt(0);
                if ((turn == 'w' && whiteToMove) || (turn == 'b' && !whiteToMove)){
                    piece = board[r][c].charAt(1);

                    switch(piece){
                        case 'P': getPawnMoves(r, c, moves);

                        case 'N':


                        case 'B':


                        case 'R': getRookMoves(r, c, moves);


                        case 'Q':


                        case 'K':

                    }

                }
            }
        }

        return moves;
    }

    private void getPawnMoves(int r, int c, ArrayList<Move> moves){
        ArrayList<Integer> tempStart; ArrayList<Integer> tempEnd;

        if (whiteToMove){
            if (board[r - 1][c].equals("--")){
                tempStart = new ArrayList<>();
                tempStart.add(r); tempStart.add(c);

                tempEnd = new ArrayList<>();
                tempEnd.add(r - 1); tempEnd.add(c);

                moves.add(new Move(tempStart, tempEnd));

                if (r == 6 && board[r - 2][c].equals("--")){
                    tempStart = new ArrayList<>();
                    tempStart.add(r); tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 2); tempEnd.add(c);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
            if (c - 1 >= 0){
                if (board[r - 1][c - 1].charAt(0) == 'b'){
                    tempStart = new ArrayList<>();
                    tempStart.add(r); tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 1); tempEnd.add(c - 1);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
            if (c + 1 <= 7){
                if (board[r - 1][c + 1].charAt(0) == 'b'){
                    tempStart = new ArrayList<>();
                    tempStart.add(r); tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 1); tempEnd.add(c + 1);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
        }
        else {
            if (board[r + 1][c].equals("--")){
                tempStart = new ArrayList<>();
                tempStart.add(r); tempStart.add(c);

                tempEnd = new ArrayList<>();
                tempEnd.add(r + 1); tempEnd.add(c);

                moves.add(new Move(tempStart, tempEnd));

                if (r == 1 && board[r + 2][c].equals("--")){
                    tempStart = new ArrayList<>();
                    tempStart.add(r); tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 2); tempEnd.add(c);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
            if (c - 1 >= 0){
                if (board[r + 1][c - 1].charAt(0) == 'b'){
                    tempStart = new ArrayList<>();
                    tempStart.add(r); tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 1); tempEnd.add(c - 1);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
            if (c + 1 <= 7){
                if (board[r + 1][c + 1].charAt(0) == 'b'){
                    tempStart = new ArrayList<>();
                    tempStart.add(r); tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 1); tempEnd.add(c + 1);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
        }
    }

    private void getRookMoves(int r, int c, ArrayList<Move> moves){
        ArrayList<Integer> tempStart; ArrayList<Integer> tempEnd;
        int[][] directions = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        char enemyColor;
        int endRow, endCol;
        String endPiece;

        if (whiteToMove){enemyColor = 'b';}
        else {enemyColor = 'w';}

        for (int[] d: directions){
            for (int i = 1; i <= 8; i++){
                endRow = r + d[0] * i;
                endCol = c + d[1] * i;

                if (endRow <= 0 && endRow < 8 && endCol <= 0 && endCol < 8){
                    endPiece = board[endRow][endCol];

                    if (endPiece.equals("--") || endPiece.charAt(0) == enemyColor){
                        tempStart = new ArrayList<>();
                        tempStart.add(r); tempStart.add(c);

                        tempEnd = new ArrayList<>();
                        tempEnd.add(endRow); tempEnd.add(endCol);

                        moves.add(new Move(tempStart, tempEnd));

                        if (endPiece.charAt(0) == enemyColor){break;}
                    }
                    else {break;}
                }
                else {break;}
            }
        }
    }
}
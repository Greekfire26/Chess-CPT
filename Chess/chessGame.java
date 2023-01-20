package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class chessGame {

    public static String[][] board;
    public static boolean whiteToMove;
    public static ArrayList<Move> moveList = new ArrayList<>();
    public static ArrayList<Move> validMoves;
    public static ArrayList<ArrayList<Integer>> clicks;
    boolean moveMade;
    public static int[] wKingLoc = {7, 4};
    public static int[] bKingLoc = {0, 4};
    public static boolean checkmate = false;
    public static boolean stalemate = false;
    public static ArrayList<Integer> enpassantPossible = new ArrayList<>();
    public static int wins;

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
        clicks = new ArrayList<>();
        whiteToMove = true;
        validMoves = getValidMoves();
        moveMade = false;

        GUI.frame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {

                if (me.getButton() == MouseEvent.BUTTON1) {
                    System.out.println(me); // debug

                    int col = Math.floorDiv(me.getX(), 96);
                    int row = Math.floorDiv(me.getY(), 96);
                    System.out.println("Col: " + col + "  Row: " + row); // debug

                    if ((!selected_square.isEmpty()) && selected_square.get(0) == row && selected_square.get(1) == col) {
                        selected_square.clear();
                        clicks.clear();
                    } else {
                        selected_square.clear();
                        selected_square.add(row);
                        selected_square.add(col);

                        clicks.add(new ArrayList<>(selected_square));
                    }

                    if (clicks.size() == 2) {
                        Move move = new Move(clicks.get(0), clicks.get(1));
                        System.out.println(move.getChessNotation());
                        for (Move current_move : validMoves) {
                            if (move.equals(current_move)) {
                                Move.makeMove(current_move);
                                moveMade = true;

                                for (Component component : GUI.panel.getComponents()) {
                                    if (component instanceof JLabel) {
                                        GUI.panel.remove(component);
                                    }
                                }

                                GUI.drawPieces();

                                GUI.panel.revalidate();
                                GUI.panel.repaint();

                                selected_square.clear();
                                clicks.clear();
                            }
                        }
                        if (!moveMade) {
                            clicks.clear();
                            selected_square.clear();
                        }
                    }

                    if (moveMade) {
                        validMoves = getValidMoves();
                        moveMade = false;

                        if (checkmate || stalemate) {
                            try {
                                gameover();
                            } catch (IOException e){e.printStackTrace();}
                        }

                    }
                }
            }
        });

        GUI.frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'p' && (checkmate || stalemate)){
                    GUI.frame.dispose();
                    new GUI();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    private ArrayList<Move> getValidMoves() {
        ArrayList<Integer> tempEnPassantPossible = enpassantPossible;
        ArrayList<Move> moves = getPossibleMoves();

        for (int i = moves.size() - 1; i >= 0; i--) {
            Move.makeMove(moves.get(i));
            whiteToMove = !whiteToMove;
            if (inCheck()) {
                moves.remove(moves.get(i));
            }
            whiteToMove = !whiteToMove;
            Move.undoMove();
        }

        if (moves.size() == 0) {
            if (inCheck()) {
                checkmate = true;
            } else {
                stalemate = true;
            }
        } else {
            checkmate = false;
            stalemate = false;
        }

        enpassantPossible = tempEnPassantPossible;
        return moves;
    }

    private boolean inCheck() {
        if (whiteToMove) {
            return underAttack(wKingLoc[0], wKingLoc[1]);
        } else {
            return underAttack(bKingLoc[0], bKingLoc[1]);
        }
    }

    private boolean underAttack(int r, int c) {
        whiteToMove = !whiteToMove;
        ArrayList<Move> oppMoves = getPossibleMoves();
        whiteToMove = !whiteToMove;

        for (int i = 0; i < oppMoves.size(); i++) {
            if ((oppMoves.get(i).endRow == r) && (oppMoves.get(i).endCol == c)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        char turn, piece;

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                turn = board[r][c].charAt(0);
                if ((turn == 'w' && whiteToMove) || (turn == 'b' && whiteToMove == false)) {
                    piece = board[r][c].charAt(1);

                    switch (piece) {
                        case 'P':
                            getPawnMoves(r, c, moves);
                            break;
                        case 'N':
                            getKnightMoves(r, c, moves);
                            break;
                        case 'B':
                            getBishopMoves(r, c, moves);
                            break;
                        case 'R':
                            getRookMoves(r, c, moves);
                            break;
                        case 'Q':
                            getQueenMoves(r, c, moves);
                            break;
                        case 'K':
                            getKingMoves(r, c, moves);
                            break;
                    }
                }
            }
        }

        return moves;
    }

    private void getPawnMoves(int r, int c, ArrayList<Move> moves) {
        ArrayList<Integer> tempStart;
        ArrayList<Integer> tempEnd;

        if (whiteToMove) {
            if (board[r - 1][c].equals("--")) {
                tempStart = new ArrayList<>();
                tempStart.add(r);
                tempStart.add(c);

                tempEnd = new ArrayList<>();
                tempEnd.add(r - 1);
                tempEnd.add(c);

                moves.add(new Move(tempStart, tempEnd));

                if (r == 6 && board[r - 2][c].equals("--")) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 2);
                    tempEnd.add(c);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
            if (c - 1 >= 0) {
                if (board[r - 1][c - 1].charAt(0) == 'b') {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 1);
                    tempEnd.add(c - 1);

                    moves.add(new Move(tempStart, tempEnd));
                } else if (enpassantPossible.size() == 2 && r - 1 == enpassantPossible.get(0) && c - 1 == enpassantPossible.get(1)) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 1);
                    tempEnd.add(c - 1);

                    moves.add(new Move(tempStart, tempEnd, true));
                }
            }
            if (c + 1 <= 7) {
                if (board[r - 1][c + 1].charAt(0) == 'b') {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 1);
                    tempEnd.add(c + 1);

                    moves.add(new Move(tempStart, tempEnd));
                } else if (enpassantPossible.size() == 2 && r - 1 == enpassantPossible.get(0) && c + 1 == enpassantPossible.get(1)) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r - 1);
                    tempEnd.add(c + 1);

                    moves.add(new Move(tempStart, tempEnd, true));
                }
            }
        } else {
            if (board[r + 1][c].equals("--")) {
                tempStart = new ArrayList<>();
                tempStart.add(r);
                tempStart.add(c);

                tempEnd = new ArrayList<>();
                tempEnd.add(r + 1);
                tempEnd.add(c);

                moves.add(new Move(tempStart, tempEnd));

                if (r == 1 && board[r + 2][c].equals("--")) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 2);
                    tempEnd.add(c);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
            if (c - 1 >= 0) {
                if (board[r + 1][c - 1].charAt(0) == 'b') {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 1);
                    tempEnd.add(c - 1);

                    moves.add(new Move(tempStart, tempEnd));
                } else if (enpassantPossible.size() == 2 && r + 1 == enpassantPossible.get(0) && c - 1 == enpassantPossible.get(1)) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 1);
                    tempEnd.add(c - 1);

                    moves.add(new Move(tempStart, tempEnd, true));
                }
            }
            if (c + 1 <= 7) {
                if (board[r + 1][c + 1].charAt(0) == 'b') {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 1);
                    tempEnd.add(c + 1);

                    moves.add(new Move(tempStart, tempEnd));
                } else if (enpassantPossible.size() == 2 && r + 1 == enpassantPossible.get(0) && c + 1 == enpassantPossible.get(1)) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(r + 1);
                    tempEnd.add(c + 1);

                    moves.add(new Move(tempStart, tempEnd, true));
                }
            }
        }
    }

    private void getRookMoves(int r, int c, ArrayList<Move> moves) {
        ArrayList<Integer> tempStart;
        ArrayList<Integer> tempEnd;
        int[][] directions = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        char enemy;
        int endRow, endCol;
        String endPiece;

        if (whiteToMove) {
            enemy = 'b';
        } else {
            enemy = 'w';
        }

        for (int[] d : directions) {
            for (int i = 1; i < 8; i++) {
                endRow = r + d[0] * i;
                endCol = c + d[1] * i;

                if ((endRow >= 0 && endRow < 8) && (endCol >= 0 && endCol < 8)) {
                    endPiece = board[endRow][endCol];
                    if (endPiece.equals("--")) {
                        tempStart = new ArrayList<>();
                        tempStart.add(r);
                        tempStart.add(c);

                        tempEnd = new ArrayList<>();
                        tempEnd.add(endRow);
                        tempEnd.add(endCol);

                        moves.add(new Move(tempStart, tempEnd));
                    } else if (endPiece.charAt(0) == enemy) {
                        tempStart = new ArrayList<>();
                        tempStart.add(r);
                        tempStart.add(c);

                        tempEnd = new ArrayList<>();
                        tempEnd.add(endRow);
                        tempEnd.add(endCol);

                        moves.add(new Move(tempStart, tempEnd));

                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void getBishopMoves(int r, int c, ArrayList<Move> moves) {
        ArrayList<Integer> tempStart;
        ArrayList<Integer> tempEnd;
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        char enemy;
        int endRow, endCol;
        String endPiece;

        if (whiteToMove) {
            enemy = 'b';
        } else {
            enemy = 'w';
        }

        for (int[] d : directions) {
            for (int i = 1; i < 8; i++) {
                endRow = r + d[0] * i;
                endCol = c + d[1] * i;

                if ((endRow >= 0 && endRow < 8) && (endCol >= 0 && endCol < 8)) {
                    endPiece = board[endRow][endCol];

                    if (endPiece.equals("--")) {
                        tempStart = new ArrayList<>();
                        tempStart.add(r);
                        tempStart.add(c);

                        tempEnd = new ArrayList<>();
                        tempEnd.add(endRow);
                        tempEnd.add(endCol);

                        moves.add(new Move(tempStart, tempEnd));
                    } else if (endPiece.charAt(0) == enemy) {
                        tempStart = new ArrayList<>();
                        tempStart.add(r);
                        tempStart.add(c);

                        tempEnd = new ArrayList<>();
                        tempEnd.add(endRow);
                        tempEnd.add(endCol);

                        moves.add(new Move(tempStart, tempEnd));

                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void getKnightMoves(int r, int c, ArrayList<Move> moves) {
        ArrayList<Integer> tempStart;
        ArrayList<Integer> tempEnd;
        int[][] directions = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
        char ally;
        int endRow, endCol;
        String endPiece;

        if (whiteToMove) {
            ally = 'w';
        } else {
            ally = 'b';
        }

        for (int d[] : directions) {
            endRow = r + d[0];
            endCol = c + d[1];

            if ((endRow >= 0 && endRow < 8) && (endCol >= 0 && endCol < 8)) {
                endPiece = board[endRow][endCol];

                if (endPiece.charAt(0) != ally) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(endRow);
                    tempEnd.add(endCol);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
        }
    }

    private void getQueenMoves(int r, int c, ArrayList<Move> moves) {
        getRookMoves(r, c, moves);
        getBishopMoves(r, c, moves);
    }

    private void getKingMoves(int r, int c, ArrayList<Move> moves) {
        ArrayList<Integer> tempStart;
        ArrayList<Integer> tempEnd;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        char ally;
        int endRow, endCol;
        String endPiece;

        if (whiteToMove) {
            ally = 'w';
        } else {
            ally = 'b';
        }

        for (int i = 0; i < 8; i++) {
            endRow = r + directions[i][0];
            endCol = c + directions[i][1];

            if ((endRow >= 0 && endRow < 8) && (endCol >= 0 && endCol < 8)) {
                endPiece = board[endRow][endCol];

                if (endPiece.charAt(0) != ally) {
                    tempStart = new ArrayList<>();
                    tempStart.add(r);
                    tempStart.add(c);

                    tempEnd = new ArrayList<>();
                    tempEnd.add(endRow);
                    tempEnd.add(endCol);

                    moves.add(new Move(tempStart, tempEnd));
                }
            }
        }
    }

    public void gameover() throws IOException{
        if (checkmate) {

            if (!whiteToMove) { // white won
                Scanner scanner = new Scanner(new File("src/Chess/whiteWins.txt"));
                wins = scanner.nextInt() + 1;
                FileWriter writer = new FileWriter("src/Chess/whiteWins.txt", false);
                PrintWriter pw = new PrintWriter(writer);
                pw.print(wins);
                pw.close();

            } else if (whiteToMove){ // black won
                Scanner scanner = new Scanner(new File("src/Chess/blackWins.txt"));
                wins = scanner.nextInt() + 1;
                FileWriter writer = new FileWriter("src/Chess/blackWins.txt", false);
                PrintWriter pw = new PrintWriter(writer);
                pw.print(wins);
                pw.close();
            }

        GUI.gameoverMessage();
        }
    }
}
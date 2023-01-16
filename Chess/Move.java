package Chess;

import java.util.ArrayList;
import java.util.HashMap;

public class Move {

    private int startRow;
    private int startCol;
    public int endRow;
    public int endCol;
    private String movedPiece;
    private String capturedPiece;
    public boolean pawnPromotion;
    public static boolean isEnPassantMove;
    private int moveID;

    private HashMap<String, Integer> ranksToRows = new HashMap();
    private HashMap<Integer, String> rowsToRanks = new HashMap();
    private HashMap<String, Integer> filesToCols = new HashMap();
    private HashMap<Integer, String> colsToFiles = new HashMap();

    public Move(ArrayList<Integer> startSq, ArrayList<Integer> endSq){
        startRow = startSq.get(0); startCol = startSq.get(1);
        endRow = endSq.get(0); endCol = endSq.get(1);
        movedPiece = chessGame.board[startRow][startCol];
        capturedPiece = chessGame.board[endRow][endCol];
        pawnPromotion = (movedPiece.equals("wP") && endRow == 0) || (movedPiece.equals("bP") && endRow == 7);
        isEnPassantMove = false;
        if (isEnPassantMove){
            if (movedPiece.equals("bP")){capturedPiece = "wP";}
            else {capturedPiece = "bP";}
        }
        moveID = startRow * 1000 + startCol * 100 + endRow * 10 + endCol;

        fillReferenceTables();
    }

    public Move(ArrayList<Integer> startSq, ArrayList<Integer> endSq, boolean enPassant){
        startRow = startSq.get(0); startCol = startSq.get(1);
        endRow = endSq.get(0); endCol = endSq.get(1);
        movedPiece = chessGame.board[startRow][startCol];
        capturedPiece = chessGame.board[endRow][endCol];
        pawnPromotion = (movedPiece.equals("wP") && endRow == 0) || (movedPiece.equals("bP") && endRow == 7);
        isEnPassantMove = enPassant;
        if (isEnPassantMove){
            if (movedPiece.equals("bP")){capturedPiece = "wP";}
            else {capturedPiece = "bP";}
        }
        moveID = startRow * 1000 + startCol * 100 + endRow * 10 + endCol;

        fillReferenceTables();
    }

    public static void makeMove(Move move){
        chessGame.board[move.startRow][move.startCol] = "--";
        chessGame.board[move.endRow][move.endCol] = move.movedPiece;
        chessGame.moveList.add(move);
        chessGame.whiteToMove = !chessGame.whiteToMove;

        if (move.movedPiece.equals("wK")){
            chessGame.wKingLoc[0] = move.endRow;
            chessGame.wKingLoc[1] = move.endCol;
        }

        else if (move.movedPiece.equals("bK")){
            chessGame.bKingLoc[0] = move.endRow;
            chessGame.bKingLoc[1] = move.endCol;
        }

        if (move.pawnPromotion){
            chessGame.board[move.endRow][move.endCol] = move.movedPiece.charAt(0) + "Q";
        }

        if (move.isEnPassantMove){
            chessGame.board[move.startRow][move.endCol] = "--";
        }

        if (move.movedPiece.charAt(1) == 'P' && Math.abs(move.startRow - move.endRow) == 2){
            chessGame.enpassantPossible.clear();
            chessGame.enpassantPossible.add((move.startRow + move.endRow) / 2 );
            chessGame.enpassantPossible.add(move.startCol);
        }
        else {
            chessGame.enpassantPossible.clear();
        }
    }

    public static void undoMove(){
        if (chessGame.moveList.size() != 0){
            Move move = chessGame.moveList.get(chessGame.moveList.size() - 1);
            chessGame.moveList.remove(chessGame.moveList.size() - 1);
            chessGame.board[move.startRow][move.startCol] = move.movedPiece;
            chessGame.board[move.endRow][move.endCol] = move.capturedPiece;
            chessGame.whiteToMove = !chessGame.whiteToMove;

            if (move.movedPiece.equals("wK")){
                chessGame.wKingLoc[0] = move.startRow;
                chessGame.wKingLoc[1] = move.startCol;
            }

            else if (move.movedPiece.equals("bK")){
                chessGame.bKingLoc[0] = move.startRow;
                chessGame.bKingLoc[1] = move.startCol;
            }

            if (move.isEnPassantMove){
                chessGame.board[move.endRow][move.endCol] = "--";
                chessGame.board[move.endRow][move.endCol] = move.capturedPiece;

                chessGame.enpassantPossible.clear();
                chessGame.enpassantPossible.add(move.endRow);
                chessGame.enpassantPossible.add(move.endCol);
            }

            if (move.movedPiece.charAt((1)) == 'P' && Math.abs(move.startRow - move.endRow) == 2){
                chessGame.enpassantPossible.clear();
            }
        }
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Move){
            return ((Move) object).moveID == this.moveID;
        }
        return false;
    }

    public String getChessNotation(){
        return getRankFile(startRow, startCol) + getRankFile(endRow, endCol);
    }

    public String getRankFile(int r, int c){
        return colsToFiles.get(c) + rowsToRanks.get(r);
    }

    void fillReferenceTables(){ // ik this is cancer, deal with it
        ranksToRows.put("1", 7);
        ranksToRows.put("2", 6);
        ranksToRows.put("3", 5);
        ranksToRows.put("4", 4);
        ranksToRows.put("5", 3);
        ranksToRows.put("6", 2);
        ranksToRows.put("7", 1);
        ranksToRows.put("8", 0);

        rowsToRanks.put(7, "1");
        rowsToRanks.put(6, "2");
        rowsToRanks.put(5, "3");
        rowsToRanks.put(4, "4");
        rowsToRanks.put(3, "5");
        rowsToRanks.put(2, "6");
        rowsToRanks.put(1, "7");
        rowsToRanks.put(0, "8");

        filesToCols.put("a", 0);
        filesToCols.put("b", 1);
        filesToCols.put("c", 2);
        filesToCols.put("d", 3);
        filesToCols.put("e", 4);
        filesToCols.put("f", 5);
        filesToCols.put("g", 6);
        filesToCols.put("h", 7);

        colsToFiles.put(0, "a");
        colsToFiles.put(1, "b");
        colsToFiles.put(2, "c");
        colsToFiles.put(3, "d");
        colsToFiles.put(4, "e");
        colsToFiles.put(5, "f");
        colsToFiles.put(6, "g");
        colsToFiles.put(7, "h");
    }
}
package Chess;

import java.util.ArrayList;
import java.util.HashMap;

public class Move {

    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private String movedPiece;
    private String capturedPiece;

    private HashMap<String, Integer> ranksToRows = new HashMap();
    private HashMap<Integer, String> rowsToRanks = new HashMap();
    private HashMap<String, Integer> filesToCols = new HashMap();
    private HashMap<Integer, String> colsToFiles = new HashMap();

    public Move(ArrayList<Integer> startSq, ArrayList<Integer> endSq){
        startRow = startSq.get(0); startCol = startSq.get(1);
        endRow = endSq.get(0); endCol = endSq.get(1);
        movedPiece = chessGame.board[startRow][startCol];
        capturedPiece = chessGame.board[endRow][endCol];

        fillReferenceTables();
    }

    public static void makeMove(Move move){
        chessGame.board[move.startRow][move.startCol] = "--";
        chessGame.board[move.endRow][move.endCol] = move.movedPiece;
        chessGame.movelist.add(move);
        chessGame.whiteToMove = !chessGame.whiteToMove;
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
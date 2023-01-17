package Chess;

public class castleRights {
    static public boolean wks;
    static public boolean wqs;
    static public boolean bks;
    static public boolean bqs;
    public castleRights(boolean wksInput, boolean wqsInput, boolean bksInput, boolean bqsInput){
        wks = wksInput;
        wqs = wqsInput;
        bks = bksInput;
        bqs = bqsInput;
    }
}
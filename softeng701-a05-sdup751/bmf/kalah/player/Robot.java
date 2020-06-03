package kalah.player;

import com.qualitascorpus.testsupport.IO;
import kalah.board.Bank;
import kalah.board.Board;

public class Robot implements PlayerInterface {

    private IO io;
    private int playerID;
    private int sizeOfBoard;
    private Board board;
    private String hollowPicked;
    private MoveResult move;

    private enum MoveResult {
        AdditionalMove,
        CaptureMove,
        LegalMove,
    }

    public Robot(IO io, int playerID, Board board) {
        this.io = io;
        this.playerID = playerID;
        this.board = board;

        this.sizeOfBoard = this.board.getTotalHollowCount()*2 + 2; // hollow*players + 1 bank per player
    }

    @Override
    public String promptPlayer() {

        // Returns hollow selected as string && updates this.move before switch case
        this.hollowPicked = selectHollow();

        switch (this.move) {
            case AdditionalMove:
                io.println("Player P" + this.playerID +
                           " (Robot) chooses house #" + this.hollowPicked + " because it leads to an extra move");
                break;
            case CaptureMove:
                io.println("Player P" + this.playerID +
                        " (Robot) chooses house #" + this.hollowPicked + " because it leads to a capture");
                break;
            case LegalMove:
                io.println("Player P" + this.playerID +
                        " (Robot) chooses house #" + this.hollowPicked + " because it is the first legal move");
                break;

            default:
        }

        return this.hollowPicked;
    }

    private String selectHollow() {

        int lastSownPlace;
        int stepsToTake;
        int index;

        //check for Additional Move
        for (int i = 0; i < this.board.getTotalHollowCount(); i++) {
            index = this.board.getArrayIndex(playerID, i);
            stepsToTake = this.board.getHollowSeedCount(this.playerID, i);
            lastSownPlace = finalIndex(stepsToTake, index);

            if (this.board.get(lastSownPlace).getPlayer() == playerID && this.board.get(lastSownPlace) instanceof Bank) {
                this.move = MoveResult.AdditionalMove;
                return Integer.toString(i + 1);
            }
        }

        //Check for Capture
        for (int i = 0; i < this.board.getTotalHollowCount(); i++) {
            index = this.board.getArrayIndex(playerID, i);
            stepsToTake = this.board.getHollowSeedCount(this.playerID, i);
            lastSownPlace = finalIndex(stepsToTake, index);

            if ((stepsToTake > 0) && checkCapture(lastSownPlace, index)) {
                    this.move = MoveResult.CaptureMove;
                    return Integer.toString(i + 1);
            }
        }

        //Check for Legal Move
        for (int i = 0; i < this.board.getTotalHollowCount(); i++) {

            if (this.board.getHollowSeedCount(playerID, i) > 0) {
                this.move = MoveResult.LegalMove;
                return Integer.toString(i + 1);
            }
        }

        return "Error: No move can be determined (Robot)";
    }

    private Boolean checkCapture(int lastSownPlace, int index) {
        if (this.board.get(lastSownPlace).getPlayer() == playerID
                && this.board.get(lastSownPlace).getSeeds() == 0) {
            if (index > lastSownPlace) {
                return true;
            } else {
                if (this.board.get(this.sizeOfBoard - 2 - lastSownPlace).getSeeds() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int finalIndex(int stepsToTake, int index) {

        int tally = ((index + stepsToTake) % this.sizeOfBoard);

        for (int j = 0; j < stepsToTake; j++) {
            // if oppositions bank is reached - skip
            if (this.board.get((index + j + 1) % this.sizeOfBoard) instanceof Bank &&
                    this.board.get((index + j + 1) % this.sizeOfBoard).getPlayer() != playerID) {
                tally++;
            }
        }
        return tally;
    }
}

package kalah.player;

import com.qualitascorpus.testsupport.IO;

public class Player implements PlayerInterface {

    private IO io;
    private int playerID;

    public Player(IO io, int playerID) {
        this.io = io;
        this.playerID = playerID;
    }

    @Override
    public String promptPlayer() {
        return io.readFromKeyboard("Player P" + this.playerID + "'s turn - Specify house number or 'q' to quit: ");
    }

}

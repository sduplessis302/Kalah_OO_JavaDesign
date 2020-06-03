package kalah.player;

import com.qualitascorpus.testsupport.IO;

public class Robot implements PlayerInterface {

    private IO io;
    private int playerID;

    public Robot(IO io, int playerID) {
        this.io = io;
        this.playerID = playerID;
    }

    @Override
    public String promptHumanPlayer() {
        // implement decesion maker

        return io.readFromKeyboard("Player P" + this.playerID + "'s (Robot) turn - Specify house number or 'q' to quit: ");
    }

}

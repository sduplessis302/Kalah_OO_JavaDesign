package kalah.display;

import com.qualitascorpus.testsupport.IO;
import kalah.board.Board;

public class PrintOutput implements DisplayInterface {

    private IO io;
    private Board board;

    public PrintOutput(IO io, Board board) {

        this.io = io;
        this.board = board;

    }

    public void printBoard() {
        io.println(printBorder());
        io.println(printPlayer(2));
        io.println(printMedian());
        io.println(printPlayer(1));
        io.println(printBorder());
    }

    public void printScore() {

        int topScore = 0;
        int winner = 0;

        for (int i = 0; i < this.board.getNumberOfPlayers(); i++) {
            int tally = 0;
            for (int j = 0; j <= this.board.getTotalHollowCount(); j++) {
                tally += this.board.getHollowSeedCount(i + 1, j);
            }

            io.println("\tplayer " + (i + 1) + ":" + tally);

            if (topScore < tally) {
                topScore = tally;
                winner = i + 1;
            } else if (topScore == tally) {
                winner = 0;
            }
        }

        if (winner == 0) { //tie
            io.println("A tie!");
        } else {
            io.println("Player " + winner + " wins!");
        }

    }

    public void invalidMoveMade() {
        io.println("House is empty. Move again.");
    }

    public void houseDoesNotExist() {
        io.println("House does not exist, choose from house 1 to "
                + this.board.getTotalHollowCount() + ": ");
    }

    private String printBorder() {
        StringBuilder s = new StringBuilder();
        s.append("+----");
        for (int i = 0; i < this.board.getTotalHollowCount(); i++) {
            s.append("+-------");
        }
        s.append("+----+");
        return s.toString();
    }

    private String printMedian() {
        StringBuilder s = new StringBuilder();
        s.append("|    |");
        for (int i = 0; i < this.board.getTotalHollowCount() - 1; i++) {
            s.append("-------+");
        }
        s.append("-------|    |");
        return s.toString();
    }

    private String printPlayer(int player) {
        StringBuilder s = new StringBuilder();

        if (player == 2) {
            s.append("| P2 |");
            for (int i = this.board.getTotalHollowCount() - 1; i >= 0; i--) {
                s.append(" " + (i + 1) + "[");
                s.append(String.format("%2d", this.board.getHollowSeedCount(player, i)));
                s.append("] |");
            }
            s.append(" " + String.format("%2d",
                    this.board.getHollowSeedCount(1, (this.board.getTotalHollowCount()))) + " |");
        } else {
            s.append("| " + String.format("%2d",
                    this.board.getHollowSeedCount(2, (this.board.getTotalHollowCount()))) + " |");
            for (int i = 0; i < this.board.getTotalHollowCount(); i++) {
                s.append(" " + (i + 1) + "[");
                s.append(String.format("%2d", this.board.getHollowSeedCount(player, i)));
                s.append("] |");
            }
            s.append(" P1 |");
        }
        return s.toString();
    }
}

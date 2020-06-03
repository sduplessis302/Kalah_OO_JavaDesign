package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.board.Board;
import kalah.board.KalahBoard;
import kalah.board.NextTurn;
import kalah.display.PrintOutput;
import kalah.player.Player;
import kalah.player.PlayerInterface;
import kalah.player.Robot;

import java.util.ArrayList;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure.
 */
public class Kalah {

    private String orientation = "Vertical"; // "Horizontal" or "Vertical"
    private Board board;
    private ArrayList<PlayerInterface> player = new ArrayList<>();
    private int initialBankVal = 0;
    private int bank = 1;
    private int numberOfPlayers = 2;
    private int currentPlayer = 1;

    // Toggle initial Parameters
    private int hollows = 6;
    private int initialHollowVal = 4;

    public Kalah() {
        this.board = new KalahBoard(this.numberOfPlayers, this.bank,
                                    this.hollows, this.initialBankVal,
                                    this.initialHollowVal);
    }

    public static void main(String[] args) {
        new Kalah().play(new MockIO());
    }

    public void play(IO io) {

        PrintOutput print = new PrintOutput(io, board, orientation);
        print.printBoard();

        this.player.add(new Player(io, 1));
        this.player.add(new Robot(io, 2));

        String input = this.player.get(currentPlayer-1).promptHumanPlayer();

        boolean gameFinsihed = false;

        while (true) {

            if (input.equals("q")) {
                break;
            }

            if (Integer.parseInt(input) > this.board.getTotalHollowCount()
                    || Integer.parseInt(input) <= 0) { // check input bounds
                print.houseDoesNotExist();
            } else {
                NextTurn turn = this.board.performPlayersTurn(currentPlayer, Integer.parseInt(input),
                        this.board.hollowPicked(currentPlayer, Integer.parseInt(input)));

                if (turn == NextTurn.HouseEmpty) {
                    print.invalidMoveMade();
                    print.printBoard();
                } else if (turn == NextTurn.MoveAgain) {
                    print.printBoard();
                } else if (turn == NextTurn.NextPlayer) {
                    getNextPlayer(currentPlayer);
                    print.printBoard();
                }

                if (this.board.testIfMovePossible(currentPlayer) == false) {
                    gameFinsihed = true;
                    break;
                }
            }

            input = this.player.get(currentPlayer-1).promptHumanPlayer();
        }

        io.println("Game over");
        print.printBoard();

        if (gameFinsihed) {
            print.printScore();
        }
    }

    private void getNextPlayer(int currentPlayer) {
        // increment player
        if (currentPlayer == 1) {
            this.currentPlayer = 2;
        } else if (currentPlayer == 2) {
            this.currentPlayer = 1;
        }
    }

}

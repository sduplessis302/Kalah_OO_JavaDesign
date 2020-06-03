package kalah.board;

import java.util.ArrayList;


public class KalahBoard implements Board {

    private ArrayList<StorageElement> board = new ArrayList<>();

    private int bank;
    private int hollows;
    private int initialBankVal;
    private int initialHollowVal;
    private int numberOfPlayers;
    private int seedsAvailablePerPlayer;

    private int sizeOfBoard;

    public KalahBoard(int numberOfPlayers, int bank, int hollows,
                      int initialBankVal, int initialHollowVal) {

        this.bank = bank;
        this.hollows = hollows;
        this.initialBankVal = initialBankVal;
        this.initialHollowVal = initialHollowVal;
        this.numberOfPlayers = numberOfPlayers;
        this.seedsAvailablePerPlayer = this.hollows * this.initialHollowVal;
        this.sizeOfBoard = this.numberOfPlayers * (this.bank + this.hollows);

        constructBoard();
    }

    @Override
    public boolean testIfMovePossible(int player) {

        int indexCheck = (player - 1) * (this.hollows + this.bank);

        for (int i = 0; i < this.hollows; i++) {
            if (this.board.get(indexCheck + i).getSeeds() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public NextTurn performPlayersTurn(int player, int house, int seedsToSow) {
        //determines play again, empty house
        if (seedsToSow == 0) { //empty house was picked
            return NextTurn.HouseEmpty;
        }

        int currentIndex = (player - 1) * (this.hollows + this.bank) + house;
        int toSow = seedsToSow;
        int lastIndexSown = 0;

        while (toSow > 0) {
            if (this.board.get(currentIndex) instanceof Hollow || this.board.get(currentIndex).getPlayer() == player) { // sow only if it not the opponents stores
                this.board.get(currentIndex).addSeeds(1);
                toSow--;
            }
            lastIndexSown = currentIndex;
            currentIndex++;

            if (currentIndex > 13) {
                currentIndex = 0;
            }
        }

        //check if player can play again
        if (this.board.get(lastIndexSown).getPlayer() == player && this.board.get(lastIndexSown) instanceof Bank) {
            return NextTurn.MoveAgain;
        } else if (this.board.get(lastIndexSown).getPlayer() == player && this.board.get(lastIndexSown).getSeeds() == 1) {
            // find opposite
            captureOpposite(player, lastIndexSown);
        }

        return NextTurn.NextPlayer;
    }

    @Override
    public int hollowPicked(int player, int house) {
        return this.board.get(((this.hollows + this.bank) * (player - 1) + house - 1)).removeSeeds();
    }

    @Override
    public int getHollowSeedCount(int player, int houseNum) {
        return board.get(getArrayIndex(player, houseNum)).getSeeds();
    }


    public int getTotalHollowCount() {
        return this.hollows;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    private void captureOpposite(int player, int lastIndexSown) {
        // find store index
        if (this.board.get(this.sizeOfBoard - 2 - lastIndexSown).getSeeds() > 0) {
            int storeIndex = (player - 1) * (this.hollows + this.bank) + this.hollows;
            int myPit = this.board.get(lastIndexSown).getSeeds();
            int capturedPit = this.board.get(this.sizeOfBoard - lastIndexSown - 2).getSeeds();
            int seedsToStore = myPit + capturedPit;

            this.board.get(storeIndex).addSeeds(seedsToStore);
            this.board.get(lastIndexSown).setSeeds(0);
            this.board.get(this.sizeOfBoard - lastIndexSown - 2).setSeeds(0);
        }
    }

    private void constructBoard() {
        for (int i = 0; i < this.numberOfPlayers; i++) {
            for (int j = 0; j < this.hollows; j++) {
                this.board.add(new Hollow(i + 1, this.initialHollowVal));
            }

            for (int j = 0; j < this.bank; j++) {
                this.board.add(new Bank(i + 1, this.initialBankVal));
            }
        }
    }

    private int getArrayIndex(int player, int pitNum) {
        return ((this.hollows + this.bank) * (player - 1) + pitNum) % sizeOfBoard;
    }

}

package kalah.board;

public interface Board {

    boolean testIfMovePossible(int player);

    int hollowPicked(int player, int house);

    NextTurn performPlayersTurn(int player, int house, int seedsToSow);

    int getHollowSeedCount(int player, int houseNum);

    int getTotalHollowCount();

    int getNumberOfPlayers();
}

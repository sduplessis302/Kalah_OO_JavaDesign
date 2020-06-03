package kalah.board;

public abstract class StorageElement {
    private int player;
    private int seeds;

    public StorageElement(int player, int seeds) {
        this.player = player;
        this.seeds = seeds;
    }

    public void addSeeds(int seed) {
        this.seeds = this.seeds + seed;
    }

    public int removeSeeds() {
        int temp = this.seeds;
        this.seeds = 0;
        return temp;
    }

    public int getPlayer() {
        return player;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }
}

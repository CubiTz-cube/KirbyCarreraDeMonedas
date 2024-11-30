package src.screens;

public class ScorePlayer implements Comparable<ScorePlayer> {
    public String name;
    public Integer score;

    public ScorePlayer(String name) {
        this.name = name;
        this.score = 0;
    }

    @Override
    public int compareTo(ScorePlayer o) {
        return o.score.compareTo(score);
    }
}

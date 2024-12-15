package src.utils;

public class ScorePlayer implements Comparable<ScorePlayer> {
    public Integer id;
    public String name;
    public Integer score;

    public ScorePlayer(Integer id,String name) {
        this.id = id;
        this.name = name;
        this.score = 0;
    }

    @Override
    public int compareTo(ScorePlayer o) {
        return o.score.compareTo(score);
    }
}

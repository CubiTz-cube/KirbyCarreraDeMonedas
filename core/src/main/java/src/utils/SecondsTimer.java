package src.utils;

public class SecondsTimer {
    private final Float saveTime;
    private Float timer;
    private Boolean finished;

    public SecondsTimer(Integer minutes, Integer seconds){
        float time = minutes * 60 + seconds;
        saveTime = time;
        timer = time;
        finished = false;
    }

    public void resetTimer(){
         timer = saveTime;
         finished = false;
    }

    public Boolean isFinished(){
        return finished;
    }

    public void update(Float delta){
        if (finished) return;
        if (timer <= 0) finished = true;
        timer -= delta;
    }

    @Override
    public String toString() {
        return (int)(timer / 60) + ":" + timer.intValue() % 60;
    }
}

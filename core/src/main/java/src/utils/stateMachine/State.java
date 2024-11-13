package src.utils.stateMachine;

public interface State {

    void start();
    void update(Float delta);
    void end();
}

package src.utils.stateMachine;

public class StateMachine {
    private State currentState;

    public StateMachine() {
    }

    public void setState(State newState) {
        if (newState == null) return;
        if (currentState != null) currentState.end();
        currentState = newState;
        currentState.start();
    }

    public State getState(){
        return currentState;
    }

    public void start() {
        currentState.start();
    }

    public void update(Float delta) {
        currentState.update(delta);
    }

    public void end() {
        currentState.end();
    }
}

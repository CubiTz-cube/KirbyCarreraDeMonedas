package src.utils.stateMachine;

public class StateMachine {
    private State currentState;

    public StateMachine() {
    }

    public void start() {
        currentState.start();
    }

    public void update() {
        currentState.update();
    }

    public void end() {
        currentState.end();
    }

    public void changeState(State newState) {
        if (currentState != null) currentState.end();
        currentState = newState;
        currentState.start();
    }
}

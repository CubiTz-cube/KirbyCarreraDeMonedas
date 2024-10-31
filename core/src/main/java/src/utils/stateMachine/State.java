package src.utils.stateMachine;

public abstract class State {
    protected StateMachine stateMachine;

    public State (StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    public abstract void start();

    public abstract void update(Float delta);

    public abstract void end();
}

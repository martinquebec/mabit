package mabit.dispatcher;

/**
 * Created by martin on 6/9/2016.
 */
public interface ILifeCycle {
    enum State {
        START,
        STOP,
        TERMINATED;

        public boolean isTerminal() { return this == TERMINATED; }
    }
    void start();
    void stop();
    void terminate();

}

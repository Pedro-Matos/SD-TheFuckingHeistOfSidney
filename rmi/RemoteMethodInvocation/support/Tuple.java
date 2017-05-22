package RemoteMethodInvocation.support;

import java.io.Serializable;

/**
 * Created by tiagoalexbastos on 20-05-2017.
 */
public class Tuple<X, Y> implements Serializable {



    private final X clock;
    private final Y second;

    public Tuple(X clock, Y second) {
        this.clock = clock;
        this.second = second;
    }

    public X getClock() {
        return clock;
    }

    public Y getSecond() {
        return second;
    }
}

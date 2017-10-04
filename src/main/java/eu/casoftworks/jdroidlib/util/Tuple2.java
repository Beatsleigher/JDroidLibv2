package eu.casoftworks.jdroidlib.util;

import eu.casoftworks.jdroidlib.interfaces.*;

/**
 * An object designed for storing two items; allows for returning of multiple values
 * from a single method.
 *
 * This class cannot be inherited!
 *
 * @param <T1> The first stored type.
 * @param <T2> the second stored type.
 */
public final class Tuple2<T1, T2> implements ITuple2 {

    private final T1 item1;
    private final T2 item2;

    /**
     * Default (and only) constructor.
     * @param item1
     * @param item2
     */
    public Tuple2(T1 item1, T2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    /**
     * Gets the first item stored in the tuple.
     * @return The first item.
     */
    public T1 getItem1() { return item1; }

    /**
     * Gets the second item stored in the tuple.
     * @return The second item.
     */
    public T2 getItem2() { return item2; }

}

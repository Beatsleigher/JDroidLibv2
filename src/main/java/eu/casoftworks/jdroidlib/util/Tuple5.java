package eu.casoftworks.jdroidlib.util;

import eu.casoftworks.jdroidlib.interfaces.*;

public class Tuple5<T1, T2, T3, T4, T5> implements ITuple5<T1, T2, T3, T4, T5> {

    private final T1 item1;
    private final T2 item2;
    private final T3 item3;
    private final T4 item4;
    private final T5 item5;

    public Tuple5(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
    }

    @Override
    public T1 getItem1() {
        return item1;
    }

    @Override
    public T2 getItem2() {
        return item2;
    }

    @Override
    public T3 getItem3() {
        return item3;
    }

    @Override
    public T4 getItem4() {
        return item4;
    }

    @Override
    public T5 getItem5() {
        return item5;
    }
}

package com.venusiot.vehicle.message;

/**
 * @author damonkohler@google.com (Damon Kohler)
 *
 * @param <T>
 *          the return type
 * @param <S>
 *          the message type
 */
public interface MessageCallable<T, S> {

    T call(S message);
}

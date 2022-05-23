package edu.diyan.reactive.spring;

import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * all doOn* callbacks can be use to add side effects like monitoring or calling some logic with flux elements as parameters
 */
public class DoOnCallbacksTest {

    @Test
    public void doOnTest() {
        var signals = new ArrayList<Signal<Integer>>();
        var nextValues = new ArrayList<Integer>();
        var subscriptions = new ArrayList<Subscription>();
        var exceptions = new ArrayList<Throwable>();
        var finalSignals = new ArrayList<SignalType>();

        var flux = Flux.<Integer>create(sink -> {
                    sink.next(1);
                    sink.next(2);
                    sink.next(3);
                    sink.error(new IllegalArgumentException("test"));
                    sink.complete();
                })
                .doOnNext(nextValues::add)
                .doOnEach(signals::add)
                .doOnSubscribe(subscriptions::add)
                .doOnError(exceptions::add)
                .doFinally(finalSignals::add);

        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .expectError(IllegalArgumentException.class)
                .verify();

        signals.forEach(System.out::println);
        assertEquals(4, signals.size());

        nextValues.forEach(System.out::println);
        assertEquals(3, nextValues.size());

        subscriptions.forEach(System.out::println);
        assertEquals(1, subscriptions.size());

        exceptions.forEach(System.out::println);
        assertEquals(1, exceptions.size());

        finalSignals.forEach(System.out::println);
        assertEquals(1, finalSignals.size());

    }

}

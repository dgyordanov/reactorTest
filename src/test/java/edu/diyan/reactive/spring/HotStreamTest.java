package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class HotStreamTest {

    @Test
    public void test1() {
        var l1 = new ArrayList<Integer>();
        var l2 = new ArrayList<Integer>();

        // hot stream
        EmitterProcessor<Integer> emitterProcessor = EmitterProcessor.create(2);
        var sink = emitterProcessor.sink();

        emitterProcessor.subscribe(l1::add);

        sink.next(1);
        sink.next(2);

        emitterProcessor.subscribe(l2::add);

        sink.next(3);
        sink.complete();

        System.out.println(l1);
        System.out.println(l2);
    }

    @Test
    public void test2() throws InterruptedException {
        var factor = 10;
        var cdl = new CountDownLatch(2);

        var flux = Flux.range(0, 10)
                .delayElements(Duration.ofMillis(factor))
                .share();

        var l1 = new ArrayList<Integer>();
        var l2 = new ArrayList<Integer>();

        flux.doFinally(signalTypeConsumer(cdl))
                .subscribe(l1::add);

        Thread.sleep(factor * 2);

        flux.doFinally(signalTypeConsumer(cdl))
                .subscribe(l2::add);

        cdl.await(5, TimeUnit.SECONDS);
        System.out.println("stop()...");

        System.out.println(l1);
        System.out.println(l2);
    }

    private Consumer<SignalType> signalTypeConsumer(CountDownLatch cdl) {
        return signalType -> {
            if (signalType.equals(SignalType.ON_COMPLETE)) {
                System.out.println("await()....");
                cdl.countDown();
            }
        };
    }

    @Test
    public void test3() {
        var l1 = new ArrayList<Integer>();
        var l2 = new ArrayList<Integer>();
        var l3 = new ArrayList<Integer>();

        var pileOnFlux = Flux.just(1, 2, 3)
                // Share flux to subscribers
                .publish()
                // Wait for 3 subscribers
                .autoConnect(3)
                // force the subscription on the same thread so we can observe the interaction.
                .subscribeOn(Schedulers.immediate());

        pileOnFlux.subscribe(l1::add);
        assertEquals(0, l1.size());

        pileOnFlux.subscribe(l2::add);
        assertEquals(0, l2.size());

        pileOnFlux.subscribe(l3::add);
        assertEquals(3, l1.size());
        assertEquals(3, l2.size());
        assertEquals(3, l3.size());
    }
}

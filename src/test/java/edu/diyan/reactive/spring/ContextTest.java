package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class ContextTest {

    @Test
    public void testContext() throws InterruptedException {
        var observedContextValues = new ConcurrentHashMap<String, AtomicInteger>();
        var max = 3;
        var key = "key1";
        var cdl = new CountDownLatch(max);

        String value = "value1";
        Context context = Context.of(key, value);

        var flux = Flux.range(0, max)
                .delayElements(Duration.ofMillis(1))
                .doOnEach((Signal<Integer> signal) -> {
                    if (signal.getType().equals(SignalType.ON_NEXT)) {
                        String key1 = signal.getContext().get(key);
                        assertEquals(key1, value);
                        observedContextValues
                                .computeIfAbsent(key, k -> new AtomicInteger(0))
                                .incrementAndGet();
                    }
                })
                // Use this for the pipeline in a similar way as ThreadLocal in imperative programming
                .subscriberContext(context);

        flux.subscribe(i -> {
            System.out.println("Int: " + i);
            cdl.countDown();
        });

        cdl.await();

        assertEquals(observedContextValues.get(key).get(), max);
    }

}

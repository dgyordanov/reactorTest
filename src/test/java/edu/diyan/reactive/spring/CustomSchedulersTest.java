package edu.diyan.reactive.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Month;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * Schedulers provide many predefined Scheduler instances like immediate(), parallel(), single(), elastic() etc.
 * In case none of them fits, we can use a custom Scheduler with a custom executor.
 */
public class CustomSchedulersTest {

    private static final String DECORATOR_KEY = "custom-decorator";
    private final AtomicInteger invokationCounter = new AtomicInteger();

    @Before
    public void setUp() {
        Schedulers.resetFactory();
    }

    @After
    public void tearDown() {
        Schedulers.resetFactory();
        Schedulers.removeExecutorServiceDecorator(DECORATOR_KEY);
    }

    private ScheduledExecutorService decorate(ScheduledExecutorService scheduledExecutorService) {
        try {
            var pfb = new ProxyFactoryBean();
            pfb.setProxyInterfaces(new Class[]{ScheduledExecutorService.class});
            pfb.addAdvice((MethodInterceptor) methodInvocation -> {
                var methodName = methodInvocation.getMethod().getName();
                System.out.println("Invocation " + methodName);
                this.invokationCounter.incrementAndGet();
                return methodInvocation.proceed();
            });
            pfb.setSingleton(true);
            pfb.setTarget(scheduledExecutorService);
            return (ScheduledExecutorService) pfb.getObject();
        } catch (Exception e) {
            System.err.println("Error " + e);
        }
        return null;
    }

    @Test
    public void decoratorTest() {
        // Let us decorate the scheduler. We will add logging for each method invocation plus a counter.
        Schedulers.addExecutorServiceDecorator(
                DECORATOR_KEY,
                ((scheduler, scheduledExecutorService) -> this.decorate(scheduledExecutorService)));

        var flux = Flux.just(1)
                .delayElements(Duration.ofMillis(1));

        StepVerifier.create(flux)
                .thenAwait(Duration.ofMillis(10))
                .expectNextCount(1)
                .verifyComplete();

        assertEquals(1, invokationCounter.get());
    }

    @Test
    public void onScheduleHook() {
        var counter = new AtomicInteger();

        Schedulers.onScheduleHook(
                "test-hook",
                runnable -> {
                    System.out.println("Thread name " + Thread.currentThread().getName());
                    counter.incrementAndGet();
                    runnable.run();
                    return runnable;
                }
        );

        var flux = Flux.just(1, 2, 3)
                .delayElements(Duration.ofMillis(1))
                // Specify a Scheduler
                // subscribeOn() uses the Scheduler from the beginning of the chain until the next publishOn()
                // publishOn() uses the Scheduler for the rest of the operators in the chain until the next publishOn()
                .subscribeOn(Schedulers.immediate());

        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .verifyComplete();

        assertEquals(3, counter.get());
    }

    @Test
    public void testSubscribeOn() {
        var threadName = CustomSchedulersTest.class.getName();
        var map = new ConcurrentHashMap<String, AtomicInteger>();

        var customExecutor = Executors.newFixedThreadPool(5,
                runnable -> {
                    Runnable wrapper = () -> {
                        var key = Thread.currentThread().getName();
                        var counter = map.computeIfAbsent(key, s -> new AtomicInteger());
                        counter.incrementAndGet();
                        runnable.run();
                    };
                    return new Thread(wrapper, threadName);
                }
        );

        // Define custom executor
        Scheduler scheduler = Schedulers.fromExecutor(customExecutor);

        var mono = Mono.just(1)
                .subscribeOn(scheduler)
                .doFinally(signalType -> map.forEach((k, v) -> System.out.println(k + "=" + v))
                );

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();

        assertEquals(1, map.get(threadName).get());
    }

}

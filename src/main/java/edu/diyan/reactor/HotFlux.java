package edu.diyan.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotFlux {
    public static void main(String[] args) throws InterruptedException {
        Flux<Long> clockTicks = Flux.interval(Duration.ofSeconds(1));

        Flux<Long> hotClockTicks = clockTicks.share();

//        clockTicks.subscribe(System.out::println);
        hotClockTicks.subscribe(System.out::println);
        Thread.sleep(2500);
//        clockTicks.subscribe(System.out::println);
        hotClockTicks.subscribe(System.out::println);

        Thread.sleep(10000);
    }
}

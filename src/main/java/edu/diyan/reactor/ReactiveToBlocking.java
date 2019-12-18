package edu.diyan.reactor;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ReactiveToBlocking {

    // Note that you should avoid this by favoring having reactive code end-to-end, as much as possible.
    // You MUST avoid this at all cost in the middle of other reactive code,
    // as this has the potentialto lock your whole reactive pipeline.
    public static void main(String[] args) {
        String element = Mono.just("element").delayElement(Duration.ofSeconds(2)).block();
        System.out.println(element);

        List<Integer> values = FluxGenerator.slowFlux(5, 1)
                .collect(Collectors.toList())
                .block();
        System.out.println(values);
        // or
        Iterable<Integer> integers = FluxGenerator.slowFlux(5, 1).toIterable();
        System.out.println(integers);
    }
}

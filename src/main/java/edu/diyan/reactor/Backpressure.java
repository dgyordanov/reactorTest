package edu.diyan.reactor;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class Backpressure {

    // Useful in case of slow consumers. Take for example a computationally expensive mapping function,
    // an update in a database or saving a file. In this case, so much data might be streamed into the consumer
    // that it completely “chokes up” on all the data that flows in, and crashes with an OutOfMemoryException.
    static class BackpressureReadyUserSubscriber extends BaseSubscriber<User> {

        public void hookOnSubscribe(Subscription subscription) {
            request(1);
        }

        public void hookOnNext(User u) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            request(1);
            //cancel() may also be called
        }
    }

    public static void main(String[] args) {
        BackpressureReadyUserSubscriber subscriber = new BackpressureReadyUserSubscriber();

        new ReactiveUserRepo().findAll()
                .log()
                .subscribe(subscriber);
    }

}

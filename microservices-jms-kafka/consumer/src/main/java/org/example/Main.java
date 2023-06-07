package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        var consumer = new MyConsumer("localhost:9092");
        var dataConsumer = new StringValueConsumer(consumer, value -> log.info("value:{}", value));
        dataConsumer.startReceiving();
    }
}
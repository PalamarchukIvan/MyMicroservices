package org.example;

public class Main {
    public static void main(String[] args) {
        Producer producer = new Producer("localhost:9090");

        DataSender dataSender =
                new DataSender(producer, stringValue -> System.err.println("asked, value: " + stringValue));
        ValueSource valueSource = new StringValueSource(dataSender::dataHandler);
        valueSource.generate();
    }
}
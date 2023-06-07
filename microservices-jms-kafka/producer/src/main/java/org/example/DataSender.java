package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.function.Consumer;
@Slf4j
public class DataSender {

    public record StringValue(long id, String value) {}

    private final Producer myProducer;
    private final Consumer<StringValue> sendAsk;

    public DataSender(Producer myProducer, Consumer<StringValue> sendAsk) {
        this.sendAsk = sendAsk;
        this.myProducer = myProducer;
    }

    public void dataHandler(StringValue value) {
        log.info("value:{}", value);
        try {
            myProducer
                    .getMyProducer()
                    .send(
                            new ProducerRecord<>(Producer.TOPIC_NAME, value.id(), value),
                            (metadata, exception) -> {
                                if (exception != null) {
                                    log.info("message wasn't sent", exception);
                                } else {
                                    log.info(
                                            "message id:{} was sent, offset:{}" ,
                                            value.id(),
                                            metadata.offset());
                                    sendAsk.accept(value);
                                }
                            });
        } catch (Exception ex) {
            System.err.println("exception: " + ex.getMessage());
        }
    }

}

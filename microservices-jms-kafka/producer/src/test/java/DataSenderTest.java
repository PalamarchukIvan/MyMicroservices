import org.apache.kafka.clients.admin.NewTopic;
import org.example.DataSender;
import org.example.Producer;
import org.example.ValueSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.LongStream;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

class DataSenderTest {
    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic(Producer.TOPIC_NAME, 1, (short) 1)));
    }

    @Test
    void dataHandlerTest() {
        // given
        List<DataSender.StringValue> stringValues =
                LongStream.range(0, 9)
                        .boxed()
                        .map(idx -> new DataSender.StringValue(idx, "test:" + idx))
                        .toList();

        Producer producer = new Producer(KafkaBase.getBootstrapServers());

        List<DataSender.StringValue> factStringValues = new CopyOnWriteArrayList<>();
        var dataProducer = new DataSender(producer, factStringValues::add);
        var valueSource =
                new ValueSource() {
                    @Override
                    public void generate() {
                        for (var value : stringValues) {
                            dataProducer.dataHandler(value);
                        }
                    }
                };

        // when
        valueSource.generate();

        // then
        await().atMost(60, TimeUnit.SECONDS)
                .until(() -> factStringValues.size() == stringValues.size());
        assertThat(factStringValues).hasSameElementsAs(stringValues);
    }
}

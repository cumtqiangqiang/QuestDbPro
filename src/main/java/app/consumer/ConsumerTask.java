package app.consumer;

import app.model.Session;
import app.sink.QuestdbSink;

import java.util.Random;

public class ConsumerTask implements Runnable {
    private QuestdbSink sink = new QuestdbSink("/usr/local/var/questdb/db", 2, 5000, "test_position");
    private long start = 1654732800000L;
    Random random = new Random(100);

    @Override
    public void run() {
        while (true) {
            Session session = new Session(start + random.nextInt(10000),
                    "Book" + random.nextInt(10),
                    random.nextInt(10),
                    "order" + random.nextInt(10),
                    "tag" + random.nextInt(10),
                    100 + random.nextDouble(),
                    1000 + random.nextDouble(),
                    "Mode" + random.nextInt(10)
            );
            System.out.println(session);
            sink.insertWithBatchExec(session);
        }
    }
}

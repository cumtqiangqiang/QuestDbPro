package app.consumer;

import app.model.Session;
import app.sink.QuestdbSink;

import java.util.Random;

public class ConsumerTask implements Runnable {
    private QuestdbSink sink = new QuestdbSink("/usr/local/var/questdb/db", 1, 1000, "test_position");
    Random random = new Random(100);

    @Override
    public void run() {
        for (int i = 0; i < 100000000; i++) {


            Session session = new Session(System.currentTimeMillis(),
                    "Book" + random.nextInt(10),
                    random.nextInt(10),
                    "order" + random.nextInt(10),
                    "tag" + random.nextInt(10),
                    100 + random.nextDouble(),
                    1000 + random.nextDouble(),
                    "Mode" + random.nextInt(10));

            sink.insertWithBatchExec(session);
        }
        sink.close();
    }
}

package app;

import app.consumer.ConsumerTask;

public class App {
//    create table if not exists test_position
//            (
//                    pTime timestamp,
//                    pBookId symbol,
//                    count int,
//                    pOrder string,
//                    pTag symbol,
//                    margin double,
//                    rate double,
//                    mode symbol
//            ) timestamp(pTime) PARTITION BY DAY;
    public static void main(String[] args) {

        final ConsumerTask consumerTask = new ConsumerTask();
        consumerTask.run();

    }
}

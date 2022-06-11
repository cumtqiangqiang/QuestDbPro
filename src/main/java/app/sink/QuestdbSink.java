package app.sink;

import app.model.Session;
import io.questdb.cairo.CairoEngine;
import io.questdb.cairo.DefaultCairoConfiguration;
import io.questdb.cairo.TableWriter;
import io.questdb.griffin.SqlExecutionContextImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class QuestdbSink {
    private final CairoEngine engine;
    private final SqlExecutionContextImpl ctx;
    private final int batch;
    private final BlockingQueue<Session> queue;
    private final String tableName;
    private final ExecutorService executor = newSingleThreadExecutor(Executors.defaultThreadFactory());

    public QuestdbSink(final String configPath,
                       final int workerCount,
                       final int batch,
                       final String tableName) {
        this.engine = new CairoEngine(new DefaultCairoConfiguration(configPath));
        this.ctx = new SqlExecutionContextImpl(engine, workerCount);
        this.batch = batch;
        this.tableName = tableName;

        if (batch > 1 && batch < 2147483647) {
            this.queue = new LinkedBlockingQueue(batch);
        } else {
            this.queue = new LinkedBlockingQueue();
        }

    }

    public void insertWithBatchExec(Session session) {

        try {
            this.queue.put(session);
        } catch (InterruptedException var3) {
            throw new RuntimeException(var3);
        }

        if (this.queue.size() >= this.batch) {
            executor.submit(this::write);
        }


    }

    private void write() {
        if (this.queue.isEmpty()) {
            return;
        }
        List<Session> batches = new ArrayList<>();

        this.queue.drainTo(batches);

        final Iterator<Session> batchIt = batches.iterator();

        try (TableWriter writer = engine.getWriter(ctx.getCairoSecurityContext(),
                tableName,
                Thread.currentThread().getName())) {
            while (batchIt.hasNext()) {
                final Session po = batchIt.next();
                TableWriter.Row row = writer.newRow(po.getDbTimeMs());

                po.adaptToQuestDb(row);

            }
            writer.commit();
        }

    }

}

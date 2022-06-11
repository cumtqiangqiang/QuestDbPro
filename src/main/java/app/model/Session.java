package app.model;

import io.questdb.cairo.TableWriter;

public class Session {
    private long pTime;
    private String pBookId;
    private int count;
    private String pOrder;
    private String pTag;
    private double margin;
    private double rate;
    private String mode;

    public Session(final long pTime,
                   final String pBookId,
                   final int count,
                   final String pOrder,
                   final String pTag,
                   final double margin,
                   final double rate,
                   final String mode) {
        this.pTime = pTime;
        this.pBookId = pBookId;
        this.count = count;
        this.pOrder = pOrder;
        this.pTag = pTag;
        this.margin = margin;
        this.rate = rate;
        this.mode = mode;
    }

    public void adaptToQuestDb(final TableWriter.Row row) {
        int i = 0;
        row.putSym(++i, this.pBookId);
        row.putInt(++i, this.count);
        row.putStr(++i, this.pOrder);
        row.putSym(++i, this.pTag);
        row.putDouble(++i, this.margin);
        row.putDouble(++i, this.rate);
        row.putSym(++i, this.mode);
        row.append();
    }

    public long getDbTimeMs() {
        return this.pTime * 1000;
    }

    @Override
    public String toString() {
        return "Session{" +
                "pTime=" + pTime +
                ", pBookId='" + pBookId + '\'' +
                ", count=" + count +
                ", pOrder='" + pOrder + '\'' +
                ", pTag='" + pTag + '\'' +
                ", margin=" + margin +
                ", rate=" + rate +
                ", mode='" + mode + '\'' +
                '}';
    }
}

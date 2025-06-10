
package survey.common.snowflake;

import java.util.random.RandomGenerator;

public class Snowflake {
    private static final int UNUSED_BITS = 1;
    private static final int EPOCH_BITS = 41;
    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    private static final long maxNodeId = (1L << NODE_ID_BITS) - 1;
    private static final long maxSequence = (1L << SEQUENCE_BITS) - 1;

    private final long nodeId;
    // 2025-06-08T15:00:00.000Z
    private final long startTimeMillis = 1749394800000L;

    private long lastTimeMillis = startTimeMillis;
    private long sequence = 0L;

    public Snowflake() {
        this(RandomGenerator.getDefault().nextLong(maxNodeId + 1));
    }

    public Snowflake(long nodeId) {
        if (nodeId < 0 || nodeId > maxNodeId) {
            throw new IllegalArgumentException(
                    String.format("노드 ID는 0에서 %d 사이여야 합니다", maxNodeId));
        }
        this.nodeId = nodeId;
    }

    public synchronized long nextId() {
        long currentTimeMillis = timestamp();

        if (currentTimeMillis < lastTimeMillis) {
            throw new ClockMovedBackwardsException(
                    String.format("시계가 역행했습니다. %d 밀리초 대기 필요",
                            lastTimeMillis - currentTimeMillis));
        }

        if (currentTimeMillis == lastTimeMillis) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                currentTimeMillis = waitNextMillis(currentTimeMillis);
            }
        } else {
            sequence = 0;
        }

        lastTimeMillis = currentTimeMillis;

        return ((currentTimeMillis - startTimeMillis) << (NODE_ID_BITS + SEQUENCE_BITS))
                | (nodeId << SEQUENCE_BITS)
                | sequence;
    }


    protected long timestamp() {
        return System.currentTimeMillis();
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp <= lastTimeMillis) {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }

    public static class ClockMovedBackwardsException extends RuntimeException {
        public ClockMovedBackwardsException(String message) {
            super(message);
        }
    }
}
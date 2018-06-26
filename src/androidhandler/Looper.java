package androidhandler;

/**
 * Created by channagihong on 2018/6/26.
 */
class Looper {
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    IMessageQueue messageQueue;
    private static Looper sMainLooper;
    public Looper() {
        messageQueue = new MessageQueue(2);
        // messageQueue = new MessageQueue1(2);
        // messageQueue = new MessageQueue2(2);
    }
    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }
    public static void prepareMainLooper() {
        prepare();
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }
    public static Looper getMainLooper() {
        return sMainLooper;
    }
    public static Looper myLooper() {
        return sThreadLocal.get();
    }
    public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        for (;;) {
            // 消费Message，如果MessageQueen为null，则等待
            Message message = null;
            try {
                message = me.messageQueue.next();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (message != null) {
                message.target.handleMessage(message);
            }
        }
    }
}

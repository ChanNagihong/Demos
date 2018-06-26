package androidhandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by channagihong on 2018/6/26.
 */
class MessageQueue implements IMessageQueue {
    private final BlockingQueue<Message> queue;
    public MessageQueue(int cap) {
        this.queue = new LinkedBlockingQueue<>(cap);
    }
    public Message next() throws InterruptedException {
        return queue.take();
    }
    public void enqueueMessage(Message message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

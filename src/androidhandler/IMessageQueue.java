package androidhandler;

/**
 * Created by channagihong on 2018/6/26.
 */
interface IMessageQueue {
    Message next() throws InterruptedException;
    void enqueueMessage(Message message) throws InterruptedException;
}

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ABSequence {

    ReentrantLock lock;
    Condition condition1, condition2;
    volatile boolean shoudPrintA = true;
    volatile boolean started = false;
    volatile int count = 10000;

    static class AThread extends Thread {

        ABSequence main;

        public AThread(ABSequence main) {
            this.main = main;
        }

        @Override
        public void run() {
            while (true) {
                if (main.count < 0) break;
//                main.lock.lock();
                if (!main.shoudPrintA) {
//                    main.lock.unlock();
                    continue;
                }
                System.out.print('A');
                main.count--;
                main.shoudPrintA = false;
//                main.lock.unlock();
            }
        }
    }

    static class BThread extends Thread {

        ABSequence main;

        public BThread(ABSequence main) {
            this.main = main;
        }

        @Override
        public void run() {
            while (true) {
                if (main.count < 0) break;
//                main.lock.lock();
                if (main.shoudPrintA) {
//                    main.lock.unlock();
                    continue;
                }
                System.out.print('B');
                main.count--;
                main.shoudPrintA = true;
//                main.lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ABSequence sequence = new ABSequence();
        sequence.lock = new ReentrantLock();
        sequence.condition1 = sequence.lock.newCondition();
        sequence.condition2 = sequence.lock.newCondition();
//        AThread aThread = new AThread(sequence);
//        BThread bThread = new BThread(sequence);
//        aThread.start();
//        bThread.start();
        CThread cThread = new CThread(sequence);
        DThread dThread = new DThread(sequence);
        cThread.start();
        dThread.start();
    }

    static class CThread extends Thread {

        ABSequence main;

        public CThread(ABSequence main) {
            this.main = main;
        }

        @Override
        public void run() {
            main.lock.lock();
            try {
                while (true) {
                    if (main.count < 0) break;
                    if (!main.shoudPrintA) {
                        main.condition1.await();
                    }
                    System.out.print('C');
                    main.count--;
                    main.shoudPrintA = false;
                    main.condition2.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                main.lock.unlock();
            }
        }
    }

    static class DThread extends Thread {

        ABSequence main;

        public DThread(ABSequence main) {
            this.main = main;
        }

        @Override
        public void run() {
            main.lock.lock();
            try {
                while(true) {
                    if(main.count < 0) break;
                    if(main.shoudPrintA) {
                        main.condition2.await();
                    }
                    System.out.print('D');
                    main.count--;
                    main.shoudPrintA = true;
                    main.condition1.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                main.lock.unlock();
            }
        }
    }

}

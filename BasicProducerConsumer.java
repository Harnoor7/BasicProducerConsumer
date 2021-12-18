import java.util.LinkedList;
import java.util.Random;

public class BasicProducerConsumer {

    private static LinkedList<Integer> queueOfResources;
    private static Object lock = new Object();
    private static final Integer capacity = 10;

    public static void produce() throws InterruptedException {

        Integer resource = 1;

        while (true) {
            synchronized (lock) {
                while (queueOfResources.size() == capacity) {
                    lock.wait();
                }
                System.out.println("Going to add " + resource + " to the queue.");
                queueOfResources.addLast(resource);
                System.out.println(resource + " successfully added to the queue.");
                lock.notify();
            }
            resource += 1;
        }

    }

    public static void consume() throws InterruptedException {

        while (true) {
            Thread.sleep(new Random().nextInt(1000));
            synchronized (lock) {
                System.out.println("Going to consume resource from the queue.");
                while (queueOfResources.isEmpty()) {
                    lock.wait();
                }
                Integer consumed = queueOfResources.removeFirst();
                System.out.println(consumed + " successfully consumed from the queue and current size of queue: " + queueOfResources.size());
                lock.notify();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {

        queueOfResources = new LinkedList<>();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

}
public class QueueExample {
    public static void main(String[] args) {
        MyQueue queue = new MyQueue(4);
        // thread
        OtherThread thread = new OtherThread(queue);
        thread.start();

        while(true)
        {
            queue.enqueue(1);
            queue.enqueue(2);
            queue.enqueue(3);
            try {
                Thread.sleep(500);
            } catch(Exception e) {};
        }
    }
}

class OtherThread extends Thread {
    MyQueue queue;

    public OtherThread(MyQueue queue) {
        this.queue = queue;
    }

    public void run() {
        while(true) {
            queue.dequeue();
            try {
                Thread.sleep(1000);
            } catch(Exception e) {}
        }
    }
}



class MyQueue {
    int capacity;
    int enIndex = 0;
    int deIndex = 0;
    int size = 0;
    int[] items;


    public MyQueue(int capacity) {
        this.capacity = capacity;
        items = new int[capacity];
    }

    public void enqueue(int item) {
        synchronized(this) {
            while (size == capacity) {
                try {
                    System.out.printf("Thread %s is waiting to enqueue %d, queue is full.\n",
                        Thread.currentThread().getName(), item);
                    this.wait();
                }
                catch(Exception e) {}
            }
        
        
            
            this.items[enIndex] = item;
            enIndex = (enIndex + 1) % capacity;
            size++;
            this.notify();

            System.out.printf("Thread %s has enqueued %d, new size is %d\n",
                Thread.currentThread().getName(), item, size);
        }
    }

    public int dequeue() {
        synchronized(this) {
            while (size == 0) {
                try {
                    System.out.printf("Thread %s is waiting for items to dequeue, queue is empty.\n",
                        Thread.currentThread().getName());
                    this.wait();
                }
                catch(Exception e) {}
            }

            int item = this.items[deIndex];
            deIndex = (deIndex + 1) % capacity;
            size--;

            System.out.printf("Thread %s has dequeued %d, new size is %d\n",
                Thread.currentThread().getName(), item, size);

            this.notify();
            return item;
        }
    }

    public int peek() {
        synchronized(this) {
            if (size == 0) {
                throw new RuntimeException("Queue is empty!");
            }

            return this.items[deIndex];
        }
    }
}

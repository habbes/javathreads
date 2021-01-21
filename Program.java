public class Program {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello, World");

        MyThread thread1 = new MyThread("MyThread1", 15);
        thread1.start();
        // if you uncomment the following line,
        // the the main thread will wait for thread1 to finish
        // running and die before executing the next statement
        // thread1.join();
        
        MyThread thread2 = new MyThread("MyThread2", 5);
        
        countTo("From main thread", 10);
        somethingElse();
        thread2.start();
        countTo("From main thread 2", 20);
        
    }

    public static void countTo(String message, int x) {
        for(int i = 1; i <= x; i++) {
            System.out.println(message + ": " + i);
        }
    }

    public static void somethingElse() {
        System.out.println("It's a wonderful world!");
    }
}

class MyThread extends Thread {
    int countTarget;
    String message;

    public MyThread(String message, int target) {
        this.countTarget = target;
        this.message = message;
    }

    public void run() {
        try {
            Program.countTo(message, this.countTarget);
            Thread.sleep(5000); // sleep for 5 seconds
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
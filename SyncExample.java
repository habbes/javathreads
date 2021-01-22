import java.util.*;

public class SyncExample {
    public static void main(String[] args) {
        BankAccount acc1 = new BankAccount("John", 1000);
    

        
        BankAccount acc2 = new BankAccount("Sarah", 0);
        acc1.setName("James");
        System.out.println(acc2);
    
        System.out.println(acc1);

        

        WithdrawThread withdrawer1 = new WithdrawThread(acc1,
            new int[] { 200, 500, 500, 500 }); // 1700
        
        WithdrawThread withdrawer2 = new WithdrawThread(acc1,
            new int[] { 400, 600, 1000, 200 }); // 2200
        
        withdrawer1.start();
        withdrawer2.start();

        try {
            withdrawer1.join();
            withdrawer2.join();
        }
        catch (InterruptedException ex) {}

        System.out.println("final balance " + acc1.getBalance());

    }
}



class BankAccount {
    private String name;
    private int balance;

    public BankAccount(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        
    }

    public void withdraw(int amount) {
        Random r = new Random();
        // assume bal = 500
        // thread1 wants to withdraw 300
        synchronized(this) {
            if (amount <= balance) {
                try {
                    Thread.sleep(r.nextInt(3) * 1000);
                }
                catch (Exception e) {}
                //
                balance -= amount;
            }
        }
    }

    public void deposit(int amount) {
        synchronized(this) {
            balance += amount;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public String toString() {
        return "Account(name: " + name + ",balance: " + balance + ")";
    }
}


class WithdrawThread extends Thread {
    BankAccount targetAccount;
    int[] amounts;

    public WithdrawThread(BankAccount account, int[] amounts) {
        this.targetAccount = account;
        this.amounts = amounts;
    }

    public void run() {
        for (int amount : amounts) {
            targetAccount.withdraw(amount);
        }
    }
}

class DepositThread extends Thread {
    BankAccount targetAccount;
    int[] amounts;

    public DepositThread(BankAccount account, int[] amounts) {
        this.targetAccount = account;
        this.amounts = amounts;
    }

    public void run() {
        for (int amount : amounts) {
            targetAccount.deposit(amount);
        }
    }
}
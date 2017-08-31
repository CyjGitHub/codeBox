package com.codeBox.thread.join;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: Thread.join()  <br/>
 * Function: <br/>
 * date: 2017/7/4 9:28 <br/>
 *
 * @author cyj
 * @since JDK 1.7
 */
public class JoinThread {

    public static void main(String[] args) {
        Thread now= Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread=new Thread(new Domino(now), String.valueOf(i));
            thread.start();
            now = thread;
        }
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName()+" finish...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Domino implements Runnable {
        private Thread thread;

        public Domino(Thread thread){
            this.thread=thread;
        }

        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" finish...");
        }

    }
}

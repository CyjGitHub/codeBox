package com.codeBox.thread.waitAndNotify;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: 线程的等待/通知机制 <br/>
 * Function: <br/>
 * date: 2017/7/4 11:54 <br/>
 *
 * @author cyj
 * @since JDK 1.7
 */
public class WaitNotify {

    static boolean flag = true;
    static Object lock = new Object();

    static class Wait implements Runnable {

        public void run() {
            synchronized (lock){
                while (flag){
                    System.out.println("wait-"+ Thread.currentThread()+ " "+flag+" "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    try {
                        /**
                         * 1，调用该方法后，会释放对象的锁；
                         * 2，在接收到notify通知后，必须重新获取对象的锁，才能正常返回，执行下面代码；
                         */
                        lock.wait(1000);

                        System.out.println(" wait next");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("wait-"+ Thread.currentThread()+ " "+flag+" "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    static class Notify implements Runnable {

        public void run() {
            synchronized (lock){
                System.out.println("notify-"+ Thread.currentThread()+ " "+flag+" "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                /**
                 * 1，调用notify()方法，必须先对对调用的对象加锁；
                 * 2，通知后并不会释放对象的锁；
                 * 3，直到当前线程释放了对象的锁（lock），等待线程才会从wait方法中返回；
                 */
                lock.notifyAll();

                flag=false;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (lock){
                System.out.println("notify-上锁"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Thread wait=new Thread(new Wait(),"wait-Thread");
        wait.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notify = new Thread(new Notify(), "notify-Thread");
        notify.start();

        //CountDownLatch

    }

}

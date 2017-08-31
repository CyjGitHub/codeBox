package com.codeBox.thread.threadLocal;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: 线程变量 <br/>
 * Function: <br/>
 * date: 2017/7/4 9:48 <br/>
 *
 * @author cyj
 * @since JDK 1.7
 */
public class LocalThread {
    /**
     * 使用ThreadLocal 维护变量，相当于 线程的局部变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL=new ThreadLocal<Long>(){
        /**
         * protected Object initialValue()返回该线程局部变量的初始值
         * 该方法是一个protected的方法，显然是为了让子类覆盖而设计的。
         * 这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。ThreadLocal中的缺省实现直接返回一个null
         */
        protected Long initialValue(){
          return System.currentTimeMillis();
        }
    };

    public static final void begin(){
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end(){
        return System.currentTimeMillis()-TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws Exception {
        LocalThread.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: "+LocalThread.end()+" mills...");
    }
}

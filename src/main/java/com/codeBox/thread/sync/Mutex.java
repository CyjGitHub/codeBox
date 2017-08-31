package com.codeBox.thread.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ClassName:  <br/>
 * Function: <br/>
 * date: 2017/8/29 15:28 <br/>
 *
 * @author cyj
 * @since JDK 1.7
 */
public class Mutex implements Lock {
    //静态内部类 自定义同步器
    private static class Syne extends AbstractQueuedSynchronizer {
        //是否处于占用状态
        protected boolean isHeldExculusively() {
            return getState() == 1;
        }

        //当前状态为0的时候获取锁
        public boolean tryAcquire() {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        //释放锁 将状态设置为0
        protected boolean tryRelease(int releases) {
            if(getState()==0) throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
        //返回一个Condition 每个condition都包含一个condition队列
        Condition newCondition(){ return new ConditionObject();}
    }

    //仅需将操作代理到Sync上即可
    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return true;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    public void unlock() {

    }

    public Condition newCondition() {
        return null;
    }
}

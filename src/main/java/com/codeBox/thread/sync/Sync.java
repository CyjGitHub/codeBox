package com.codeBox.thread.sync;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * ClassName:  <br/>
 * Function: <br/>
 * date: 2017/8/29 15:05 <br/>
 *
 * @author cyj
 * @since JDK 1.7
 */
public class Sync extends AbstractQueuedSynchronizer {

    public void test() {

        this.acquire(1);
    }
}

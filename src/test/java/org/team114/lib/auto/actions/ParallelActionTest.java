package org.team114.lib.auto.actions;

import org.junit.*;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.lang.Runnable;

import static org.mockito.Mockito.times;

/**
 * Tests for {@link ParallelAction}.
 */
public class ParallelActionTest {

    //at class level to avoid triggering timeout

    private static class TestRunnable implements Runnable {
        private CountDownLatch latch = new CountDownLatch(3);

        @Override
        public void run() {
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                //TODO error logging
                System.out.print("Test interrupted: " + e.getMessage());
            }
        }
    }

    private Runnable runnable = new TestRunnable();

    private Runnable spy = Mockito.spy(runnable);
    private Runnable parallel = new ParallelAction(spy, spy, spy);

    /**
     * Uses a CountDownLatch to test that all actions are run concurrently. If the actions do not
     * run at the same time, the latch will never unlock and the test will halt indefinitely,
     * triggering a timeout.
     */
    @Test(timeout = 500)
    public void testConcurrentExecution() {

        parallel.run();

        Mockito.verify(spy, times(3)).run();
        Mockito.verifyNoMoreInteractions(spy);
    }
}


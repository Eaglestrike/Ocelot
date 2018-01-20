package org.team114.lib.auto.actions;

import org.junit.*;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.times;

/**
 * Tests for {@link ParallelAction}.
 */
public class ParallelActionTest {

    //at class level to avoid triggering timeout
    private CountDownLatch latch = new CountDownLatch(3);
    private Runnable runnable = ()  -> {
        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            //TODO error logging
            System.out.print("Test interrupted: " + e.getMessage());
        }
    };

    private Action spy = Mockito.spy(new RunnerAction(runnable));
    private Action parallel = new ParallelAction(spy, spy, spy);

    /**
     * Uses a CountDownLatch to test that all actions are run concurrently. If the actions do not
     * run at the same time, the latch will never unlock and the test will halt indefinitely,
     * triggering a timeout.
     */
    @Test(timeout = 100)
    public void testConcurrentExecution() {

        parallel.run();

        Mockito.verify(spy, times(3)).run();
        Mockito.verifyNoMoreInteractions(spy);
    }
}


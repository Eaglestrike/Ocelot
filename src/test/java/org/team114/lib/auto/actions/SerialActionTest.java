package org.team114.lib.auto.actions;

import org.junit.*;
import org.mockito.*;

/**
 * Tests for {@link SerialAction}.
 */
public class SerialActionTest {

    /**
     * Tests that actions provided to SerialAction are run in the correct order. Also
     * checks that they are not run more times than is necessary.
     */
    @Test
    public void testSerialRun() {
        Action spy1 = Mockito.mock(Action.class);
        Action spy2 = Mockito.mock(Action.class);
        Action spy3 = Mockito.mock(Action.class);
        InOrder inOrder = Mockito.inOrder(spy1, spy2, spy3);

        Action serial = new SerialAction(spy1, spy2, spy3);
        serial.run();

        inOrder.verify(spy1).run();
        inOrder.verify(spy2).run();
        inOrder.verify(spy3).run();

        Mockito.verifyNoMoreInteractions(spy1, spy2, spy3);
    }
}

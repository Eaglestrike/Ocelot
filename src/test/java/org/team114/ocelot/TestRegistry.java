package org.team114.ocelot;

import junit.framework.TestCase;
import org.junit.Test;
import org.team114.ocelot.settings.Settings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestRegistry extends TestCase {
    @Test
    public void test() throws IOException {
        RegistryImpl robotRegistry = new RegistryImpl(new Settings().load());
        int channelA = robotRegistry.getSubRegistry("Drive")
                .getSubRegistry("left")
                .getConfiguration().getInt("channelA");
        assertEquals(5, channelA);
    }

    public void testIndexed() {
        RegistryImpl robotRegistry = new RegistryImpl(null);
        robotRegistry.putIndex(1, "o1");
        robotRegistry.putIndex(10, "o10");
        // same key different object
        robotRegistry.putIndex(10, 10);
        Integer ten = robotRegistry.getIndex(10, Integer.class);
        assertEquals(10, ten.intValue());
        String o_ten = robotRegistry.getIndex(10, String.class);
        assertEquals("o10", o_ten);
    }

    public void testSingleton() {
        RegistryImpl robotRegistry = new RegistryImpl(null);
        robotRegistry.put("o10");
        String o_ten = robotRegistry.get(String.class);
        assertEquals("o10", o_ten);
    }

    @Test(expected = IllegalStateException.class)
    public void testChannels() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test.slave=1\nTest.master=1\nTest.other=2".getBytes());
        RegistryImpl robotRegistry = new RegistryImpl(new Settings().load(inputStream));
        Settings.Configuration configuration = robotRegistry.getConfiguration();
        int master = configuration.getChannelAndRegister("Test.master");
        assertEquals(1, master);
        // check to make sure that asking twice does not cause a problem
        master = configuration.getChannelAndRegister("Test.master");
        int other = configuration.getChannelAndRegister("Test.other");
        assertEquals(2, other);
        try {
            int slave = configuration.getChannelAndRegister("Test.slave");
            assertFalse("should fail because master and slave are the same", true);
        } catch (IllegalStateException e) {}
    }
}

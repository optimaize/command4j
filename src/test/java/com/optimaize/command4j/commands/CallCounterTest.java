package com.optimaize.command4j.commands;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CallCounterTest {

    /**
     * Makes sure the getName() provided in the BaseCommand works.
     */
    @Test
    public void getName() throws Exception {
        assertEquals(new CallCounter(new CallCounter.Counter()).getName(), "CallCounter");
    }
}
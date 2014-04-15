package com.optimaize.command4j.cache;

import org.testng.annotations.Test;

import java.util.concurrent.Callable;

import static org.testng.Assert.assertEquals;

/**
 * @author Fabian Kessler
 */
public class ExecutorCacheTest {

    @Test
    public void testGet() throws Exception {
        ExecutorCache executorCache = new ExecutorCache();

        final int[] counter = new int[]{0};
        Callable<String> creator = new Callable<String>() {
            @Override
            public String call() throws Exception {
                counter[0]++;
                return "foo";
            }
        };

        for (int i=0; i<10; i++){
            ExecutorCacheKey<String> executorCacheKey = ExecutorCacheKey.create(String.class);
            String value = executorCache.get(executorCacheKey, creator);
            assertEquals(value, "foo");
        }

        assertEquals(counter[0], 1);
    }
}

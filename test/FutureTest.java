package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Future;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void testGet()
    {
        assertFalse(future.isDone());
        future.resolve("");
        future.get();
        assertTrue(future.isDone());
    }

    @Test
    public void testResolve(){
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertEquals(future.get(), str);
    }

    @Test
    public void testIsDone(){
        String str = "someResult";
        assertFalse(future.isDone());
        future.resolve(str);
        assertTrue(future.isDone());
        assertEquals(future.get(), str);
    }

    @Test
    public void testGetWithTimeOut() throws InterruptedException
    {
        assertFalse(future.isDone());
        future.get(100,TimeUnit.MILLISECONDS);
        assertFalse(future.isDone());
        future.resolve("foo");
        assertEquals(future.get(100,TimeUnit.MILLISECONDS),"foo");
    }
}

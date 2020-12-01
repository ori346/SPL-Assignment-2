package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.application.passiveObjects.Ewok;

import static org.junit.jupiter.api.Assertions.*;


class EwokTest {
    private Ewok testEwok;

    @BeforeEach
    void testSetUp() {
        testEwok = new Ewok(0);
    }


    @Test
    void testAcquire() {
        //by default the Ewok available
        assertTrue(testEwok.isAvailable());
        testEwok.acquire();
        assertFalse(testEwok.isAvailable());
    }

    @Test
    void testRelease() {
        testEwok.acquire();
        assertFalse(testEwok.isAvailable());
        //check if the release function changing the status
        testEwok.release();
        assertTrue(testEwok.isAvailable());
    }
}
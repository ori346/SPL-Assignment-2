package test;

import src.application.messages.AttackEvent;
import src.application.services.C3POMicroservice;
import src.application.services.HanSoloMicroservice;
import src.MessageBus;
import src.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MessageBusImplTest {
    MessageBus messageBus;
    @BeforeEach
    void setUp() {
        messageBus =  MessageBusImpl.getInstance();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSubscribeEvent() {
        try {
            messageBus.subscribeEvent(AttackEvent.class,null);
            fail("can't insert null micro service");
        } catch (NullPointerException ignored) {
        }
        try{
            messageBus.subscribeEvent(null,new HanSoloMicroservice());
            fail("can't insert null event");
        }catch (NullPointerException ignored){
        }
    }
    @Test
    void testSubscribeBroadcast() {
        try{
            Broadcast broadcast = new Broadcast() {};
            messageBus.subscribeBroadcast(broadcast.getClass(),null);
            fail("can't insert null microservice");
        }catch (NullPointerException ignored){}
    }

    @Test
    void testComplete() {
        //need to test
        Event e1 = new AttackEvent();
        MicroService m1 = new C3POMicroservice();
        MicroService m2 = new HanSoloMicroservice();
        messageBus.subscribeEvent(AttackEvent.class, m1);
        messageBus.sendEvent(e1);
        try {
            Message m = messageBus.awaitMessage(m2);
            messageBus.complete(e1,5);
            //assertEquals((int) future.get(), 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSendBroadcast() {
    }

    @Test
    void testSendEvent() {
        Event e1 = new AttackEvent();
        MicroService m1 = new C3POMicroservice();
        MicroService m2 = new HanSoloMicroservice();
        //m2.subscribeEvent(AttackEvent.class, c -> {});
        //m1.sendEvent(e1);
        try {
            Message message = messageBus.awaitMessage(m2);
            assertEquals(message, e1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    void testRegister() {
        //don't need to test
    }

    @Test
    void testUnregister() {
        //don't need to test
    }

    @Test
    void testAwaitMessage() {
        //need to test
        Event e1 = new AttackEvent();
        MicroService m1 = new HanSoloMicroservice();
        MicroService m2 = new MicroService("") {
            @Override
            protected void initialize() {
            }
        };

    }
}
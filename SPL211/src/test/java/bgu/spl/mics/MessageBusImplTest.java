package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
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
        m2.subscribeEvent(AttackEvent.class, c -> {});
        Future future = m1.sendEvent(e1);
        try {
            Message m = messageBus.awaitMessage(m2);
            m2.complete((Event<? super Integer>) m,5);
            assertEquals((int) future.get(), 5);
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
        m2.subscribeEvent(AttackEvent.class, c -> {});
        m1.sendEvent(e1);
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
        m1.subscribeEvent(AttackEvent.class, new Callback<AttackEvent>() {
            @Override
            public void call(AttackEvent c) {}});
        m2.sendEvent(e1);
        try {
            Message m = messageBus.awaitMessage(m1);
            assertEquals(e1, m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
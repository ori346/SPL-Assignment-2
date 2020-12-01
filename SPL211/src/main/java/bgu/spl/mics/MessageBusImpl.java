package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private static MessageBusImpl messageBusInstance = null;
	private static Map<Class <? extends  Event>, Integer> RoundRobin;
	private Map<MicroService, Queue<Message>> messegesQueuesMap;
	private Map <Class <? extends Message> ,List<MicroService>> eventsMap;
	private Map <Class <? extends Message> , List<MicroService>> broadcastMap;

	private MessageBusImpl(){
		RoundRobin = new ConcurrentHashMap<>();
		messegesQueuesMap = new ConcurrentHashMap<>();
		eventsMap = new ConcurrentHashMap<>();
		broadcastMap = new ConcurrentHashMap <>();
	}

	public static MessageBus getInstance(){
		if (messageBusInstance == null)
			messageBusInstance = new MessageBusImpl();
		return messageBusInstance;
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (m == null || type == null)
			throw new NullPointerException("can't subscribe null");
		if (!isRegister(m))
			throw new IllegalArgumentException("The MicroService is not register the messages bus");

		if (eventsMap.containsValue(type))
			eventsMap.get(type).add(m);
		else {
			List<MicroService> listSet = Collections.synchronizedList(new ArrayList<>());
			listSet.add(m);
			RoundRobin.put(type,0);
			eventsMap.put(type, listSet);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (m == null || type == null)
			throw new NullPointerException("can't subscribe null");
		if (!isRegister(m))
			throw new IllegalArgumentException("The MicroService is not register the messages bus");

		if (broadcastMap.containsValue(type))
			broadcastMap.get(type).add(m);
		else {
			List<MicroService> listSet = Collections.synchronizedList(new ArrayList<>());
			listSet.add(m);
			broadcastMap.put(type, listSet);
		}
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		if (result == null || e == null)
			throw new NullPointerException("result can't be null");
		Future<T> future = new Future<>();
		future.resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (b == null)
			throw new IllegalArgumentException("Broadcast can't be null");
		for (MicroService m : broadcastMap.get(b.getClass())) {
			messegesQueuesMap.get(m).add(b);
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future <T> output = new Future<>();
		List <MicroService> list = eventsMap.get(e.getClass());
		int round  = RoundRobin.get(e.getClass());
		Queue<Message> messageQueue = messegesQueuesMap.get(list.get(round % list.size()));
		messageQueue.add(e);
		round = round + 1;
		RoundRobin.replace(e.getClass(),round);
		return null;
	}

	@Override
	public void register(MicroService m) {
		if (m == null)
			throw new NullPointerException("can't register null");
		if (messegesQueuesMap.containsKey(m))
			throw new IllegalArgumentException("cannot insert the same MicroService twice");
		Queue <Message> MicroserviceQueue = new LinkedList<>();
		messegesQueuesMap.put(m,MicroserviceQueue);
	}

	@Override
	public void unregister(MicroService m) {
		messegesQueuesMap.remove(m);
		unsubscribe(broadcastMap,m);
		unsubscribe(eventsMap,m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (!isRegister(m))
			throw new IllegalStateException("MicroService never register to the message bus");
		Message output = messegesQueuesMap.get(m).poll();
		return output;
	}

	private static void unsubscribe (Map <Class <? extends Message> ,List <MicroService>>  map , MicroService m ) {
		for (Map.Entry<Class <? extends Message> ,List <MicroService>> entry : map.entrySet()){
			entry.getValue().remove(m);
		}
	}
	private boolean isRegister(MicroService m){
		return messegesQueuesMap.containsKey(m);
	}
}

package com.example.demoserver.event.dispatch;

import com.example.demoserver.event.common.EventHandler;
import com.example.demoserver.event.common.GameEvent;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class EventManager {


    private static Map<Class<? extends GameEvent>, List<EventHandler>> listenerMap = new ConcurrentHashMap<>();

    public static <E extends GameEvent> void subscribe(Class<? extends GameEvent> eventClass, EventHandler<E> eventHandler) {

        List<EventHandler> eventHandlerList = listenerMap.get(eventClass);
        if (null == eventHandlerList) {
            eventHandlerList = new CopyOnWriteArrayList<>();
        }

        eventHandlerList.add(eventHandler);

        listenerMap.put(eventClass,eventHandlerList);
    }




    private static ThreadFactory sceneThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("event-loop-%d").build();
    private static ExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(sceneThreadFactory);




    public static <E extends GameEvent> void publish(E event) {
        log.debug("事件{}被抛出 ，listenerMap {}",event.getClass(),listenerMap);
        List<EventHandler> handlerList =  listenerMap.get(event.getClass());
        if (!Objects.isNull(handlerList)) {
            for (EventHandler eventHandler: handlerList) {
                singleThreadSchedule.execute( () -> eventHandler.handle(event));
            }
        }
    }

    public static void close() throws Exception {
        //释放资源
        listenerMap.clear();
    }
}

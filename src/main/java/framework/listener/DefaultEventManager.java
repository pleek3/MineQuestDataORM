package framework.listener;

import framework.listener.model.EventListener;
import framework.listener.model.EventDetail;
import framework.listener.model.StandardEvent;
import framework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class DefaultEventManager implements EventManager {

    private final static Class<? extends Annotation> ANNOTATION_CLASS = EventListener.class;

    private final Map<String, EventDetail> eventDetails = new HashMap<>();
    private boolean standardEventsRegistered = false;

    public DefaultEventManager() {
        registerStandardEvents();
    }

    private void registerStandardEvents() {
        if (this.standardEventsRegistered) {
            System.out.println("Standard events are already registered.");
            return;
        }

        Arrays.stream(StandardEvent.values()).map(StandardEvent::getEventName).forEach(this::registerEvent);

        this.standardEventsRegistered = true;
    }

    private EventDetail getEventDetail(String eventName) {
        return this.eventDetails.getOrDefault(eventName, null);
    }

    @Override
    public void registerListener(Class<?> clazz) {
        List<Method> annotatedMethods = ClassUtils.getMethodsAnnotatedWith(clazz, ANNOTATION_CLASS);
        Map<String, List<Method>> eventToMethodsMap = new HashMap<>();

        // Group methods by event name
        for (Method method : annotatedMethods) {
            EventListener listener = method.getAnnotation(EventListener.class);
            String eventName = listener.value();
            eventToMethodsMap.computeIfAbsent(eventName, k -> new ArrayList<>()).add(method);
        }

        for (Map.Entry<String, List<Method>> entry : eventToMethodsMap.entrySet()) {
            String eventName = entry.getKey();
            EventDetail detail = getEventDetail(eventName);

            if (detail == null) {
                throw new RuntimeException("Event detail not found for event: " + eventName);
            }

            List<Method> methodsWithPriority = entry.getValue();
            methodsWithPriority.sort(Comparator.comparingInt(method -> method.getAnnotation(EventListener.class).priority()));

            detail.getEventListener().clear();
            detail.getEventListener().addAll(methodsWithPriority);
        }
    }

    @Override
    public void dispatch(String eventName) {
        EventDetail detail = getEventDetail(eventName);

        if (detail == null) {
            throw new RuntimeException("Event detail not found for event: " + eventName);
        }

        for (Method listenerMethod : detail.getEventListener()) {
            try {
                if (listenerMethod.getParameterCount() != 0) {
                    throw new RuntimeException("Method with parameters found in event listener: " + listenerMethod.getName());
                }

                listenerMethod.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dispatch(StandardEvent standardEvent) {
        this.dispatch(standardEvent.getEventName());
    }


    @Override
    public void registerEvent(String eventName) {
        EventDetail eventDetail = getEventDetail(eventName);

        if (eventDetail != null) {
            throw new RuntimeException("Event already registered: " + eventName);
        }

        eventDetail = new EventDetail(eventName);
        this.eventDetails.put(eventName, eventDetail);
    }

    @Override
    public void registerEvents(String... eventNames) {
        Arrays.stream(eventNames).forEach(this::registerEvent);
    }

}

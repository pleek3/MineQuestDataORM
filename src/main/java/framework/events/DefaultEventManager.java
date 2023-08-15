package framework.events;

import framework.annotations.Component;
import framework.annotations.Service;
import framework.events.model.EventListener;
import framework.events.model.EventDetail;
import framework.events.model.StandardEvent;
import framework.registry.ComponentRegistry;
import framework.util.TempClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Service
public class DefaultEventManager implements EventManager {

    private final static Class<? extends Annotation> ANNOTATION_CLASS = EventListener.class;

    private final Map<String, EventDetail> eventDetails = new HashMap<>();
    private boolean standardEventsRegistered = false;

    private ComponentRegistry componentRegistry;

    /*
    todo: Auto Register EventMethods
     */

    public DefaultEventManager(ComponentRegistry componentRegistry) {
        this.componentRegistry = componentRegistry;
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
        //todo: Hier draus eine Methode im ServiceRegistry machen
        if (!clazz.isAnnotationPresent(Service.class) && !clazz.isAnnotationPresent(Component.class)) {
            throw new RuntimeException("Invalid listener registration for class: " + clazz.getName() +
                ". Class must be annotated with @Service or @Component.");
        }

        List<Method> annotatedMethods = TempClassUtils.getMethodsAnnotatedWith(clazz, ANNOTATION_CLASS);
        Map<String, List<Method>> eventToMethodsMap = new HashMap<>();

        // Group methods by event name
        for (Method method : annotatedMethods) {
            EventListener listener = method.getAnnotation(EventListener.class);
            String eventName = listener.eventName();
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

            Collections.reverse(methodsWithPriority);

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

                if (Modifier.isStatic(listenerMethod.getModifiers())) {
                    listenerMethod.invoke(null);
                    continue;
                }

                Object instance = this.componentRegistry.getService(listenerMethod.getDeclaringClass());

                if (instance != null) {
                    listenerMethod.invoke(instance);
                } else {
                    throw new RuntimeException("Instance of class not found for invoking non-static method: " + listenerMethod.getName());
                }
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

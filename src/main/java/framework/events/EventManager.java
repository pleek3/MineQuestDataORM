package framework.events;

import framework.events.model.EventListener;
import framework.events.model.StandardEvent;

/**
 * The EventManager interface provides methods to manage events and event listeners
 * for an event-driven architecture.
 */
public interface EventManager {

    /**
     * Registers an event with the given event name. If the event is already registered,
     * a RuntimeException will be thrown.
     *
     * @param eventName The name of the event to register.
     * @throws RuntimeException If the event is already registered.
     */
    void registerEvent(String eventName);

    /**
     * Registers multiple events with the provided event names. If any of the events
     * are already registered, a RuntimeException will be thrown.
     *
     * @param eventNames The names of the events to register.
     * @throws RuntimeException If any of the events are already registered.
     */
    void registerEvents(String... eventNames);

    /**
     * Registers event listeners for the provided class. Event listeners are methods
     * annotated with the {@link EventListener} annotation.
     *
     * @param clazz The class containing event listener methods.
     */
    void registerListener(Class<?> clazz);

    /**
     * Dispatches an event to its registered listeners. This method notifies all
     * listeners that are registered for the specified event name.
     *
     * @param eventName The name of the event to dispatch.
     */
    void dispatch(String eventName);

    /**
     * Dispatches a standard event to its registered listeners. This method notifies
     * all listeners that are registered for the specified standard event.
     *
     * @param standardEvent The standard event to dispatch.
     */
    void dispatch(StandardEvent standardEvent);

}

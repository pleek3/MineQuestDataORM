package framework.events.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class EventDetail {

    private final String eventName;

    private final List<Method> eventListener = new ArrayList<>();

}

package framework.listener.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StandardEvent {

    AFTER_CONTEXT_INITIALIZED("afterContextInitialized"),
    ;

    private final String eventName;

}

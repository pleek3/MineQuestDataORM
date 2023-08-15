package framework.entity;

import framework.util.TempClassUtils;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@Getter
public class EntityClassCollector {

    private final static Class<? extends Annotation> ENTITY_ANNOTATION_CLASS = Entity.class;

    private final Set<Class<?>> entityClasses = new HashSet<>();

    public EntityClassCollector() {
    }

    public Set<Class<?>> findEntityClasses(String basePackage) {
        this.entityClasses.clear();
        this.entityClasses.addAll(TempClassUtils.findAllAnnotatedClasses(basePackage, ENTITY_ANNOTATION_CLASS));
        return new HashSet<>(this.entityClasses); // Return a new HashSet to avoid external modification
    }

}

package com.minequest.dataorm;

import jakarta.persistence.Table;
import lombok.Getter;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ReflectionUtilsPredicates;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

@Getter
public class EntityMetaDataStorage<E> {

    private final Class<? extends Annotation> ID_ANNOTATION_CLASS = Annotation.class;

    private final Class<E> entityClass;

    public EntityMetaDataStorage(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public Object identifyID(E instance) {
        Set<Field> idFields = ReflectionUtils.getAllFields(entityClass, ReflectionUtilsPredicates.withAnnotation(ID_ANNOTATION_CLASS));

        if (idFields.isEmpty()) {
            throw new RuntimeException("No @Id annotated field found in class " + entityClass.getSimpleName());
        }

        if (idFields.size() > 1) {
            throw new RuntimeException("Multiple @Id annotated fields found in class " + entityClass.getSimpleName());
        }

        Field idField = idFields.iterator().next();
        idField.setAccessible(true);
        try {
            return idField.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error getting ID value from entity", e);
        }
    }

    public Table identifyTable() {
        Reflections reflections = new Reflections(entityClass.getPackage().getName());
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Table.class);

        for (Class<?> annotatedClass : annotatedClasses) {
            if (annotatedClass.equals(entityClass)) {
                Table tableAnnotation = annotatedClass.getAnnotation(Table.class);
                if (tableAnnotation != null) {
                    return tableAnnotation;
                }
            }
        }
        throw new RuntimeException("Cannot find @Table annotation for class: " + entityClass.getName());
    }


}

package com.minequest.dataorm.utils;

import java.lang.annotation.Annotation;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

@UtilityClass
public class ClassUtils {

  public Set<Class<?>> findAllAnnotatedClasses(String packageToScan, Class<? extends Annotation> annotation) {
    Reflections reflections = new Reflections(packageToScan);
    return reflections.getTypesAnnotatedWith(annotation);
  }

}

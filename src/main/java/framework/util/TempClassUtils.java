package framework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

@UtilityClass
public class TempClassUtils {

    //todo: change class name in service registry
    //todo: change name

    public Set<Class<?>> findAllAnnotatedClasses(String packageToScan, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packageToScan);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

}

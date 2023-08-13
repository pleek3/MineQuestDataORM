package com.minequest.dataorm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ReflectionUtilsPredicates;

public class DataRepositoryImpl<E, ID> implements CrudDataRepository<E, ID> {

  private static final Class<? extends Annotation> ID_ANNOTATION_CLASS = Id.class;

  private final EntityManager entityManager;
  private final E entity;
  private final Class<?> entityClass;

  private final String tableName;

  private final Session session;

  public DataRepositoryImpl(E entity, EntityManager entityManager) {
    this.entity = entity;
    this.entityClass = entity.getClass();
    this.entityManager = entityManager;

    this.tableName = getTableAnnotation().name();

    this.session = entityManager.unwrap(Session.class);
  }

  @Override
  public E save(E entity) {
    Class<?> entityClass = entity.getClass();

    if (!isAnnotationPresent(entityClass, ID_ANNOTATION_CLASS)) {
      throw new RuntimeException("Class " + entityClass.getSimpleName() + " has no @Id annotated field");
    }

    Object idValue = getIdFieldValue(entity);

    if (null != idValue && !existsInDatabase(entityClass, idValue)) {
      this.entityManager.merge(entity);
    } else {
      this.entityManager.persist(entity); //sollte reichen ig?
    }

    return entity;
  }

  @Override
  public E findById(ID id) {
    return null;
  }

  @Override
  public List<E> findAll() {
    Session session = null;

    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<?> cq = builder.createQuery(this.entityClass);

    session.createQuery(cq).setFirstResult(0).setMaxResults(1).list();


    return null;
  }

  @Override
  public void deleteById(ID id) {

  }

  public boolean existsInDatabase(Class<?> entityClass, Object id) {
    return entityManager.find(entityClass, id) != null;
  }

  private boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotationClass) {
    return Arrays.stream(clazz.getDeclaredFields())
        .anyMatch(field -> field.isAnnotationPresent(annotationClass));
  }

  private Object getIdFieldValue(E entity) {
    Class<?> entityClass = entity.getClass();

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
      return idField.get(entity);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Error getting ID value from entity", e);
    }
  }

  public Table getTableAnnotation() {
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

package com.minequest.dataorm.provider;

import com.minequest.dataorm.DataCompound;
import com.minequest.dataorm.EntityMetaDataStorage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * An implementation of the {@link Provider} interface using Hibernate as the underlying data source.
 *
 * @param <E>  The type of entity to be managed.
 * @param <ID> The type of the entity's ID.
 */
public class HibernateProviderImpl<E, ID> implements Provider<E, ID> {

    private final EntityMetaDataStorage<E> entityMetaDataStorage;
    private final DataCompound dataCompound;

    /**
     * Constructs a new instance of {@link HibernateProviderImpl}.
     *
     * @param metaDataStorage The meta-data storage for the entity.
     * @param compound        The data compound containing the {@link EntityManager}.
     */
    public HibernateProviderImpl(EntityMetaDataStorage<E> metaDataStorage, DataCompound compound) {
        this.dataCompound = compound;
        this.entityMetaDataStorage = metaDataStorage;
    }

    /**
     * Retrieves all entities of the specified type from the data source.
     *
     * @return A list of entities.
     */
    @Override
    public List<E> findAll() {
        Session session = getSession();

        Class<E> entityClass = this.entityMetaDataStorage.getEntityClass();

        return session.createQuery("SELECT e FROM Entity e", entityClass).getResultList();
    }

    /**
     * Saves an entity to the data source.
     *
     * @param entity The entity to be saved.
     * @return An {@link Optional} containing the saved entity, or an empty {@link Optional} if the operation fails.
     */
    @Override
    public Optional<E> save(E entity) {
        Object idValue = this.entityMetaDataStorage.identifyID(entity);
        if (idValue == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    /**
     * Inserts a new entity into the data source.
     *
     * @param entity The entity to be inserted.
     * @return An {@link Optional} containing the inserted entity, or an empty {@link Optional} if the operation fails.
     */
    private Optional<E> insert(E entity) {
        try {
            Session session = getSession();
            EntityTransaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(entity);
            transaction.commit();
            return Optional.of(entity);
        } catch (PersistenceException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Updates an existing entity in the data source.
     *
     * @param entity The entity to be updated.
     * @return An {@link Optional} containing the updated entity, or an empty {@link Optional} if the operation fails.
     */
    private Optional<E> update(E entity) {
        try {
            Session session = getSession();
            EntityTransaction transaction = session.getTransaction();
            transaction.begin();
            session.merge(entity);
            transaction.commit();
            return Optional.of(entity);
        } catch (PersistenceException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieves the Hibernate {@link Session} from the underlying {@link EntityManager}.
     *
     * @return The Hibernate session.
     */
    private Session getSession() {
        return this.dataCompound.getSessionFactory().openSession();
    }
}
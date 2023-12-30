package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.KibuEntity;

import java.util.Collection;

public interface EntityStorage {

    /**
     * Adds an entity to the storage.
     * @param entity The entity to add.
     * @return True, if the storage did not already contain the entity.
     */
    boolean addEntity(KibuEntity entity);

    /**
     * Removes an entity from the storage.
     * @param entity The entity to remove.
     * @return True, if the entity was removed from the storage. False if the entity was not in the storage.
     */
    boolean removeEntity(KibuEntity entity);

    /**
     * Get an immutable view of all entities in this storage.
     * @return A collection of all entities in this storage.
     */
    Collection<? extends KibuEntity> getEntities();
}

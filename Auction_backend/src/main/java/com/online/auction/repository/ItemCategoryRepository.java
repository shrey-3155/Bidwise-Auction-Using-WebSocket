package com.online.auction.repository;

import com.online.auction.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link ItemCategory} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and a custom query method
 * for {@link ItemCategory} entities, with the primary key of type {@code Integer}.
 * </p>
 */
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Integer> {
    /**
     * Finds an item category by its name.
     * <p>
     * This method retrieves an {@link ItemCategory} entity with the specified category name.
     * </p>
     *
     * @param name The name of the item category to be found.
     * @return An {@link Optional} containing the {@link ItemCategory} entity if found, otherwise {@code Optional.empty()}.
     */
    Optional<ItemCategory> findByItemCategoryName(String name);
}

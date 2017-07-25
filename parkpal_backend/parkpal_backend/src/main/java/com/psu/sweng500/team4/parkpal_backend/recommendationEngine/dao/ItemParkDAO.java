package com.psu.sweng500.team4.parkpal_backend.recommendationEngine.dao;

import org.grouplens.lenskit.data.dao.ItemDAO;

import javax.annotation.Nullable;

/**
 * Extended item DAO that loads item titles.
 *
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public interface ItemParkDAO extends ItemDAO {
    /**
     * Get the title for a movie.
     * @param item The park ID.
     * @return The park's name, or {@code null} if no such park exists.
     */
    @Nullable
    public String getItemName(long item);
}

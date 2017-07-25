package com.psu.sweng500.team4.parkpal_backend.recommendationEngine.dao;

import com.psu.sweng500.team4.parkpal_backend.MyDBConnection;
import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.EventCollectionDAO;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.sql.BasicSQLStatementFactory;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;

import javax.inject.Inject;

/**
 * Customized rating DAO for MOOC ratings.  This just wraps some standard LensKit DAOs in an
 * easy-to-configure interface.
 *
 * @see EventCollectionDAO
 * @see SimpleFileRatingDAO
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class ParkRatingDAO implements EventDAO {
    private transient volatile EventCollectionDAO cache;
    private final JDBCRatingDAO jdbcRatingDAO;
    MyDBConnection db;

    @Inject
    public ParkRatingDAO() {
        BasicSQLStatementFactory basicSQLStatementFactory = new BasicSQLStatementFactory();
        db = new MyDBConnection();

        basicSQLStatementFactory.setTableName("PARK_RATINGS");
        basicSQLStatementFactory.setItemColumn("park_id");
        basicSQLStatementFactory.setRatingColumn("rating");
        basicSQLStatementFactory.setUserColumn("user_id");
        basicSQLStatementFactory.setTimestampColumn(null);

        jdbcRatingDAO = new JDBCRatingDAO(db.getConnection(), basicSQLStatementFactory);
    }

    /**
     * Pre-fetch the ratings into memory if we haven't done so already.
     */
    private void ensureRatingCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new EventCollectionDAO(Cursors.makeList(jdbcRatingDAO.streamEvents()));
                }
            }
        }
    }

    @Override
    public Cursor<Event> streamEvents() {
        // delegate to the cached event collection DAO
        ensureRatingCache();
        return cache.streamEvents();
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type) {
        // delegate to the cached event collection DAO
        ensureRatingCache();
        return cache.streamEvents(type);
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type, SortOrder order) {
        // delegate to the cached event collection DAO
        ensureRatingCache();
        return cache.streamEvents(type, order);
    }
}

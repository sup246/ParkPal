package com.psu.sweng500.team4.parkpal_backend.dao;

import com.psu.sweng500.team4.parkpal_backend.MyDBConnection;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.dao.DataAccessException;
import org.grouplens.lenskit.util.DelimitedTextCursor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class ParkDAO implements ItemParkDAO {
    private transient volatile Long2ObjectMap<String> parkCache;

    private void ensureParkCache() {
        if (parkCache == null) {
            synchronized (this) {
                if (parkCache == null) {
                    parkCache = loadParkCache();
                }
            }
        }
    }

    private Long2ObjectMap<String> loadParkCache() {
        Long2ObjectMap<String> cache = new Long2ObjectOpenHashMap<String>();
        MyDBConnection db = new MyDBConnection();
        HashMap<Integer,String> parks = new HashMap<Integer,String>();

        parks = db.getAllParks();
        Iterator it = parks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            cache.put((Integer)pair.getKey(),pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }

        return cache;
    }

    @Override
    public LongSet getItemIds() {
        ensureParkCache();
        return LongSets.unmodifiable(parkCache.keySet());
    }

    @Override
    public String getItemName(long item) {
        return parkCache.get(item);
    }
}

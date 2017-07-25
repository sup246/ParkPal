package com.psu.sweng500.team4.parkpal_backend.recommendationEngine.dao;

import com.google.common.collect.ImmutableSet;
import com.psu.sweng500.team4.parkpal_backend.MyDBConnection;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import javax.inject.Inject;
import java.util.*;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class ParkTagDAO extends ParkDAO implements ItemTagDAO {
    private transient volatile Long2ObjectMap<List<String>> tagCache;
    private transient volatile Set<String> vocabCache;

    @Inject
    public ParkTagDAO() {
        super();
    }

    private void ensureTagCache() {
        if (tagCache == null) {
            synchronized (this) {
                if (tagCache == null) {
                    loadTagCache();
                }
            }
        }
    }

    private void loadTagCache() {
        tagCache = new Long2ObjectOpenHashMap<List<String>>();
        ImmutableSet.Builder<String> vocabBuilder = ImmutableSet.builder();
        MyDBConnection db = new MyDBConnection();
        HashMap<Integer, List<String>> tags = new HashMap<Integer, List<String>>();

        tags = db.getAllParkTags();
        Iterator it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            tagCache.put((Integer)pair.getKey(),(List<String>)pair.getValue());

            for (String tag : (List<String>)pair.getValue()) {
                if (tag == null || !tag.isEmpty()) {
                    vocabBuilder.add(tag);
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        vocabCache = vocabBuilder.build();
    }

    @Override
    public List<String> getItemTags(long item) {
        ensureTagCache();
        List<String> tags = tagCache.get(item);
        if (tags != null) {
            return Collections.unmodifiableList(tags);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Set<String> getTagVocabulary() {
        ensureTagCache();
        return vocabCache;
    }
}

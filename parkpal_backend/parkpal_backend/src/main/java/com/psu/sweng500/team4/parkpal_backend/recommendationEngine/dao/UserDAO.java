package com.psu.sweng500.team4.parkpal_backend.recommendationEngine.dao;

import com.psu.sweng500.team4.parkpal_backend.MyDBConnection;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.grouplens.lenskit.collections.LongUtils;

import java.util.*;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class UserDAO implements UserNameDAO {
    private transient volatile Object2LongMap<String> nameCache;
    private transient volatile LongSet userIds;

    private void ensureNameCache() {
        if (nameCache == null) {
            synchronized (this) {
                if (nameCache == null) {
                    nameCache = loadNameCache();
                    userIds = LongUtils.packedSet(nameCache.values());
                }
            }
        }
    }

    private Object2LongMap<String> loadNameCache() {
        Object2LongMap<String> cache = new Object2LongOpenHashMap<String>();
        MyDBConnection db = new MyDBConnection();
        HashMap<Integer,String> users = new HashMap<Integer,String>();

        users = db.getAllUsers();
        Iterator it = users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            cache.put(pair.getValue().toString(),(Integer)pair.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }

        return cache;

//        Object2LongMap<String> cache = new Object2LongOpenHashMap<String>();
//        // make the cache return -1 for missing users
//        cache.defaultReturnValue(-1);
//        Cursor<String[]> lines = null;
//        try {
//            lines = new DelimitedTextCursor(userFile, ",");
//        } catch (FileNotFoundException e) {
//            throw new DataAccessException("cannot open file", e);
//        }
//        try {
//            for (String[] line: lines) {
//                long uid = Long.parseLong(line[0]);
//                cache.put(line[1], uid);
//            }
//        } finally {
//            lines.close();
//        }
//        return cache;
    }

    @Override
    public LongSet getUserIds() {
        ensureNameCache();
        return userIds;
    }

    @Override
    public long getUserByName(String name) {
        ensureNameCache();
        return nameCache.get(name);
    }
}

package com.psu.sweng500.team4.parkpal_backend.recommendationEngine;

import com.psu.sweng500.team4.parkpal_backend.MyDBConnection;
import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.scored.ScoredId;
import com.psu.sweng500.team4.parkpal_backend.recommendationEngine.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Simple hello-world program.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class RecommendationEngine {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationEngine.class);
    private static final ClassLoader loader = RecommendationEngine.class.getClassLoader();
    private MyDBConnection db = new MyDBConnection();

    public String getRecommendations(String userId) throws RecommenderBuildException {
        LenskitConfiguration config = configureRecommender();
        String recommendations = new String();

        db.getAllParks();

        logger.info("building recommender");
        Recommender rec = LenskitRecommender.build(config);

        if (userId.length() == 0) {
            logger.error("No user specified; provide user IDs as command line arguments");
        }

        // we automatically get a useful recommender since we have a scorer
        ItemRecommender irec = rec.getItemRecommender();
        assert irec != null;
        try {
            // Generate 5 recommendations for each user
            long uid;
            try {
                uid = Long.parseLong(userId);
            } catch (NumberFormatException e) {
                logger.error("cannot parse user {}", userId);
                return null;
            }
            logger.info("searching for recommendations for user {}", userId);
            List<ScoredId> recs = irec.recommend(uid, 5);
            if (recs.isEmpty()) {
                logger.warn("no recommendations for user {}, do they exist?", uid);
            }
            System.out.format("recommendations for user %d:\n", uid);
            //recommendations += String.format("recommendations for user %d:\n", uid);
            for (ScoredId id: recs) {
                System.out.format("  %d: %.4f\n", id.getId(), id.getScore());
                //recommendations += String.format("  %d: %.4f\n", id.getId(), id.getScore());
                if (!recommendations.isEmpty()) {
                    recommendations += " ";
                }
                recommendations += id.getId();
            }
        } catch (UnsupportedOperationException e) {
            if (e.getMessage().equals("stub implementation")) {
                System.out.println("Congratulations, the stub builds and runs!");
            }
        }

        return recommendations;
    }

    /**
     * Create the LensKit recommender configuration.
     * @return The LensKit recommender configuration.
     */
    // LensKit configuration API generates some unchecked warnings, turn them off
    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureRecommender() {

        LenskitConfiguration config = new LenskitConfiguration();
        // configure the rating data source
        config.bind(EventDAO.class)
              .to(ParkRatingDAO.class);

        // use custom item and user DAOs
        // specify item DAO implementation with tags
        config.bind(ItemDAO.class)
              .to(ParkTagDAO.class);

        // our user DAO can look up by user name
        config.bind(UserDAO.class)
              .to(com.psu.sweng500.team4.parkpal_backend.recommendationEngine.dao.UserDAO.class);

        // use the TF-IDF scorer you will implement to score items
        config.bind(ItemScorer.class)
              .to(TFIDFItemScorer.class);
        return config;
    }
}
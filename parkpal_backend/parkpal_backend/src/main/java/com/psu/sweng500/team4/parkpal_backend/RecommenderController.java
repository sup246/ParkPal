package com.psu.sweng500.team4.parkpal_backend;

import com.psu.sweng500.team4.parkpal_backend.recommendationEngine.RecommendationEngine;
import org.grouplens.lenskit.RecommenderBuildException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommenderController {
    @RequestMapping("/recommendations/user/{userId}")
    public @ResponseBody String getRecommendations(@PathVariable String userId){

        RecommendationEngine recommendationEngine = new RecommendationEngine();
        String result = new String();
        try {
            result = recommendationEngine.getRecommendations(userId);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }
        return result;
    }
}

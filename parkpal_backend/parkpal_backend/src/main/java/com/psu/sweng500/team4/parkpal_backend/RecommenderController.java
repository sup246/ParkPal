package com.psu.sweng500.team4.parkpal_backend;

import org.grouplens.lenskit.RecommenderBuildException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommenderController {
    @RequestMapping("/recommendations/user/{userId}")
    public String getRecommendations(@PathVariable String userId){

        CBFMain cbf = new CBFMain();
        String result = new String();
        try {
            result = cbf.getRecommendations(userId);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }
        return result;
    }
}

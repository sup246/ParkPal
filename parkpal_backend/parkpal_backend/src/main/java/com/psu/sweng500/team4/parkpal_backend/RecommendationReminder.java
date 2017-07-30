package com.psu.sweng500.team4.parkpal_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RecommendationReminder {
    private static final Logger log = LoggerFactory.getLogger(RecommendationReminder.class);

    private static final String notification = "recommendations";
    private static final String notificationURL = "http://parkpalapp.azurewebsites.net/api/notification/";


    @Scheduled(fixedRate = 300000) // 5 minutes
    public void notifyUsers() {
        String finalNote = notificationURL + notification;
        System.out.println(finalNote);

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(finalNote, String.class);
        log.info(result);
    }
}
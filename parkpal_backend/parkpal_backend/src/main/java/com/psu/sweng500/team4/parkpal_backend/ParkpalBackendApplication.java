package com.psu.sweng500.team4.parkpal_backend;

import com.psu.sweng500.team4.parkpal_backend.dao.ParkTagDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParkpalBackendApplication {

	public static void main(String[] args) {
	    SpringApplication.run(ParkpalBackendApplication.class, args);

//		CBFMain cbf = new CBFMain();
//		try {
//			cbf.getRecommendations("1");
//		} catch (RecommenderBuildException e) {
//			e.printStackTrace();
//		}

		ParkTagDAO parkTags = new ParkTagDAO();
		parkTags.getItemTags(1);
		parkTags.getTagVocabulary();

	}
}

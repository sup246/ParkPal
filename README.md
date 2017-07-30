# ParkPal

To open in Android studio: File -> New -> Import from Version Control -> Github

Might need to Build -> make project first before running

To run the backend:
1. Change the following line in RecommendationsFragment.java, line 40 to be your local IP address
    private String ip = "192.168.1.192";
2. Open up a terminal/powershell/etc to the parkpal_backend directory
3. Run:    java -jar parkpal_backend-0.0.1-SNAPSHOT.jar
4. Now you can run the app and use the recommendations tab.  You should see a notification every 5 minutes alerting you about new recommendations (this is just an arbitrary amount of time right now)
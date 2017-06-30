package com.psu.sweng500.team4.parkpal.RegistrationService;

/**
 * Created by P_Hui on 6/29/2017.
 */

public class RegistrationService extends IntentService {
    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID myID = InstanceID.getInstance(this);
        String registrationToken = myID.getToken(
                getString(R.string.gcm_defaultSenderId),
                GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                null
        );
        GcmPubSub subscription = GcmPubSub.getInstance(this);
        subscription.subscribe(registrationToken, "/topics/my_little_topic", null);

    }
}
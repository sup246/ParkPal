package TokenRefreshListenerService;

/**
 * Created by P_Hui on 6/29/2017.
 */

public class TokenRefreshListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
    }
}

package com.psu.sweng500.team4.parkpal;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private String mEmail;
    private String mPassword;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);


    @Before
    public void initValidString() {
        mEmail = "test@gmail.com";
        mPassword = "Password";
    }

    @Test
    public void successfulGoogleLoginActivityTest() {
        // Clicks google signin.
        onView(withId(R.id.sign_in_button)).perform(click());

//        // Verifies that the activity received an intent
//        // with the correct package name and message.
//        intended(allOf(
//                hasComponent(hasShortClassName(".MainActivity")),
//                toPackage("com.psu.sweng500.team4.parkpal")));
        Assert.fail();
    }

    @Test
    public void successfulNewUserGoogleLoginActivityTest() {
        // Clicks google signin.
        onView(withId(R.id.sign_in_button)).perform(click());

        Assert.fail();
    }

    @Test
    public void successfulEmailLoginActivityTest() {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(mPassword), closeSoftKeyboard());

        // Clicks email signin.
        onView(withId(R.id.email_sign_in_button)).perform(click());

        Assert.fail();
    }

    @Test
    public void successfulNewUserEmailLoginActivityTest() {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(mEmail));
        onView(withId(R.id.password))
                .perform(typeText(mPassword), closeSoftKeyboard());

        // Clicks email signin.
        onView(withId(R.id.email_sign_in_button)).perform(click());

        Assert.fail();
    }

    @Test
    public void failedEmailLoginActivityTest() {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(mPassword), closeSoftKeyboard());

        // Clicks email signin.
        onView(withId(R.id.email_sign_in_button)).perform(click());

        Assert.fail();
    }

    @Test
    public void successfulFacebookLoginActivityTest() {
        Assert.fail();
    }

    @Test
    public void successfulNewUserFacebookLoginActivityTest() {
        Assert.fail();
    }

    @Test
    public void failedFacebookLoginActivityTest() {
        Assert.fail();
    }
}

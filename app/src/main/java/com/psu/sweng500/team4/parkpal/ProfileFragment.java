package com.psu.sweng500.team4.parkpal;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Models.UserPrefs;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.UserPrefsQueryTask;
import com.psu.sweng500.team4.parkpal.Queries.UserPrefsUpdateTask;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.concurrent.ExecutionException;


public class ProfileFragment extends Fragment {


    private EditText mEmailView;
    private EditText mBirthDateView;
    private EditText mUsernameView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mZipCodeView;
    private Switch mKids;
    private Switch mDogs;
    private Switch mWatersports;
    private Switch mHiking;
    private Switch mCamping;
    private Switch mForest;
    private Switch mDesert;
    private Switch mMountain;
    private Switch mBeach;
    private View progressContainer;
    Button mSaveButton;

    private User profileUser;
    private UserPrefs userPrefs;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileUser = (User) this.getArguments().getSerializable("User");

        Log.d("INFO", "email : " + profileUser.getEmail());
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

    /*    TabHost tabHost = (TabHost)v.findViewById(R.id.tabHost);

        TabHost.TabSpec profileTab = tabHost.newTabSpec("Profile");
        profileTab.setIndicator("Profile");
        profileTab.setContent()

        TabHost.TabSpec accountTab = tabHost.newTabSpec("Account");*/

        progressContainer = v.findViewById(R.id.profile_progress);
        mEmailView = (EditText) v.findViewById(R.id.email);
        mFirstNameView = (EditText) v.findViewById(R.id.firstName);
        mLastNameView = (EditText) v.findViewById(R.id.lastName);
        mBirthDateView = (EditText) v.findViewById(R.id.birthdate);
        mUsernameView = (EditText) v.findViewById(R.id.userName);
        mZipCodeView = (EditText) v.findViewById(R.id.zipCode);
        mKids = (Switch) v.findViewById(R.id.prefs_kids);
        mDogs = (Switch) v.findViewById(R.id.prefs_dogs);
        mWatersports = (Switch) v.findViewById(R.id.prefs_watersports);
        mHiking = (Switch) v.findViewById(R.id.prefs_hiking);
        mCamping = (Switch) v.findViewById(R.id.prefs_camping);
        mForest = (Switch) v.findViewById(R.id.prefs_forest);
        mDesert = (Switch) v.findViewById(R.id.prefs_desert);
        mMountain = (Switch) v.findViewById(R.id.prefs_mountain);
        mBeach = (Switch) v.findViewById(R.id.prefs_beach);
        mSaveButton = (Button) v.findViewById(R.id.save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressContainer.setVisibility(View.VISIBLE);
                saveChanges();
                progressContainer.setVisibility(View.INVISIBLE);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("User", profileUser);
                Fragment fragment = GMapFragment.newInstance();
                fragment.setArguments(bundle);
                transaction.replace(R.id.content, fragment);
                transaction.commit();

            }
        });

        progressContainer.setVisibility(View.VISIBLE);

        populateProfile();
        return v;
    }

    private MobileServiceTable<User> mobileServiceUserTable(){
        return  AzureServiceAdapter.getInstance().getClient().getTable("USERS", User.class);
    }

    private void updateUserProfile(final User user) throws ExecutionException, InterruptedException {
        mobileServiceUserTable().update(user).get();
    }

    private void saveChanges(){

        profileUser.setFirstName(mFirstNameView.getText().toString());
        profileUser.setLastName(mLastNameView.getText().toString());
        profileUser.setEmail(mEmailView.getText().toString());
        profileUser.setBirthdate(mBirthDateView.getText().toString());
        profileUser.setZipCode(mZipCodeView.getText().toString());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    updateUserProfile(profileUser);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

        userPrefs.setPrefs_kids(mKids.isChecked());
        userPrefs.setPrefs_dogs(mDogs.isChecked());
        userPrefs.setPrefs_watersports(mWatersports.isChecked());
        userPrefs.setPrefs_camping(mCamping.isChecked());
        userPrefs.setPrefs_hiking(mHiking.isChecked());
        userPrefs.setPrefs_forest(mForest.isChecked());
        userPrefs.setPrefs_mountain(mMountain.isChecked());
        userPrefs.setPrefs_desert(mDesert.isChecked());
        userPrefs.setPrefs_beach(mBeach.isChecked());

        updatePrefs(userPrefs);
    }

    private void updatePrefs(UserPrefs prefs){
        // Populate prefs
        UserPrefsUpdateTask asyncQuery = new UserPrefsUpdateTask(new AsyncResponse(){
            @Override
            public void processFinish(Object result){
            }
        }, prefs);

        asyncQuery.execute();
    }

    private void populateProfile(){
        mUsernameView.setText(profileUser.getUsername());
        mFirstNameView.setText(profileUser.getFirstName());
        mLastNameView.setText(profileUser.getLastName());
        mBirthDateView.setText(profileUser.getBirthdate());
        mEmailView.setText(profileUser.getEmail());
        mZipCodeView.setText(profileUser.getZipCode());
        progressContainer.setVisibility(View.INVISIBLE);

        // Populate prefs
        UserPrefsQueryTask asyncQuery = new UserPrefsQueryTask(new AsyncResponse(){
            @Override
            public void processFinish(Object result){
                if (result == null) {
                    userPrefs = new UserPrefs(profileUser.getEmail());
                }
                else {
                    userPrefs = (UserPrefs) result;
                }

                mKids.setChecked(userPrefs.isPrefs_kids());
                mDogs.setChecked(userPrefs.isPrefs_dogs());
                mWatersports.setChecked(userPrefs.isPrefs_watersports());
                mCamping.setChecked(userPrefs.isPrefs_camping());
                mHiking.setChecked(userPrefs.isPrefs_hiking());
                mForest.setChecked(userPrefs.isPrefs_forest());
                mDesert.setChecked(userPrefs.isPrefs_desert());
                mMountain.setChecked(userPrefs.isPrefs_mountain());
                mBeach.setChecked(userPrefs.isPrefs_beach());
            }
        }, profileUser.getUsername());

        asyncQuery.execute();
    }
}

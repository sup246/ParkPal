package com.psu.sweng500.team4.parkpal;


import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.concurrent.ExecutionException;


public class ProfileFragment extends Fragment {


    private EditText mEmailView;
    private EditText mBirthDateView;
    private EditText mUsernameView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mZipCodeView;
    private View progressContainer;
    Button mSaveButton;

    private User ProfileUser;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        progressContainer = v.findViewById(R.id.profile_progress);
        mEmailView = (EditText) v.findViewById(R.id.email);
        mFirstNameView = (EditText) v.findViewById(R.id.firstName);
        mLastNameView = (EditText) v.findViewById(R.id.lastName);
        mBirthDateView = (EditText) v.findViewById(R.id.birthdate);
        mUsernameView = (EditText) v.findViewById(R.id.userName);
        mZipCodeView = (EditText) v.findViewById(R.id.zipCode);
        mSaveButton = (Button) v.findViewById(R.id.save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressContainer.setVisibility(View.VISIBLE);
                saveChanges();
                progressContainer.setVisibility(View.INVISIBLE);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, new GMapFragment());
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

        ProfileUser.setFirstName(mFirstNameView.getText().toString());
        ProfileUser.setLastName(mLastNameView.getText().toString());
        ProfileUser.setEmail(mEmailView.getText().toString());
        ProfileUser.setBirthdate(mBirthDateView.getText().toString());
        ProfileUser.setZipCode(mZipCodeView.getText().toString());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    updateUserProfile(ProfileUser);
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
    }

    private void populateProfile(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final MobileServiceTable<User> table = mobileServiceUserTable();
                try {
                    final MobileServiceList<User> results = table
                            .where()
                            .field("email")
                            .eq("foo@example.com")
                            .execute()
                            .get();

                    ProfileUser = results.get(0);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mUsernameView.setText(ProfileUser.getUsername());
                            mFirstNameView.setText(ProfileUser.getFirstName());
                            mLastNameView.setText(ProfileUser.getLastName());
                            mBirthDateView.setText(ProfileUser.getBirthdate());
                            mEmailView.setText(ProfileUser.getEmail());
                            mZipCodeView.setText(ProfileUser.getZipCode());
                            progressContainer.setVisibility(View.INVISIBLE);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


    }

}

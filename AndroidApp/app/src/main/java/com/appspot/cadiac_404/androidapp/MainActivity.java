package com.appspot.cadiac_404.androidapp;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import com.appspot.cadiac_404.androidapp.preference.SettingsFragment;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    static String TAG="CARdiac";
    SharedPreferences preferences;
    static final String PREF_ACCOUNT_NAME = "accountName";
    static final int REQUEST_ACCOUNT_PICKER = 1;

    static GoogleAccountCredential credentials;
    public static ApiCaller apiCaller;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        setAccountName(accountName);
                        apiCaller = new ApiCaller(credentials);
                    }
                }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(TAG, 0);

        credentials = GoogleAccountCredential.usingAudience(this, "server:client_id:" + Constants.WEB_CLIENT_ID);
        String accountName = preferences.getString(PREF_ACCOUNT_NAME, null);
        setAccountName(accountName);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                setTitle(R.string.title_section1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .commit();
                break;
            case 1:
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, DeveloperFragment.newInstance())
                        .commit();
                break;
        }
    }

    public void setAccountName(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_ACCOUNT_NAME, name);
        editor.commit();
        credentials.setSelectedAccountName(name);
    }

    public void signIn() {
        startActivityForResult(credentials.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

}

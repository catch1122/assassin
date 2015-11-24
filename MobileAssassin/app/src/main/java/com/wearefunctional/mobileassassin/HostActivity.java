package com.wearefunctional.mobileassassin;

/**
 * Created by Christine on 11/22/2015.
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by Christine on 11/21/2015.
 */
public class HostActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mGameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mHostFormView;
    private String password;
    private String gameName;
    private String SHAHash;
    private ClientManager cm;
    private Switch sw;
    private TextView switchStatus;
    boolean privateGame;
    String value;

    public static int NO_OPTIONS=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        mGameView = (EditText) findViewById(R.id.game_name);
        mPasswordView = (EditText) findViewById(R.id.gamePass);

        //needs to be done or else the app will crash
        Firebase.setAndroidContext(this);
        cm = new ClientManager("https://mobileassassin.firebaseio.com");

        sw = (Switch) findViewById(R.id.private_switch);
        sw.setChecked(false);
        switchStatus = (TextView) findViewById(R.id.gamePass);
        switchStatus.setVisibility(View.GONE);
        //Firebasetest
//        cm.createUserInDB("tommyTrojan@usc.edu", "Tommy", "fighton");
//        cm.createUserInDB("dickbutt@usc.edu", "Richard Butters", "1234");
//        User bro = null;
//        bro = cm.getUserDB("Richard Butters");
//        if(bro == null){
//            System.out.println("!!!!!\nBro is null\n");
//        } else{
//            System.out.println("!!!!!\nYour code works!\n");
//        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("displayString");
        }

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    try {
                        //attemptLogin();
                        createGame();
                        startActivity(new Intent(HostActivity.this, MapsActivity.class));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.host_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //attemptLogin();
                    createGame();
                    startActivity(new Intent(HostActivity.this, MapsActivity.class));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        mHostFormView = findViewById(R.id.host_form);
        mProgressView = findViewById(R.id.login_progress);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    privateGame = true;
                    switchStatus.setVisibility(View.VISIBLE);
                }
                else{
                    privateGame = false;
                    switchStatus.setVisibility(View.GONE);
                }
            }
        });

    }

    public String convertToHex(byte [] pass){
        StringBuffer buffer = new StringBuffer();
        String hexed = null;

        hexed = Base64.encodeToString(pass, 0, pass.length, NO_OPTIONS);

        buffer.append(hexed);

        return buffer.toString();

    }

    public void computeSHAHash(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        md.update(password.getBytes("UTF-8"), 0, password.length());
        byte[] hash = md.digest();
        SHAHash = convertToHex(hash);
    }


    private void createGame() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mGameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        gameName = mGameView.getText().toString();
        if(privateGame) {
            password = mPasswordView.getText().toString();

            //SHAHash now contains hashed password
            computeSHAHash(password);
        }
        else{
            password = "";
            SHAHash = "";
        }

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
        if(privateGame && TextUtils.isEmpty(password)){
            mPasswordView.setError("This field is required");
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(gameName)) {
            mGameView.setError(getString(R.string.error_field_required));
            focusView = mGameView;
            cancel = true;
        }
//      else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//      }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
           // mAuthTask = new UserLoginTask(email, SHAHash);
           // mAuthTask.execute((Void) null);

            cm.createGame(gameName, 1, privateGame, SHAHash, value);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mHostFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mHostFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mHostFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mHostFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
////        return new CursorLoader(this,
////                // Retrieve data rows for the device user's 'profile' contact.
////                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
////                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
////
////                // Select only email addresses.
////                ContactsContract.Contacts.Data.MIMETYPE +
////                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
////                .CONTENT_ITEM_TYPE},
////
////                // Show primary email addresses first. Note that there won't be
////                // a primary email address if the user hasn't specified one.
////                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
////        List<String> emails = new ArrayList<>();
////        cursor.moveToFirst();
////        while (!cursor.isAfterLast()) {
////            emails.add(cursor.getString(ProfileQuery.ADDRESS));
////            cursor.moveToNext();
////        }
//
//        //addEmailsToAutoComplete(emails);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//    }

//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }


//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(HostActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        mEmailView.setAdapter(adapter);
//    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

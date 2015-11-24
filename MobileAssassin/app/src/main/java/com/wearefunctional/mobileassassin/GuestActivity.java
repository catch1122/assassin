package com.wearefunctional.mobileassassin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.firebase.client.Firebase;

public class GuestActivity extends AppCompatActivity {
    private Button confirmB;
    private AutoCompleteTextView nameTextView;

    private ClientManager cm;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);

        confirmB = (Button) findViewById(R.id.guest_button);
        nameTextView = (AutoCompleteTextView) findViewById(R.id.guestname);

        cm = new ClientManager("https://mobileassassin.firebaseio.com");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        View.OnClickListener guestHandler = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (attemptGuest()) {
                    Intent i = new Intent(getApplicationContext(), GameLobbyActivity2.class);
                    i.putExtra("displayString", displayName);
                    startActivity(i);
//                    GuestActivity.this.startActivity(new Intent(GuestActivity.this, GameLobbyActivity2.class));
                }
            }
        };

        confirmB.setOnClickListener(guestHandler);
    }

    public boolean attemptGuest() {
        displayName = nameTextView.getText().toString();

        if (TextUtils.isEmpty(displayName)) {
            nameTextView.setError("This field is required!");
            return false;
        }

        cm.createUserInDB(null, displayName, null, true);
        return true;
//
//        if (displayName != null)
//            return true;
//        else
//            return false;
    }

}

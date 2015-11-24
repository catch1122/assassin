package com.wearefunctional.mobileassassin;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button loginB, registerB, guestB;
    private Drawable dagger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginB = (Button) findViewById(R.id.button);
        registerB = (Button) findViewById(R.id.button2);
        guestB = (Button) findViewById(R.id.button3);
        dagger = getResources().getDrawable(R.drawable.dagger);

        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.dagger);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        View.OnClickListener loginHandler = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        };

        View.OnClickListener registerHandler = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        };

        View.OnClickListener guestHandler = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, GuestActivity.class));
            }
        };

        loginB.setOnClickListener(loginHandler);
        registerB.setOnClickListener(registerHandler);
        guestB.setOnClickListener(guestHandler);


    }

}

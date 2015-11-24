package com.wearefunctional.mobileassassin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JoinActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;

    private Button button;

    private String value, gameValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.joinTextView);
        editText = (EditText) findViewById(R.id.joinEditText);
        button = (Button) findViewById(R.id.joinbutton);

        View.OnClickListener joinHandler = new View.OnClickListener() {
                public void onClick(View view) {
                    gameValue = editText.getText().toString();

                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    i.putExtra("displayString", value);
                    i.putExtra("gameName", gameValue);
                    startActivity(i);
                }
        };



    }

}

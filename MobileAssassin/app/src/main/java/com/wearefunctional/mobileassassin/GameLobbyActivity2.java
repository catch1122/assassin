package com.wearefunctional.mobileassassin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.AdapterView;
import android.view.View.OnClickListener;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GameLobbyActivity2 extends AppCompatActivity {
    private ListView listView;
    private Button hostB, joinB;

    private ClientManager cm;
    private int itemPlace;
    private String value, itemValue;

    private Vector<Game> mGames;
    private String[] games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostB = (Button) findViewById(R.id.button);
        joinB = (Button) findViewById(R.id.button2);

        listView = (ListView) findViewById(R.id.game_list);

        ClientManager cm = new ClientManager("https://mobileassassin.firebaseio.com");

        mGames = cm.getGames();

        games = new String[mGames.size()];

        for (int i = 0; i < mGames.size(); i++) {
            games[i] = mGames.get(i).getName();
        }

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, games));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPlace = position;
                itemValue = (String) listView.getItemAtPosition(itemPlace);

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("displayString");
        }

        // Event Handlers for Buttons
        View.OnClickListener hostHandler = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GameLobbyActivity2.this.startActivity(new Intent(GameLobbyActivity2.this, HostActivity.class));
            }
        };

        View.OnClickListener joinHandler = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                GameLobbyActivity2.this.startActivity(new Intent(GameLobbyActivity2.this, JoinActivity.class));
                if (checkJoin())
                    GameLobbyActivity2.this.startActivity(new Intent(GameLobbyActivity2.this, JoinActivity.class));
            }
        };

        hostB.setOnClickListener(hostHandler);
        joinB.setOnClickListener(joinHandler);


//        ArrayList<String> data = new ArrayList<String>();
//
//        for (int i = 0; i < mGames.size(); i++) {
//            data.add(mGames.get(i).getName());
//        }
//
//        listView = (ListView) findViewById(R.id.game_list);
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, data));
//
//
//        // Event Handlers for Buttons
//        View.OnClickListener hostHandler = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                GameLobbyActivity2.this.startActivity(new Intent(GameLobbyActivity2.this, HostActivity.class));
//            }
//        };
//
//        View.OnClickListener joinHandler = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //GameLobbyActivity2.this.startActivity(new Intent(GameLobbyActivity2.this, JoinActivity.class));
//            }
//        };
//
//        hostB.setOnClickListener(hostHandler);
//        joinB.setOnClickListener(joinHandler);
//
//        listView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//
//            }
//
//            @Override
//            public void onItemClick(AdapterView<> parentMisc, View viewMisc, int positionMisc, long idMisc) {
//                position = positionMisc;
//                value = (String) listView.getItemAtPosition(position);
//
//                // SEND NAME OF GAME TO SERVER
//
//            }
//        });
    }

    public boolean checkJoin() {

        String displayName = value;
        String gameName = itemValue;
        int numPlayers = mGames.get(itemPlace).getNumPlayers();

        cm.setUserInGame(displayName, gameName, numPlayers);


        return true;
    }


}

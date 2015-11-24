package com.wearefunctional.mobileassassin;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import java.util.Vector;

/**
 * Created by Matt on 11/21/15.
 */
public class Player extends User {
    private String userName;
    private String displayName;
    private String password;
    private boolean timeStamp;

    private double longitude;
    private double latitude;
    private boolean isAlive;
    private boolean isGuest;
    private String targetName;

    private ClientManager cm;
    private MapsActivity mMapsActivity;

    public Player(Boolean isAlive, String username, String displayName, String password, Context context){
        super();
        mMapsActivity = new MapsActivity();
        Intent i = new Intent(context, MapsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        this.userName = username;
        this.displayName = displayName;
        this.password = password;
        timeStamp = false;
        this.isAlive = isAlive;
        //gamesList = new Vector<String>();
        //targetName = null;

        /*Temporary for testing purposes*/
        targetName = displayName;
        childFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Within getUserDB/onDataChange: ");
                System.out.println("username: " + dataSnapshot.child("displayName").getValue() + "\n"
                        + dataSnapshot.child("userName").getValue());
                User use = new User((String) dataSnapshot.child("userName").getValue(),
                        (String) dataSnapshot.child("displayName").getValue(),
                        (String) dataSnapshot.child("password").getValue(),
                        (String) dataSnapshot.child("gameName").getValue());
                currentUser = use;
                /*System.out.println("There are " + dataSnapshot.getChildrenCount() + " children.");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//fkajsdfl;kjsdf
                    User use = ds.getValue(User.class);
                    String dname = (String) ds.child("displayName").getValue();
                    String email = (String) ds.child("userName").getValue();
                    String password = (String) ds.child("password").getValue();

                    User use = new User();
                    use.displayName = dname;
                    use.userName = email;
                    use.password = password;
                    //System.out.println(dname);
                    System.out.println("Display Name: " + use.getDisplayName());
                    if (use.getDisplayName() != null && displayName != null) {
                        if (use.getDisplayName().equals(displayName)) {
                            currentUser = use;
                        }
                    }
                }*/
            }
    }

    //used to set the Player's internal client manager
    public void setClientManager(ClientManager cm){
        this.cm = cm;
    }

    //used to set target
    //must be done before getTarget
    //the only way to set this
    public void setTarget(String targetName){
        this.targetName = targetName;
    }

    //returns the target name
    public String getTargetName(){
        return targetName;
    }

    //returns the username
    public String getUserName(){
        return userName;
    }

    //returns the password
    public String getPassword(){
        return password;
    }

    //return the display name
    public String getDisplayName(){
        return displayName;
    }

    //returns whether the player is dead or alive
    public boolean playIsAlive(){
        return isAlive;
    }

    public void addGame(String name){
        //gamesList.add(name);
    }

    /*public Vector<String> getGamesList(){
        return gamesList;
    }*/

    public void setTimeStamp(double length){
        //make sure you pass in the current window?
        //Firebase.setAndroidContext(this);
        final ClientManager cm = new ClientManager("https://mobileassassin.firebaseio.com");

        int minute = 60000;
        //set some shitty variable here
        timeStamp = true;
        //update the user class on firebase by calling create
        cm.changeTimeStamp(getThisUser(), true);
        new CountDownTimer((int)(length*minute), 1000) {

            public void onTick(long millisUntilFinished) {
                //nothing needs to be done here since we only update activation and after
            }

            public void onFinish() {
                //set some shitty variable here
                timeStamp = false;
                //update the user class on firebase by calling create
                cm.changeTimeStamp(getThisUser(), false);
            }
        }.start();
    }

    public User getThisUser(){
        User u = new User(userName, displayName, password);
        return u;
    }

    public double getPlayerLongitude(String playerName){
        ClientManager cm = new ClientManager("https://mobileassassin.firebaseio.com");
        return cm.getUserDB(playerName).getLongitude();
    }

    public double getPlayerLatitude(String playerName){
        ClientManager cm = new ClientManager("https://mobileassassin.firebaseio.com");
        return cm.getUserDB(playerName).getLatitude();
    }

    public void setPlayerLongitude(){
        ClientManager cm = new ClientManager("https://mobileassassin.firebaseio.com");
        //add in the map getter
        cm.setLongitude(getThisUser().getDisplayName(), mMapsActivity.getCurrentPlayerLatitude());
    }

    public void setPlayerLatitude(){
        ClientManager cm = new ClientManager("https://mobileassassin.firebaseio.com");
        cm.setLatitude(getThisUser().getDisplayName(), mMapsActivity.getCurrentPlayerLongitude());
    }
    public void setDead(String playerName){
        final ClientManager cm = new ClientManager("https://mobileassassin.firebaseio.com");
        cm.kill(playerName);

    }
    public void passPlayerToMapsActivity(){
        mMapsActivity.setPlayer(this);
    }

    /*
    “I’ve been thinking.
    When life gives you lemons?
    Don’t make lemonade.
    Make life take the lemons back!
    Get mad! I don’t want your damn lemons!
    What am I supposed to do with these?
    Demand to see life’s manager!
    Make life rue the day it thought is could give me lemons!
    Do you know who I am?
    I’m the man who’s going to burn your house down!
    With the lemons!
    I’m going to get my engineers to invent a combustible lemon that burns your house down!”
    -Cave Johnson
    */
}

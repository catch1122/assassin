package com.wearefunctional.mobileassassin;

import android.location.Location;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Vector;

public class ClientManager{
    //reference to our main database
    Firebase selfFB;
    public User currentUser;

    public boolean exists = false;
    //instantiates the reference to our firebase
    public ClientManager(String fireBaseURL) {
        selfFB = new Firebase(fireBaseURL);
    }

    //create a user
    //this will be called from our GameManager
    public void createUserInDB(String email, String displayName, String password, boolean guest){
        //create a user object
        User newUser = new User();
        /*newUser.userName = email;
        newUser.displayName = displayName;
        newUser.password = password;
        newUser.isGuest = guest;
        //push that user object to our firebase
        newUser.isAlive = true;*/

        Firebase childFB = selfFB.child("users").child(displayName);
        childFB.setValue(newUser);
        childFB.child("username").setValue(email);
        childFB.child("displayName").setValue(displayName);
        childFB.child("password").setValue(password);
        childFB.child("isGuest").setValue(guest);
        childFB.child("isAlive").setValue(true);
        childFB.child("gameName").setValue("none");
    }

    public void changeTimeStamp(User u, boolean isSafe){
        Firebase childFB = selfFB.child("users").child(u.getDisplayName());
        childFB.child("timeStamp").setValue(isSafe);
    }
    //grabs the user object from the database
    public User getUserDB(final String displayName) {
        //this is a way to cheat assigning a non-static variable from the context
        //of an anonymous inner class
        currentUser = new User();
        System.out.println("Within get getUserDB DisplayName: " + displayName);
        //instantiating in case of a null pull
        currentUser = null;
        Firebase childFB = selfFB.child("users").child(displayName);

        childFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Within getUserDB/onDataChange: ");
                System.out.println("username: " + dataSnapshot.child("displayName").getValue() + "\n"
                        + dataSnapshot.child("userName").getValue());
                User use = new User((String) dataSnapshot.child("userName").getValue(),
                        (String) dataSnapshot.child("displayName").getValue(),
                        (String) dataSnapshot.child("password").getValue());
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

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        /*if(currentUser == null) {
            System.out.println("ITS NULL");
        }
        else{
            System.out.println("ITS NOT NULL, NAME IS: " + displayName);
        }*/
        return currentUser;
    }
    //get the hashed user password from the database
    public String getHashPass(String displayName) {
        //grabs the local hashed copy of the display name in the user database
        //System.out.println("ALKJSDF:LSKDLDSFLKDS THIS IS THE PASSWORD LASJDFL:DLFJS");
        //User use = getUserDB(displayName);
        //System.out.println(use.getPassword());

        return currentUser.getPassword();
    }
    //push the user's current location to the database
    /*public void pushLocation(String displayName, Location location){
        return;
    }*/
    //grabs the user's current location from the database
    //public Location getLocation(String displayName){
    //    return getUserDB(displayName).;
    //}
    //checks whether there exists a valid instance of
    //said email in the database
    public boolean checkEmail(final String displayName) {
        Firebase childFB = selfFB.child("users").child(displayName);
        String dispname = childFB.child("displayName").child(displayName).getKey();
        return dispname.equals(displayName);
    }

//    public void createGame(String gameName, int numPlayers) //should take in numPlayers and list of player names
//    {
//        //basic creategame code
//        Firebase games = selfFB.child("games");
//        Game g = new Game(gameName, 3);
//        Firebase gamesSub = games.child(gameName);
//        gamesSub.setValue(g);
//    }

    public void createGame(String gameName, int numPlayers, boolean isPrivate, String password){
        //basic creategame code
        Firebase games = selfFB.child("games");
        Game g = new Game(gameName, 3);
        g.setPrivate(isPrivate);
        g.setPassword(password);
        Firebase gamesSub = games.child(gameName);
        gamesSub.setValue(g);
    }



    public void createGame(String gameName, int numPlayers, boolean isPrivate, String password, String displayName){
        //basic creategame code
        Firebase games = selfFB.child("games");
        Game g = new Game(gameName, 1, isPrivate, password, displayName);
        g.setPrivate(isPrivate);
        g.setPassword(password);
        Firebase gamesSub = games.child(gameName);
        gamesSub.setValue(g);
    }

    public void clearPlayers(String gameName) {
        Firebase targetGame = selfFB.child("games").child(gameName);
        targetGame.child("mPlayerNames").removeValue();
    }

    public Vector<Game> getGames() {
        Firebase childFB = selfFB.child("games");
        //this is a way to cheat assigning a non-static variable from the context
        //of an anonymous inner class
        final Vector<Game> currentGames = new Vector<Game>();

        childFB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Within getGames' onDataChange, " + dataSnapshot.getChildrenCount() + " children");
                for (DataSnapshot ds : dataSnapshot.getChildren())//accesses high level games
                {

                    Game g = ds.getValue(Game.class);
                    currentGames.add(g);
                }
                System.out.println("\n");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        return currentGames;
    }

    public void setUserInGame(String displayName, String gameName, int numPlayers) {
        Firebase gameFB = selfFB.child("games").child(gameName);
        gameFB.child("playerNames").child(Integer.toString(numPlayers)).setValue(displayName);

        Firebase userFB = selfFB.child("users").child(displayName);
        userFB.child("gameName").setValue(gameName);

    }

    public boolean isGuest(String displayName) {
        Firebase childFB = selfFB.child("users").child(displayName).child("isGuest");
        return childFB.getKey().equalsIgnoreCase("true");
    }
//
//    public Vector<User> getActivePlayers() {
//        Vector<User> actives = new Vector<User>();
//
//        Firebase childFB = selfFB.child("users");
//
//        return actives;
//
//    }

    public double getLongitude(String displayName){
        User u = getUserDB(displayName);
        return u.getLongitude();
    }

    public void setLongitude(String displayName, double longitude){
        Firebase childFB = selfFB.child("users").child(displayName);
        childFB.child("longitude").setValue(longitude);
    }


    public double getLatitude(String displayName) {
        User u = getUserDB(displayName);
        return u.getLatitude();
    }

    public void setLatitude(String displayName, double latitude){
        Firebase childFB = selfFB.child("users").child(displayName);
        childFB.child("latitude").setValue(latitude);
    }

    //this is used to set isAlive to false after the player has been assassinated
    public void kill(String displayName){
        Firebase childFB = selfFB.child("users").child(displayName);
        childFB.child("isAlive").setValue(false);
    }

    public boolean isAlive(String displayName){
        Firebase childFB = selfFB.child("users").child(displayName);
        System.out.println("isAlive key: " + childFB.child("isAlive").getKey());
        if(childFB.child("isAlive").getKey().equals("true"))
        {
            return true;
        }
        return false;
    }
}
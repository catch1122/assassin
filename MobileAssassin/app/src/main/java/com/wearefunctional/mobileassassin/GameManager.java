package com.wearefunctional.mobileassassin;
import java.util.*;

/**
 * Created by brianzhang on 11/12/15.
 */
public class GameManager {
    private Game mGame;
    private Vector<User> mPlayers;
    boolean mGameOver = false;

    public GameManager(Vector<User> players) {
        mPlayers = new Vector<User>();
        mGameOver = false;
        Vector<User> playerstmp = players; // make a copy
        while (!playerstmp.isEmpty()) {
            Random r = new Random();
            int index = r.nextInt(playerstmp.size());
            mPlayers.add(playerstmp.elementAt(index));
            playerstmp.remove(index);
        }
    }

    public void updateTargets(){
        for(int i=0; i<mPlayers.size(); i++) {
            //User assassin = getUserDB(mPLayers[i].getDisplayName());
            User assassin = mPlayers.elementAt(i);
            //upload back to database?
        }
    }

    User getTarget(int index) {
        if (index < 0 || index > mPlayers.size() - 1) {
            System.out.println("Out of bounds");
            return new User();

        }

        if (index == mPlayers.size() - 1) {
            return mPlayers.elementAt(0);
        } else {
            return mPlayers.elementAt(index + 1);
        }
    }

    User getTarget(String displayName) //target FOR displayName
    {

        for (int i = 0; i < mPlayers.size(); i++) {
            if (mPlayers.elementAt(i).equals(displayName)) {
                if (i == mPlayers.size() - 1) {
                    return mPlayers.firstElement();
                } else {
                    return mPlayers.elementAt(i + 1);
                }
            }
        }

        return new User();
    }

    boolean deletePlayer(int index) {
        if (index < 0 || index > mPlayers.size() - 1) {
            System.out.println("Out of bounds");
            return true;
        }

        mPlayers.remove(index);
        return false;
    }

    boolean deletePlayer(String displayName) {

        for (User u : mPlayers)
        {
            if(u.getDisplayName().equals(displayName))
            {
                mPlayers.remove(u);
                if(mPlayers.size() == 1){
                    mGameOver = true;
                }
                //add code to update target in database
                return true;
            }
        }
        return false;
    }

    boolean gameIsOver()
    {
        return mGameOver;
    }
}

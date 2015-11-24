package com.wearefunctional.mobileassassin;

/**
 * Created by brianzhang on 11/19/15.
 */
public class Game {
    private String name;
    private int numPlayers;
    private String[] playerNames;
    private boolean isPrivate = false;
    private String password;
    private String hostName;


    //default constructor
    public Game() { }

    public Game(String name, int numplayers)
    {
        this.name = name;
        this.numPlayers = numplayers;
        playerNames = new String[this.numPlayers];
        for(int i = 0; i < numplayers; i++)
        {
            playerNames[i] = "Player" + i;
        }
    }

    public Game(String name, int numplayers, boolean isPrivate, String password){
        this.name = name;
        this.numPlayers = numplayers;
        playerNames = new String[this.numPlayers];
        for(int i = 0; i < numplayers; i++)
        {
            playerNames[i] = "Player" + i;
        }
        this.isPrivate = isPrivate;
        this.password = password;
    }

    public Game(String name, int numplayers, boolean isPrivate, String password, String hostName){
        this.name = name;
        this.numPlayers = numplayers;
        playerNames = new String[this.numPlayers];
        playerNames[0] = hostName;
        for(int i = 1; i < numplayers; i++)
        {
            playerNames[i] = "Player" + i;
        }
        this.isPrivate = isPrivate;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public int getNumPlayers()
    {
        return numPlayers;
    }

    public String[] getPlayerNames()
    {
        return playerNames;
    }

    public void setPrivate(boolean access){isPrivate = access;}

    public void setPassword(String pass){password = pass;}





}

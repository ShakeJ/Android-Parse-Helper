package com.shakej.android.parse.helper;

import java.util.HashMap;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.shakej.android.parse.helper.listeners.ParseListener;
import com.shakej.android.parse.helper.managers.LoginManager;
import com.shakej.android.parse.helper.managers.QueryManager;

public class ParseHelper
{
  private volatile static ParseHelper instance;
  private Context context;
  
  //Managers
  private LoginManager loginManager;
  private QueryManager queryManager;
  
  
  public static ParseHelper getInstance(Context context)
  {
    if (instance == null)
    {
      synchronized (ParseHelper.class)
      {
        if (instance == null)
          instance = new ParseHelper(context);
      }
    }
    return instance;
  }
  
  
  private ParseHelper(Context context)
  {
    this.context = context;
    if (loginManager == null)
      loginManager = new LoginManager(context);
    if (queryManager == null)
      queryManager = new QueryManager(context);
  }
  
  
  public void clearListener()
  {
    loginManager.clearListeners();
    queryManager.clearListeners();
  }
  
  
  public void parseInit(String applicationId, String clientKey)
  {
    Parse.initialize(context, applicationId, clientKey);
    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    ParseACL.setDefaultACL(defaultACL, true);
    
    ParseInstallation.getCurrentInstallation().saveInBackground();
  }
  
  
  /**
   * searchDataObject. return ArrayList<ParseObject> in listener
   * 
   * @param listener
   * @param tableName
   * @param searchField
   * @param searchWord
   */
  public void searchDataObjects(final ParseListener listener, String tableName, String searchField, String searchWord)
  {
    queryManager.addListener(listener);
    queryManager.searchDataObjects(tableName, searchField, searchWord);
  }
  
  
  /**
   * Join parseUser with id, pw. Return user in listener
   * 
   * @param listener
   * @param id
   * @param pw
   */
  public void join(final ParseListener listener, String id, String pw)
  {
    loginManager.addListener(listener);
    loginManager.join(id, pw);
  }
  
  
  /**
   * Login parseUser. return ParseUser in listener
   * 
   * @param listener
   * @param id
   * @param pw
   */
  public void login(final ParseListener listener, String id, final String pw)
  {
    loginManager.addListener(listener);
    loginManager.login(id, pw);
  }
  
  
  public void requestUserWithDeviceToken(ParseListener listener, String deviceToken)
  {
    loginManager.addListener(listener);
    loginManager.getUserWithDeviceToken(deviceToken);
  }
  
  
  public void setDataToUserData(ParseListener listener, String userName, HashMap<Object, Object> datas)
  {
    loginManager.addListener(listener);
    loginManager.setDataToUserData(userName, datas);
  }
  
  
  public void registerPushService(String channel)
  {
    ParsePush.subscribeInBackground(channel);
  }
  
  
  public void unregisterPushService(String channel)
  {
    ParsePush.unsubscribeInBackground(channel);
  }
  
}

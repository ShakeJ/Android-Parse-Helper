package com.shakej.android.parse.helper.managers;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shakej.android.parse.helper.listeners.ParseListener;

public class LoginManager
{
  private Context context;
  private ParseListener listener;
  
  
  public LoginManager(Context context)
  {
    this.context = context;
  }
  
  
  public void clearListeners()
  {
  }
  
  
  public void addListener(ParseListener listener)
  {
    this.listener = listener;
  }
  
  
  public void login(String id, String pw)
  {
    ParseUser.logInInBackground(id, pw, new LogInCallback()
    {
      public void done(ParseUser user, ParseException e)
      {
        if (e != null)
          Log.w("WARN", "Login Error : " + e.getMessage());
        
        if (listener != null)
          listener.onRequestEnd(user);
      }
    });
  }
  
  
  public void loginWithDeviceToken(String deviceToken)
  {
    
  }
  
  
  public void getUserWithDeviceToken(String deviceToken)
  {
    ParseQuery<ParseUser> query = ParseUser.getQuery();
    query.whereEqualTo("deviceToken", deviceToken);
    query.findInBackground(new FindCallback<ParseUser>()
    {
      public void done(List<ParseUser> objects, ParseException e)
      {
        ParseUser user = null;
        if (e == null)
        {
          if (objects.size() > 0)
          {
            user = objects.get(0);
            Log.w("WARN", "CurrentUser id : " + user.getObjectId());
            Log.w("WARN", "CurrentUser name : " + user.getUsername());
          }
        }
        else
          Log.w("WARN", "getUserWithDeviceToken error : " + e.getMessage());
        
        if (listener != null)
          listener.onRequestEnd(user);
      }
    });
  }
  
  
  public void join(String id, String pw)
  {
    final ParseUser user = new ParseUser();
    user.setUsername(id);
    user.setPassword(pw);
    
    user.signUpInBackground(new SignUpCallback()
    {
      public void done(ParseException e)
      {
        if (e == null)
        {
          if (listener != null)
            listener.onRequestEnd(user);
        }
        else if (listener != null)
          listener.onRequestEnd(null);
      }
    });
  }
  
  
  public void setDataToUserData(String userName, final HashMap<Object, Object> datas)
  {
    ParseUser.logInInBackground(userName, "", new LogInCallback()
    {
      public void done(ParseUser user, ParseException e)
      {
        if (user != null)
        {
          Log.w("WARN", "login success : " + user.getObjectId());
          for (Object key : datas.keySet())
            user.put((String) key, datas.get(key));
          
          user.saveInBackground(new SaveCallback()
          {
            @Override
            public void done(ParseException arg0)
            {
              Log.w("WARN", "setData to user done");
              if (listener != null)
                listener.onRequestEnd(null);
            }
          });
        }
        else
          Log.w("WARN", "setDataToUserData Error :" + e);
      }
    });
  }
}

package com.shakej.android.parse.helper.managers;

import java.util.ArrayList;

import android.content.Context;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shakej.android.parse.helper.listeners.ParseListener;

public class LoginManager
{
  private Context context;
  private ArrayList<ParseListener> listeners = new ArrayList<ParseListener>();
  
  
  public LoginManager(Context context)
  {
    this.context = context;
  }
  
  
  public void addListener(ParseListener listener)
  {
    listeners.add(listener);
  }
  
  
  public void login(String id, String pw)
  {
    ParseUser.logInInBackground(id, pw, new LogInCallback()
    {
      public void done(ParseUser user, ParseException e)
      {
        for (ParseListener listener : listeners)
        {
          listener.onRequestEnd(user);
        }
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
          for (ParseListener listener : listeners)
          {
            listener.onRequestEnd(user);
          }
        }
        else
          for (ParseListener listener : listeners)
          {
            listener.onRequestEnd(null);
          }
      }
    });
  }
}

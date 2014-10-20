package com.shakej.android.parse.helper.managers;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shakej.android.parse.helper.listeners.ParseListener;

public class QueryManager
{
  @SuppressWarnings("unused")
  private Context context;
  private ParseListener listener;
  
  
  public QueryManager(Context context)
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
  
  
  public void searchDataObjects(String tableName, String searchField, String searchWord)
  {
    ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
    query.whereEqualTo(searchField, searchWord);
    query.findInBackground(new FindCallback<ParseObject>()
    {
      public void done(List<ParseObject> searchObjects, ParseException e)
      {
        if (e == null)
        {
          Log.w("Parse", "Search Parse Obejcts Count : " + searchObjects.size());
        }
        else
          Log.w("Parse", "Error: " + e.getMessage());
        
        if (listener != null)
          listener.onRequestEnd(searchObjects);
      }
    });
  }
  
}

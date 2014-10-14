package com.shakej.android.parse.helper.managers;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shakej.android.parse.helper.listeners.ParseListener;

import android.content.Context;
import android.util.Log;

public class QueryManager
{
  private Context context;
  private ArrayList<ParseListener> listeners = new ArrayList<ParseListener>();
  
  
  public QueryManager(Context context)
  {
    this.context = context;
  }
  
  
  public void addListener(ParseListener listener)
  {
    listeners.add(listener);
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
          if (searchObjects.size() > 0)
            Log.w("Parse", "Search Parse Obejcts Count : " + searchObjects.size());
        }
        else
          Log.w("Parse", "Error: " + e.getMessage());
        
        for (ParseListener listener : listeners)
        {
          listener.onRequestEnd(searchObjects);
        }
      }
    });
  }
}

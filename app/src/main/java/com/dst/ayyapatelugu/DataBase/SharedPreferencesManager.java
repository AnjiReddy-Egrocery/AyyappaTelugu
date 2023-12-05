package com.dst.ayyapatelugu.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

import com.dst.ayyapatelugu.Model.BooksModelResult;
import com.dst.ayyapatelugu.Model.TempleMapDataResponse;
import com.dst.ayyapatelugu.Model.YatraList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "AyyappaBooksPrefs";
    private static final String KEY_BOOKS = "bookList";

    private static final String PREFs_NAME = "ayyappa_prefs";
    private static final String YATRA_LIST_KEY = "yatra_list_key";



    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFs_NAME, Context.MODE_PRIVATE);
    }
    public static void saveBookList(Context context, List<BooksModelResult> bookList) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(bookList);
        editor.putString(KEY_BOOKS, json);
        editor.apply();
    }

    public static List<BooksModelResult> getBookList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_BOOKS, null);
        Type type = new TypeToken<List<BooksModelResult>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void saveYatraList(Context context, YatraList yatraList) {
        String yatraListJson = new Gson().toJson(yatraList);
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(YATRA_LIST_KEY, yatraListJson);
        editor.apply();
    }

    public static YatraList getYatraList(Context context) {
        String savedYatraListJson = getSharedPreferences(context).getString(YATRA_LIST_KEY, "");
        if (!savedYatraListJson.isEmpty()) {
            return new Gson().fromJson(savedYatraListJson, YatraList.class);
        }
        return null;
    }

}

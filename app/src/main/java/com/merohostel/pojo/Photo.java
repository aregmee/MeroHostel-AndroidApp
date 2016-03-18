package com.merohostel.pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asimsangram on 2/5/16.
 */
public class Photo {

    private int id;
    private String url;
    private boolean main;

    public Photo(JSONObject photoJSONObject) throws Exception{

        id = photoJSONObject.getInt("id");
        url = photoJSONObject.getString("url").replaceAll("/s\\d{3}", "/s360");                     //decreasing quality of image
        main = photoJSONObject.getBoolean("main");                                                  //this isn't the right way but
    }                                                                                               //will make it efficient in the
                                                                                                    //future
    public Photo(int id, String url, boolean main){

        this.id = id;
        this.url = url;
        this.main = main;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public boolean isMain(){

        return main;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "url='" + url + '\'' +
                ", main=" + main +
                '}';
    }
}

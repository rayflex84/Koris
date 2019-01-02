package com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by macbook on 2/09/17.
 */

public class GsonDateFormatter implements JsonDeserializer<Date> {


    @Override
    public synchronized java.util.Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            return dateFormat.parse(jsonElement.getAsString());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }




    }
}

package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class EMailConverters {

    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String mailStringArrayToString(final String[] strings){
        return new Gson().toJson(strings);
//        final StringBuilder stb = new StringBuilder();
//        if(strings == null)
//            return null;
//        for (final String s: strings) {
//            stb.append(s).append('\n');
//        }
//        stb.deleteCharAt(stb.length()-1);
//        return stb.toString();
    }

    @TypeConverter
    public static String[] StringToMailstring(final String string){
        if(string == null)
            return null;

        Type arrayType = new TypeToken<String[]>() {}.getType();
        return new Gson().fromJson(string, arrayType);
//        return string.split("\\n");
    }

}

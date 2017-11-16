package de.haw_landshut.hawmobile;

public class Credentials {

    private static String username;
    private static String password;
    private static Fakultaet fakultaet;

    public static void setCredentials(String username, String password, Fakultaet fakultaet){
        Credentials.username = username;
        Credentials.password = password;
        Credentials.fakultaet = fakultaet;
    }

    public static String getUsername(){
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static Fakultaet getFakultaet(){
        return fakultaet;
    }
}

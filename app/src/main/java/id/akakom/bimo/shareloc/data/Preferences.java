package id.akakom.bimo.shareloc.data;

import id.akakom.bimo.shareloc.BuildConfig;

public class Preferences {
    private static String package_name = BuildConfig.APPLICATION_ID;
    public static final String USER_ACCESS                  = package_name+"_user_access";
    public static final String USER_LOCATION                  = package_name+"_user_location";
    public static final String SHARED_STATUS                  = package_name+"_shared_status";
}

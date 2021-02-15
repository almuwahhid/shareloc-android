package id.akakom.bimo.shareloc.data;


import id.akakom.bimo.shareloc.BuildConfig;

public class EndPoints {
    public static String a = BuildConfig.base_url;

    private static String b = BuildConfig.api;

    private static String c = a+b;

    public static String stringLogin() {
        return c + BuildConfig.login;
    }

    public static String stringRegister() {
        return c + BuildConfig.register;
    }

    public static String stringAllRiwayat() {
        return c + BuildConfig.allRiwayat;
    }

    public static String stringAllRiwayatMember() {
        return c + BuildConfig.allRiwayatMember;
    }

    public static String stringDetailShareloc() {
        return c + BuildConfig.detailShareloc;
    }

    public static String stringLocationsShareloc() {
        return c + BuildConfig.locationsShareloc;
    }

    public static String stringMemberShareloc() {
        return c + BuildConfig.memberShareloc;
    }

    public static String stringShare() {
        return c + BuildConfig.share;
    }

    public static String stringAddPartisipan() {
        return c + BuildConfig.addPartisipan;
    }

    public static String stringStopShare() {
        return c + BuildConfig.stopShare;
    }

    public static String stringAddLocation() {
        return c + BuildConfig.addLocation;
    }

}

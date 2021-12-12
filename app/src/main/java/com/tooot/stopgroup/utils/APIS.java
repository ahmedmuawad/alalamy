package com.tooot.stopgroup.utils;

import com.ciyashop.library.apicall.URLS;

public class APIS {

    //TODO:Copy and Paste URL and Key Below from Admin Panel.
    //TODO:Copy and Paste URL and Key Below from Admin Panel.

    public final String APP_URL = "https://toootstore.com/";
    public final String WOO_MAIN_URL = APP_URL + "wp-json/wc/v2/";
    public final String MAIN_URL = APP_URL + "wp-json/pgs-woo-api/v1/";

    public static final String CONSUMERKEY = "kUlaGIuhTOoR";
    public static final String CONSUMERSECRET = "VNHiVQ3oyM2h0zO5OcOGakPJxNPJa0YD4MS4EzlOByhPSUFm";
    public static final String OAUTH_TOKEN = "3d8h5kuVKP9TpARBCMZjsAb9";
    public static final String OAUTH_TOKEN_SECRET = "E9mxnyz8vnjHEA8HzT2wiVnU3MccVy3RTJuMzcXx4GQJR5a3";

    public static final String WOOCONSUMERKEY = "ck_82c7dbd33862bf1f8a9a93702197e379427bd74a";
    public static final String WOOCONSUMERSECRET = "cs_f101f0e071e1759d4590c974ef16fa63eaad93dd";
    public static final String version="4.1.0";
    public static final String purchasekey="b50852cd-97f6-40b2-a9cd-5e07b884dfff";
    public APIS() {
        URLS.APP_URL = APP_URL;
        URLS.NATIVE_API = APP_URL + "wp-json/wc/v3/";
        URLS.WOO_MAIN_URL = WOO_MAIN_URL;
        URLS.MAIN_URL = MAIN_URL;
        URLS.version = version;
        URLS.CONSUMERKEY = CONSUMERKEY;
        URLS.CONSUMERSECRET = CONSUMERSECRET;
        URLS.OAUTH_TOKEN = OAUTH_TOKEN;
        URLS.OAUTH_TOKEN_SECRET = OAUTH_TOKEN_SECRET;
        URLS.WOOCONSUMERKEY = WOOCONSUMERKEY;
        URLS.WOOCONSUMERSECRET = WOOCONSUMERSECRET;
        URLS.PURCHASE_KEY=purchasekey;
    }
}
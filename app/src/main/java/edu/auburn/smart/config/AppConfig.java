package edu.auburn.smart.config;

/**
 * Author: Gary
 * Time: 17/1/10
 */

public class AppConfig {
    public static final int count_per_page = 10;
    public static final String COLLECT_ID = "collect_id";
    public final static String[] categories = {"art","biology","chemistry","education","geography","history","math","physics","psychology","responsibility","technology"};
    /**
     * config.xml record whether it is the first to login
     */
    public final static String IS_FIRST_ENTER = "is_first";


    public final static String USER = "user";
    public final static String TOKEN = "token";

    public final static String SERVER_IP = "http://172.17.82.238:8080/iw2kserver/";
    public final static String API_CATEGORY = SERVER_IP+"CategoryServlet";
    public final static String API_ARTICLE = SERVER_IP+"ArticleServlet";
    public final static String API_SEARCH = SERVER_IP+"SearchServlet";
    public final static String API_SAVED = SERVER_IP+"CollectServlet";
    public final static String API_REGISTER = SERVER_IP+"RegisterServlet";
    public final static String API_LOGIN = SERVER_IP+"LoginServlet";
    public static String createUrlWithStartAndCid(int start, int cId){
        return API_ARTICLE + "?start="+start+"&count="+count_per_page+"&categoryid="+cId;
    }

    /**
     * test method Foryou
     * @param start
     * @return
     */
    public static String createUrlWithStart(int start){
        return API_ARTICLE + "?start="+start+"&count="+count_per_page+"&categoryid=9";
    }
    public static String createUrlWithKeywords(int start, String kw){
        return API_SEARCH + "?start="+start+"&count="+count_per_page+"&kw="+kw;
    }

    public static String createUrlBySavedIds(String ids){
        return API_SAVED + "?ids="+ids;
    }
}

package edu.auburn.smart.utils;

import android.content.Context;

/**
 * 缓存工具类：标准做法要修改为本地文件存储，用MD5(url)为文件名
 * Author: Gary
 * Time: 17/1/13
 */

public class CacheUtils {
    /**
     * K-V对保存url和json
     * @param url
     * @param json
     * @param ctx
     */
    public static void setCache(String url, String json, Context ctx){
        PrefUtils.setString(ctx, url, json);
    }

    /**
     * 通过url获得缓存json
     * @param url
     * @param ctx
     * @return
     */
    public static String getCache(String url, Context ctx){
        return PrefUtils.getString(ctx, url, null);
    }
}

package cn.edu.pku.liangxiaochong.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/*
在访问网络资源时，先检测网络状态，如果网络不可用，提示用户先打开网络
 */
public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if(networkInfo == null) {
            return NETWORK_NONE;
        }

        int nType = networkInfo.getType();
        if(nType == ConnectivityManager.TYPE_MOBILE) {
            return NETWORK_MOBILE;
        } else if(nType == ConnectivityManager.TYPE_WIFI) {
            return NETWORK_WIFI;
        }
         return NETWORK_NONE;






    }
}

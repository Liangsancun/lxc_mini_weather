package cn.edu.pku.liangxiaochong.miniweather;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import java.util.List;

import cn.edu.pku.liangxiaochong.app.MyApplication;
import cn.edu.pku.liangxiaochong.bean.City;

public class MyLocationListener extends BDAbstractLocationListener{//实现BDAbstractLocationListener接口
    public String city;
    public String cityCode;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //BDLocation为定位结果信息类。
        String addr= bdLocation.getAddrStr();//获取详细的地址信息
        String country= bdLocation.getCountry();//
        String province= bdLocation.getProvince();
        String city= bdLocation.getCity();
        String district = bdLocation.getDistrict();//获取区县
        String street= bdLocation.getStreet();//获取街道
        city=city.replace("市", "");//返回格式是北京市，要去掉市

        List<City> myCityList;
        MyApplication myApplication = MyApplication.getInstance();//getInstance()是静态方法返回一个MyApplication类
                                                                    //调用静态成员。直接用 类名.静态成员名 即可。
        myCityList=myApplication.getCityList();

        for(City i:myCityList) {
            if(i.getCity().equals(city)) {
                cityCode=i.getNumber();
                Log.d("location_code", cityCode);
                break;
            }
        }

        //int errorCode = location.getLocType();
        Log.i("tag", "location.getLocType()="+ bdLocation.getLocType());
        //获取定位类型、定位错误返回码，具体信息可参考BDLocation类中的说明


    }
}

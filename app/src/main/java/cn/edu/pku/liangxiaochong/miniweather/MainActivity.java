package cn.edu.pku.liangxiaochong.miniweather;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.liangxiaochong.adapter.MyPagerAdapter;
import cn.edu.pku.liangxiaochong.bean.FutureThreeWeather;

import cn.edu.pku.liangxiaochong.util.NetUtil;
import cn.edu.pku.liangxiaochong.bean.TodayWeather;

public class MainActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private static final int UPDATE_TODAY_WEATHER = 1;
    private static final int DB=2;
    private ImageView myUpdateBtn;
    private ImageView myCitySelectBtn;//为选择城市ImageView添加OnClick事件
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;
    private String sendedCode = "101010100";
    private ProgressBar myUpdateProgressBar;//刷新按钮动画
    private ImageView myLocation;//我的定位

    private ViewPager myViewPager;//滑动视图
    private MyPagerAdapter myPagerAdapter;//滑动适配器
    //private List<FutureThreeWeather> myfuture_weather_list;//存储未来每天天气的列表。一个下标的位置有一天全部的天气信息。

    private List<View> list_paper_view;

    //最近六天的天气所用到的控件
    private TextView date11,date0,date1, date2, date3,date4,type11,type0,type1,type2,type3,type4,fengli11,fengli0,fengli1,fengli2,fengli3,fengli4,temper11,temper0,temper1,temper2,temper3,temper4;

    //定位
    public LocationClient myLocationClient=null;
    private MyLocationListener myLocationListener = new MyLocationListener();

    private ImageView[] imgdots;//里面放两个ImageView类型的圆点
    private int[] dotids={R.id.dot1, R.id.dot2};//里面是圆点的id

    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //根据收到的消息的what类型处理
            //更新今日天气
            switch(msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather)msg.obj);

                    //刷新结束，静态刷新图片显示，动态刷新图片隐藏且不占用屏幕
                    myUpdateBtn.setVisibility(View.VISIBLE);
                    myUpdateProgressBar.setVisibility(View.GONE);
                    break;
                 default:
                    break;
            }
        }
    };
    private Handler BDHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("响应", "收到线程给的结果");
            switch (msg.what) {
                case DB:
                    if(msg.obj != null) {
                        if(NetUtil.getNetworkState(MainActivity.this)!=NetUtil.NETWORK_NONE) {
                            Log.d("myWeather", "网络ok");
                            queryWeatherCode(myLocationListener.cityCode);
                        }else {
                            Log.d("myWeather", "网络挂了");
                            Toast.makeText(MainActivity.this, "网络挂了", Toast.LENGTH_SHORT).show();
                        }
                    }
                    myLocationListener.cityCode=null;//以免换地方之后的citycode不变。
                    break;
                default:
                    break;
            }
        }
    };



    @Override //ActivityT
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        myUpdateProgressBar = (ProgressBar)findViewById(R.id.title_update_progress);
        myUpdateBtn = (ImageView)findViewById(R.id.title_update_static);
        myUpdateBtn.setOnClickListener(this);//this指的是继承了onClickListener的MainActivity

        if(NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("myWeather", "网络ok");
            Toast.makeText(MainActivity.this, "网络是ok的", Toast.LENGTH_LONG).show();

        } else {
            Log.d("myWeather", "网络故障");
            Toast.makeText(MainActivity.this, "网络有故障", Toast.LENGTH_LONG).show();
        }
        //选择城市
        myCitySelectBtn = (ImageView)findViewById(R.id.title_city_manager);
        myCitySelectBtn.setOnClickListener(this);

        initPagerView();//初始化两个滑动视图
        initDots();//初始化圆点
        initView();//初始化界面控件



        myLocation=(ImageView)findViewById(R.id.title_location);
        myLocation.setOnClickListener(this);



    }

    public void initDots() {
        imgdots=new ImageView[list_paper_view.size()];//有几个滑动页面
        for(int i=0; i<imgdots.length; i++) {
            imgdots[i]=(ImageView)findViewById(dotids[i]);

        }
    }

    void initPagerView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        list_paper_view=new ArrayList<View>();
        list_paper_view.add(inflater.inflate(R.layout.page, null));
        list_paper_view.add(inflater.inflate(R.layout.page2, null));

        myPagerAdapter=new MyPagerAdapter(MainActivity.this, list_paper_view);
        myViewPager =(ViewPager)findViewById(R.id.view_pager);

        myViewPager.setAdapter(myPagerAdapter);

        myViewPager.setOnPageChangeListener(this);//设置页面滑动监听，来改变小圆点
    }

    void initView() {
        city_name_Tv = (TextView)findViewById(R.id.title_city_name);
        cityTv = (TextView)findViewById(R.id.weather_today_city);
        timeTv = (TextView)findViewById(R.id.weather_today_time);
        humidityTv = (TextView)findViewById(R.id.weather_today_humidity);
        weekTv = (TextView)findViewById(R.id.weather_today_news_words_week);
        pmDataTv = (TextView)findViewById(R.id.pm_content_word_data);
        pmQualityTv = (TextView)findViewById(R.id.pm_quality);
        pmImg = (ImageView)findViewById(R.id.pm_content_image);
        temperatureTv = (TextView)findViewById(R.id.weather_today_news_words_temper);
        climateTv = (TextView)findViewById(R.id.weather_today_news_words_climate);
        windTv = (TextView)findViewById(R.id.weather_today_news_words_wind);
        weatherImg = (ImageView)findViewById(R.id.weather_today_news_image);


        date11=list_paper_view.get(0).findViewById(R.id.future_date1);
        date0=list_paper_view.get(0).findViewById(R.id.future_date2);
        date1=list_paper_view.get(0).findViewById(R.id.future_date3);
        date2=list_paper_view.get(1).findViewById(R.id.future_date1);
        date3=list_paper_view.get(1).findViewById(R.id.future_date2);
        date4=list_paper_view.get(1).findViewById(R.id.future_date3);

        fengli11=list_paper_view.get(0).findViewById(R.id.future_fengli1);
        fengli0=list_paper_view.get(0).findViewById(R.id.future_fengli2);
        fengli1=list_paper_view.get(0).findViewById(R.id.future_fengli3);
        fengli2=list_paper_view.get(1).findViewById(R.id.future_fengli1);
        fengli3=list_paper_view.get(1).findViewById(R.id.future_fengli2);
        fengli4=list_paper_view.get(1).findViewById(R.id.future_fengli3);

        type11=list_paper_view.get(0).findViewById(R.id.future_type1);
        type0=list_paper_view.get(0).findViewById(R.id.future_type2);
        type1=list_paper_view.get(0).findViewById(R.id.future_type3);
        type2=list_paper_view.get(1).findViewById(R.id.future_type1);
        type3=list_paper_view.get(1).findViewById(R.id.future_type2);
        type4=list_paper_view.get(1).findViewById(R.id.future_type3);

        temper11=list_paper_view.get(0).findViewById(R.id.future_temper1);
        temper0=list_paper_view.get(0).findViewById(R.id.future_temper2);
        temper1=list_paper_view.get(0).findViewById(R.id.future_temper3);
        temper2=list_paper_view.get(1).findViewById(R.id.future_temper1);
        temper3=list_paper_view.get(1).findViewById(R.id.future_temper2);
        temper4=list_paper_view.get(1).findViewById(R.id.future_temper3);



        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");

        date11.setText("N/A");
        type11.setText("N/A");
        temper11.setText("N/A");
        fengli11.setText("N/A");

        date0.setText("N/A");
        type0.setText("N/A");
        temper0.setText("N/A");
        fengli0.setText("N/A");

        date1.setText("N/A");
        type1.setText("N/A");
        temper1.setText("N/A");
        fengli1.setText("N/A");

        date2.setText("N/A");
        type2.setText("N/A");
        temper2.setText("N/A");
        fengli2.setText("N/A");

        date3.setText("N/A");
        type3.setText("N/A");
        temper3.setText("N/A");
        fengli3.setText("N/A");

        date4.setText("N/A");
        type4.setText("N/A");
        temper4.setText("N/A");
        fengli4.setText("N/A");


    }

    private void initLocation() {
        //定位
        myLocationClient=new LocationClient(getApplicationContext());//声明LocationClient类
        myLocationClient.registerLocationListener(myLocationListener);//注册监听函数


        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，定位模式，默认高精度
        option.setCoorType("bd09ll");//可选，返回的定位结果坐标系，设为百度，默认gcj02，
        option.setScanSpan(1000);//连续定位请求的间隔，设为1000ms，默认0，为仅定位一次。
        option.setIsNeedAddress(true);//需要地址信息
        option.setOpenGps(true);//开启GPS定位，默认不开启
        option.setIsNeedLocationDescribe(true);//需要地址描述
        option.setIgnoreKillProcess(true);//定位sdk内部是一个service，并放到了独立进程，在stop的时候，不杀死该进程，默认不杀死
        myLocationClient.setLocOption(option);//给myLocationClient配置定位参数

        myLocationClient.start();//开启定位客户端


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.title_city_manager) {
            Intent i = new Intent(this, SelectCity.class);
            //startActivity(i);
            //startActivityForResult(Intent intent, int requestCode) 方法打开新的 Activity，
            // 我们需要为 startActivityForResult() 方法传入一个请求码 (第二个参数)。
            // 请求码的值是根据业务需要由自已设定，用于标识请求来源，哪个按钮发出的请求
            startActivityForResult(i,1);
        } if(view.getId()==R.id.title_location) {
            Toast.makeText(this, "点击图标响应没问题", Toast.LENGTH_SHORT).show();
            initLocation();//配置定位客户端相关信息，并开启
            if(myLocationClient.isStarted()) {
                myLocationClient.stop();
            }
            myLocationClient.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(myLocationListener.cityCode==null) {
                            Thread.sleep(1000);
                        }
                        Message msg=new Message();
                        msg.what=DB;
                        msg.obj=myLocationListener.cityCode;
                        BDHandler.sendMessage(msg);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();


        }if(view.getId() == R.id.title_update_static) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city", sendedCode);
            Log.d("myWeather", cityCode);

            if(NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络ok");
                //图片转起来
                myUpdateBtn.setVisibility(View.GONE);//静态刷新图片控件不占屏幕，不可见
                myUpdateProgressBar.setVisibility(View.VISIBLE);//显示动态刷新图片

                queryWeatherCode(cityCode);//获取网络数据，以城市代码，连接天气预报网络，查询该城市的情况
            }else {
                Log.d("myWeather", "网路故障");
                Toast.makeText(MainActivity.this, "网络故障", Toast.LENGTH_LONG).show();
            }

        }


    }


    /*
    @param cityCode
     */
    //以城市代码，连接天气预报网站，查询该城市的天气情况
    public void queryWeatherCode(String cityCode) {

        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                //实例化TodayWeather
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);

                    //在获得网络数据后，调用解析函数
                    todayWeather = parseXML(responseStr);
                    if(todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());

                        /*
                        通过消息机制，将解析的天气对象，通过消息发送给主线程
                        主线程收到消息数据后，调用updateTodayWeather函数，
                        更新UI界面上的数据
                         */
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        myHandler.sendMessage(msg);
                    }

                } catch(Exception e) {
                    e.printStackTrace();

                } finally {
                    if(con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();


    }


    @Override
    //该activity关闭后，跳转到其他activity，在接收到返回数据时，会自动调用该方法
    //一般和startActivityForResult配套使用，场景：主activity发出请求跳转到新activity，并期望返回数据
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        if(requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode = i.getStringExtra("cityCode");
            sendedCode = newCityCode;//用全局变量保存返回的所挑选的城市代码
            Log.d("myWeather", "选择的城市代码为" + newCityCode);

            if(NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络ok");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络故障");
                Toast.makeText(MainActivity.this, "网络故障", Toast.LENGTH_LONG).show();


            }

        }
    }

    //解析函数，解析出城市名称，和已经更新时间信息
    private  TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;//风向
        int fengliCount = 0;//风力
        int dateCount = 0;//哪天
        int highCount = 0;//最高温
        int lowCount = 0;//
        int typeCount = 0;//天气类型

        //引入Count=0，是为了解析出未来的天气情况

        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                     //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();//刚开始为null，现在实例化了不是null了
                        }
                        if(todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                                //Log.d("myWeather", "city:  " + xmlPullParser.getText());

                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                                //Log.d("myWeather", "updatetime:  " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                                //Log.d("myWeather", "shidu:  " + xmlPullParser.getText());

                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                                //Log.d("myWeather", "wendu:  " + xmlPullParser.getText());

                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                                //Log.d("myWeather", "pm25:  " + xmlPullParser.getText());

                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                               // Log.d("myWeather", "quality:  " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFx(xmlPullParser.getText());
                                //Log.d("myWeather", "fengxiang:  " + xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFx1(xmlPullParser.getText());
                                //Log.d("myWeather", "fengxiang:  " + xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFx2(xmlPullParser.getText());
                                //Log.d("myWeather", "fengxiang:  " + xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFx3(xmlPullParser.getText());
                                //Log.d("myWeather", "fengxiang:  " + xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFx4(xmlPullParser.getText());
                                //Log.d("myWeather", "fengxiang:  " + xmlPullParser.getText());
                                fengxiangCount++;
                            } else if(xmlPullParser.getName().equals("fx_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFx11(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                //Log.d("myWeather", "fengli:  " + xmlPullParser.getText());
                                fengliCount++;
                            } else if(xmlPullParser.getName().equals("fengli") && fengliCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli1(xmlPullParser.getText());
                                fengliCount++;
                            } else if(xmlPullParser.getName().equals("fengli") && fengliCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli2(xmlPullParser.getText());
                                fengliCount++;
                            } else if(xmlPullParser.getName().equals("fengli") && fengliCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli3(xmlPullParser.getText());
                                fengliCount++;
                            } else if(xmlPullParser.getName().equals("fengli") && fengliCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli4(xmlPullParser.getText());
                                fengliCount++;
                            } else if(xmlPullParser.getName().equals("fl_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli11(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                //Log.d("myWeather", "date:  " + xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate1(xmlPullParser.getText());
                                //Log.d("myWeather", "date:  " + xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate2(xmlPullParser.getText());
                                //Log.d("myWeather", "date:  " + xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate3(xmlPullParser.getText());
                                //Log.d("myWeather", "date:  " + xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate4(xmlPullParser.getText());
                                //Log.d("myWeather", "date:  " + xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate11(xmlPullParser.getText());


                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "high:  " + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh1(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "high:  " + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh2(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "high:  " + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh3(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "high:  " + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh4(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "high:  " + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh11(xmlPullParser.getText().substring(2).trim());

                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "low:  " + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow1(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "low:  " + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow2(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "low:  " + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow3(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "low:  " + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow4(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "low:  " + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow11(xmlPullParser.getText().substring(2).trim());
                                //Log.d("myWeather", "low:  " + xmlPullParser.getText());

                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                //Log.d("myWeather", "type:  " + xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType1(xmlPullParser.getText());
                                //Log.d("myWeather", "type:  " + xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType2(xmlPullParser.getText());
                                //Log.d("myWeather", "type:  " + xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType3(xmlPullParser.getText());
                                //Log.d("myWeather", "type:  " + xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType4(xmlPullParser.getText());
                                //Log.d("myWeather", "type:  " + xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType11(xmlPullParser.getText());
                                //Log.d("myWeather", "type:  " + xmlPullParser.getText());

                            }
                        }

                        break;

                     //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;

                }

                //进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }

        } catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return todayWeather;
    }

    //更新今日天气函数
    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());

        timeTv.setText(todayWeather.getUpdatetime());
        humidityTv.setText("湿度： " + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText(todayWeather.getFx()+ todayWeather.getFengli());
        Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();

        date11.setText(todayWeather.getDate11());
        date0.setText(todayWeather.getDate());
        date1.setText(todayWeather.getDate1());
        date2.setText(todayWeather.getDate2());
        date3.setText(todayWeather.getDate3());
        date4.setText(todayWeather.getDate4());

        fengli11.setText(todayWeather.getFx11()+todayWeather.getFengli11());
        fengli0.setText(todayWeather.getFx()+todayWeather.getFengli());
        fengli1.setText(todayWeather.getFx1()+todayWeather.getFengli1());
        fengli2.setText(todayWeather.getFx2()+todayWeather.getFengli2());
        fengli3.setText(todayWeather.getFx3()+todayWeather.getFengli3());
        fengli4.setText(todayWeather.getFx4()+todayWeather.getFengli4());

        type11.setText(todayWeather.getType11());
        type0.setText(todayWeather.getType());
        type1.setText(todayWeather.getType1());
        type2.setText(todayWeather.getType2());
        type3.setText(todayWeather.getType3());
        type4.setText(todayWeather.getType4());


        temper11.setText(todayWeather.getLow11()+"~"+todayWeather.getHigh11());
        temper0.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());
        temper1.setText(todayWeather.getLow1()+"~"+todayWeather.getHigh1());
        temper2.setText(todayWeather.getLow2()+"~"+todayWeather.getHigh2());
        temper3.setText(todayWeather.getLow3()+"~"+todayWeather.getHigh3());
        temper4.setText(todayWeather.getLow4()+"~"+todayWeather.getHigh4());
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        for(int a=0; a<imgdots.length; a++) {
            if(a==i) {
                imgdots[a].setImageResource(R.drawable.page_indicator_focused);
            }else {
                imgdots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

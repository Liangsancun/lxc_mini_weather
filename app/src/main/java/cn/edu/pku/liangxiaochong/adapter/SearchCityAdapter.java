package cn.edu.pku.liangxiaochong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Filter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.liangxiaochong.bean.City;
import cn.edu.pku.liangxiaochong.miniweather.R;

public class SearchCityAdapter extends BaseAdapter{

    private Context myContext;//上下文环境，即是所在的类
    private List<City> myCityList;//所有城市列表
    private List<City> mySearchCityList;//搜索到的城市列表
    private LayoutInflater myInflater;//给充哪种布局，找layout下的xml布局文件，并实例化

    //构造函数
    public SearchCityAdapter(Context context, List<City> cityList) {
        myContext = context;
        myCityList = cityList;
        mySearchCityList = new ArrayList<City>();//City型动态列表（数组）
        myInflater = LayoutInflater.from(myContext);//从myContext获取一个布局加载器，用来管理myContext的布局
    }
    @Override
    //返回搜索到的城市数量
    public int getCount() {
        return mySearchCityList.size();
    }
    @Override
    //根据下标返回一个item（listView中的一行）子项
    public Object getItem(int position) {
        return mySearchCityList.get(position);

    }
    @Override
    //根据下标返回一个item的id（下标，你没有看错，就是根据下标返回下标）
    public long getItemId(int position) {
        return position;
    }

    @Override
    //position显示屏中，要出现的一行搜索结果（一个item.xml形成的视图），其在搜索结果中的位置
    //convertView，The old view to reuse, 随着滑动，屏幕中消失的一行视图被回收到了缓存, convertView是从缓存中取出来的
    //parent,里面有每一行item视图，ListView要显示新的一行视图时，从里面取
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取搜索结果的每一行（item)的显示内容
        if(convertView == null) {
            //缓存中没有item视图时，
            //搜索结果第一次在屏幕显示，还没有滑动屏幕，如果有n行，则n次convertView=null
            //下面这一行运行了n次
            convertView = myInflater.inflate(R.layout.item, null);//填充item的布局文件（即是ListView一行的布局），将其转化为view

        }
        //在item.xml转化成的view中，找控件
        TextView provinceTv = (TextView)convertView.findViewById(R.id.item_provincename);
        TextView cityTv = (TextView)convertView.findViewById(R.id.item_cityname);

        provinceTv.setText(mySearchCityList.get(position).getProvince());
        cityTv.setText(mySearchCityList.get(position).getCity());
        return convertView;//把缓存中item视图（也即是convertView），改一改，更新控件里的内容，
        //此时的convertView 就是下一行即将在屏幕中出现的item视图
    }

    public Filter getFilter() {
        Filter filter = new Filter() {
            //根据用户在搜索框输入的字符串CharSequence，调用一个工作线程过滤数据
            //过滤结果以FilterResults的形式返回，然后在UI进程通过publishResults()赋值到搜索结果列表mySearchCityList
            //如果CharSequence为null，原始数据必须恢复
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString().toUpperCase();
                //获取用户输入的信息，并加以改造（中文就不变，字母就全变成大写的）
                //用户输入的信息就是CharSequence
                //CharSequence是接口，String实现了它，如要转换，用CharSequence.toString()
                //如果是字母，就把它们都变成大写字母，如果是中文，toUpperCase()不发挥作用
                FilterResults results = new FilterResults();//返回的过滤结果
                ArrayList<City> filterList = new ArrayList<City>();//存储过滤后留下来的数据，最后，把它给FilterResults


                if(myCityList != null && myCityList.size() != 0) {
                    for(City city: myCityList) {
                        //int indexOf(String str): 返回str在字符串中第一次出现处的索引，若没有，返回 -1
                        if(city.getCity().indexOf(str)>-1 || city.getAllPY().indexOf(str)>-1 || city.getAllFirstPY().indexOf(str)>-1) {
                            filterList.add(city);
                        }
                    }
                }

                results.values = filterList;//过滤后留下来的数据当做FilterResults的值
                results.count = filterList.size();//过滤的数据个数

                return results;
            }

            @Override
            //通过调用UI进程在用户界面发布过滤结果
            //Filter的子类FilterResults必须实现该方法来显示performFiltering()过滤的结果
            protected void publishResults(CharSequence constrain, FilterResults results) {
                mySearchCityList = (ArrayList<City>)results.values;

                if(results.count > 0) {
                    notifyDataSetChanged();//如果适配器的内容改变（搜索有结果），调用getView(),更新每行item的视图
                }else {
                    notifyDataSetInvalidated();//没搜索到结果，还原的初始状态
                }
            }

        };
        return filter;
    }



}

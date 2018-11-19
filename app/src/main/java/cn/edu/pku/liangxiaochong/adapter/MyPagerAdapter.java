package cn.edu.pku.liangxiaochong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cn.edu.pku.liangxiaochong.bean.FutureThreeWeather;
import cn.edu.pku.liangxiaochong.miniweather.R;

public class MyPagerAdapter extends PagerAdapter{
    private Context mycontext;
    private List<View> mylist;


    public MyPagerAdapter(Context context, List<View> list) {
        this.mycontext=context;
        this.mylist=list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        container.addView(mylist.get(position));
        return mylist.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)mylist.get(position));
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }
}

/*
选择城市界面
 */
package cn.edu.pku.liangxiaochong.miniweather;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.liangxiaochong.app.MyApplication;
import cn.edu.pku.liangxiaochong.bean.City;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView myBackBtn;
    //声明ListView对象，绑定select_city布局文件中的ListView
    private ListView myListView;
    //声明TextView对象，用于绑select_city布局文件中顶部栏的显示内容
    private TextView citySelected;

   // private String cityCode;//保存所选择城市代码，按返回键时返回该代码


    private List<City> list_of_city = MyApplication.getInstance().getCityList();
    int size = list_of_city.size();
    private String[] city = new String[size];//爬下n城市，就创建有n个元素的String数组



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        myBackBtn = (ImageView) findViewById(R.id.title_select_city_bar_back);
        //更改顶栏显示：哪个城市
        citySelected = (TextView) findViewById(R.id.title_name);
        //启动myBackBtn监听
        myBackBtn.setOnClickListener(this);
        //看是否爬下的城市信息导入成功
        Log.i("city", list_of_city.get(1).getCity());
        //将爬下来的城市信息写入city数组
        for(int i=0; i<size; i++) {
            city[i] = list_of_city.get(i).getCity();
            Log.d("city", city[i]);
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, city);

        myListView = (ListView)findViewById(R.id.title_select_city_content_search_city_list);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity.this, "你已经选择： " + city[i], Toast.LENGTH_SHORT).show();
                citySelected.setText("现在选择城市： " + city[i]);
                //cityCode =  list_of_city.get(i).getNumber();
            }

        });





    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_select_city_bar_back:
                //在finish（）之前传递数据
                //用getCheckedItemPosition()来获取刚才点击的位置，我原先用一个全局变量保存
                // 所选城市代码，也可以达到如此效果
                int pos = myListView.getCheckedItemPosition();
                String cityCode = list_of_city.get(pos).getNumber();
                Intent i = new Intent();
                i.putExtra("cityCode", cityCode);//返回所选择城市的代码
                setResult(RESULT_OK, i);

                finish();
                break;
            default:
                break;
        }
    }

}
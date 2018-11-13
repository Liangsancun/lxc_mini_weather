/*
选择城市界面
 */
package cn.edu.pku.liangxiaochong.miniweather;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.liangxiaochong.adapter.SearchCityAdapter;
import cn.edu.pku.liangxiaochong.app.MyApplication;
import cn.edu.pku.liangxiaochong.bean.City;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView myBackBtn;

    private ListView myListView;//声明ListView对象，绑定select_city布局文件中的ListView,管理城市列表界面
    //声明TextView对象，用于绑select_city布局文件中顶部栏的显示内容
    private TextView citySelected;//在bar显示已经选择的城市
    private EditText myEditText;//搜索框输入的文字
    private SearchCityAdapter mySearchCityAdapter;//搜索城市的适配器
    private ArrayAdapter<String> cityAdapter;//没有搜索时，显示所有城市的适配器
    private String returncitycode;//最后返回选择的城市代码



    //弄一份爬下来的城市信息列表list_of_city
    private List<City> my_list_of_city ;

    TextWatcher myTextWatcher = new TextWatcher() {
        //用TextWatcher监听EditText变化
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        //变化中
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            mySearchCityAdapter = new SearchCityAdapter(SelectCity.this, my_list_of_city);
            //上下文环境，数据源
            myListView.setTextFilterEnabled(true);//开启过滤功能
            if(my_list_of_city.size()<-1 || TextUtils.isEmpty(s)) {
                //下载的城市列表为空，或输入的CharSequence为null或为空 ""，设置数组适配器ArrayAdapter
                //TextUtils.isEmpty()判断CharSequence类型，EditView里输入的内容时CharSequence类型
                myListView.setAdapter(cityAdapter);
            }else {
                myListView.setAdapter(mySearchCityAdapter);
                mySearchCityAdapter.getFilter().filter(s);//在所有的城市中过滤搜索信息得到结果，
                // 并显示，因为filter里，如果有搜索结果，会自动调用mySearchCityAdapter里的重写函数getView()
                //更新每一行item的视图

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        myBackBtn = (ImageView) findViewById(R.id.title_select_city_bar_back);
        //启动myBackBtn监听，this指的是继承了onClickListener的SelectCity(该类）
        myBackBtn.setOnClickListener(this);
        //更改顶栏显示：选择了哪个城市
        citySelected = (TextView) findViewById(R.id.title_name);
        //搜索框
        myEditText = (EditText) findViewById(R.id.title_select_city_search_bar_textbox);
        myEditText.addTextChangedListener(myTextWatcher);

        //更新ListView
        updateListVeiw();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_select_city_bar_back:
                //在finish（）之前传递数据
                //用getCheckedItemPosition()来获取刚才点击的位置，我原先用一个全局变量保存
                // 所选城市代码，也可以达到如此效果
                int pos = myListView.getCheckedItemPosition();
                String cityCode = returncitycode;
                Intent i = new Intent();
                i.putExtra("cityCode", returncitycode);//返回所选择城市的代码
                setResult(RESULT_OK, i);
                finish();
                break;

            default:
                break;
        }
    }

    //用适配器更新ListView
    void updateListVeiw() {
        //arrayAdapter适配器
        my_list_of_city = MyApplication.getInstance().getCityList();//完整的城市列表

        final String[] cityList = new String[my_list_of_city.size()];//完整的城市名字数组

        int i=0;
        for(City city:my_list_of_city) {
            cityList[i] = city.getCity();
            i++;
        }

        myListView = (ListView)findViewById(R.id.title_select_city_content_city_list);

        //没有搜索时，显示所有城市的适配器
        cityAdapter = new ArrayAdapter<String>(SelectCity.this, android.R.layout.simple_list_item_1, cityList);
        //传递字符串数组的适配器，数据源是字符串数组


        myListView.setAdapter(cityAdapter);//使得在刚进去选择城市时，就有全部的城市列表，如果不加这句，搜索前只有搜索框，别的啥也没有

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                City city;
                if(mySearchCityAdapter != null) {
                    city=(City)mySearchCityAdapter.getItem(position);
                }else {
                    city = my_list_of_city.get(position);
                }
                returncitycode = city.getNumber();//返回选择城市的代码
                citySelected.setText("当前城市："+city.getCity());//工具栏显示，选择了哪个城市
                Toast.makeText(SelectCity.this, "你单击了"+position+" 城市编码为："+returncitycode, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
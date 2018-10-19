/*
选择城市界面
 */
package cn.edu.pku.liangxiaochong.miniweather;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView myBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        myBackBtn = (ImageView) findViewById(R.id.title_back);
        myBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                //在finish（）之前传递数据
                Intent i = new Intent();
                i.putExtra("cityCode", "101160101");
                setResult(RESULT_OK, i);

                finish();
                break;
            default:
                break;
        }
    }

}
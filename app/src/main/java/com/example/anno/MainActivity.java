package com.example.anno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anno.listener.InjectListener;
import com.example.anno.listener.annotation.OnClick;
import com.example.anno.listener.annotation.OnLongClick;

public class MainActivity extends AppCompatActivity {
    Button btn_hw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectListener.getInstance().init(this);
        btn_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_hw.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @OnClick(R.id.btn_hw)
    private void btnHW() {
        Toast.makeText(getBaseContext(), "我是OnClick注解绑定的事件哦！",
                Toast.LENGTH_SHORT).show();
    }

    @OnLongClick(R.id.btn_long)
    private boolean btnLong() {
        Toast.makeText(getBaseContext(), "我是OnLongClick注解绑定的事件哦！",
                Toast.LENGTH_SHORT).show();
        return true;
    }
}

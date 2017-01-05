package com.gusw.notificationdemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ViewGroup mButtonContainer;

    private NotificationManager mNm;
    private int mId = 1;

    private List<CheckBox> mProperties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonContainer = (ViewGroup) findViewById(R.id.button_container);
        mNm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        addProperty("ContentTitle", true, new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                builder.setContentTitle("ContentTitle");
            }
        });
        addButton("send");
    }

    private void addButton(CharSequence text) {
        Button btn = (Button) LayoutInflater.from(this)
                .inflate(R.layout.button, mButtonContainer, false);
        btn.setText(text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification.Builder builder = new Notification.Builder(MainActivity.this);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                for (CheckBox checkBox : mProperties) {
                    if (checkBox.isChecked()) {
                        Object tag = checkBox.getTag();
                        if (tag != null && tag instanceof CheckBoxListener) {
                            ((CheckBoxListener) tag).run(builder);
                        }
                    }
                }
                mNm.notify(nextId(), builder.build());
            }
        });
        mButtonContainer.addView(btn);
    }

    private void addProperty(CharSequence text, boolean checked, CheckBoxListener listener) {
        CheckBox checkbox = (CheckBox) LayoutInflater.from(this)
                .inflate(R.layout.checkbox, mButtonContainer, false);
        checkbox.setText(text);
        checkbox.setChecked(checked);
        checkbox.setTag(listener);
        mProperties.add(checkbox);
        mButtonContainer.addView(checkbox);
    }

    private int nextId() {
        return ++mId;
    }
}

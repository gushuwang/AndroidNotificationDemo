package com.gusw.notificationdemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ViewGroup mButtonContainer;

    private NotificationManager mNm;
    private int mId = 1;

    private List<CheckBox> mCheckboxProperties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonContainer = (ViewGroup) findViewById(R.id.button_container);
        mNm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        addCheckboxProperty("ContentTitle", true, new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                builder.setContentTitle("ContentTitle");
            }
        });
        addCheckboxProperty("ContentText", true, new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                builder.setContentText("ContentText");
            }
        });
        addCheckboxProperty("SubText", false, new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                builder.setSubText("SubText");
            }
        });
        addCheckboxProperty("LargeIcon", false, new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                Bitmap large = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                builder.setLargeIcon(large);
            }
        });
        if (Build.VERSION.SDK_INT >= 17) {
            addCheckboxProperty("ShowWhen", false, new CheckBoxListener() {
                @Override
                public void run(Notification.Builder builder) {
                    builder.setShowWhen(true);
                }
            });
        }

        addCheckboxProperty("UsesChronometer", false, new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                builder.setUsesChronometer(true);
            }
        });
        addCheckboxProperty("Ticker", false, new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                builder.setTicker("Ticker");
            }
        });
        addProgressProperty("Progress", false, true);

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
                for (CheckBox checkBox : mCheckboxProperties) {
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

    private void addCheckboxProperty(CharSequence text, boolean checked, CheckBoxListener listener) {
        CheckBox checkbox = (CheckBox) LayoutInflater.from(this)
                .inflate(R.layout.checkbox, mButtonContainer, false);
        checkbox.setText(text);
        checkbox.setChecked(checked);
        checkbox.setTag(listener);
        mCheckboxProperties.add(checkbox);
        mButtonContainer.addView(checkbox);
    }

    private void addProgressProperty(CharSequence text, boolean checked, boolean indeterminate) {
        View container = LayoutInflater.from(this).inflate(R.layout.progress, mButtonContainer, false);
        CheckBox progress = (CheckBox) container.findViewById(R.id.progress_checkbox);
        final RadioButton indeterminateBtn = (RadioButton) container.findViewById(R.id.progress_indeterminate);

        progress.setChecked(checked);
        indeterminateBtn.setChecked(indeterminate);

        progress.setTag(new CheckBoxListener() {
            @Override
            public void run(Notification.Builder builder) {
                builder.setProgress(100, 70, indeterminateBtn.isChecked());
            }
        });

        mCheckboxProperties.add(progress);
        mButtonContainer.addView(container);
    }

    private int nextId() {
        return ++mId;
    }
}

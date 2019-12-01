package com.example.wechatwallet;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import de.robv.android.xposed.XposedBridge;

public class TextViewWatcher implements TextWatcher {
    public static final String LAST_TEXT = "66666666666666666.00";
    private TextView textView;

    public TextViewWatcher(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        XposedBridge.log("textView内容: " + text);
        if (text.contains("¥")) {
            textView.removeTextChangedListener(this);
            textView.setText(LAST_TEXT);
            textView.addTextChangedListener(this);
        }
    }
}

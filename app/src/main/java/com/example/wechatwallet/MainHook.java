package com.example.wechatwallet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.tencent.mm")) {
            String hookClass = "com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI";
            String hookMethodName = "onCreate";

            XposedHelpers.findAndHookMethod(hookClass, lpparam.classLoader, hookMethodName, Bundle.class, new XC_MethodHook () {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object walletBalanceActivity = param.thisObject;
                    Field[] allField = walletBalanceActivity.getClass().getDeclaredFields();
                    for (Field field : allField) {
                        field.setAccessible(true);
                        Object fieldObject = field.get(walletBalanceActivity);
                        if (fieldObject != null && fieldObject instanceof TextView) {
                            TextView textView = (TextView) fieldObject;
                            textView.addTextChangedListener(new TextViewWatcher(textView));
                            XposedBridge.log(field.getName() + ", " + textView.getText().toString());
                        }
                    }
                }
            });
        }
    }
}


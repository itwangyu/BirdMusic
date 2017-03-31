package com.example.ex.mvp;

import android.content.Context;

/**
 * MVPPlugin
 */

public interface BaseView {
     Context getContext();
     void showLoadingDialog();
     void dismissLoadingDialog();
}

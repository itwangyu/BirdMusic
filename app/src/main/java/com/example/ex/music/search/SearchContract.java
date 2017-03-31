package com.example.ex.music.search;

import com.example.ex.mvp.BasePresenter;
import com.example.ex.mvp.BaseView;


/**
 * MVPPlugin
 */

public class SearchContract {
    interface View extends BaseView {

    }

    interface  Presenter extends BasePresenter<View> {
        void search(String key);
    }
}

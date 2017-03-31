package com.example.ex.music;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ex.MusicService.MusicService;
import com.example.ex.MusicService.ResultBean;
import com.example.ex.adapter.SearchResultAdapter;
import com.example.ex.view.MyListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private static final String HOST = "http://music.163.com/weapi/cloudsearch/get/web?csrf_token=";
    @BindView(R.id.et_keyword)
    EditText etKeyword;
    @BindView(R.id.bt_search)
    Button btSearch;
    @BindView(R.id.myListview)
    MyListView myListview;
    @BindView(R.id.rg)
    RadioGroup radioGroup;
    private int musicSource=0;
    private SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_wy:
                        musicSource=MusicService.WY;
                        break;
                    case R.id.rb_qq:
                        musicSource=MusicService.QQ;
                        break;
                    case R.id.rb_kg:
                        musicSource=MusicService.KG;
                        break;
                    case R.id.rb_xm:
                        musicSource=MusicService.XM;
                        break;
                }
            }
        });
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchResult();
                return true;
            }
        });
        adapter = new SearchResultAdapter(this);
        myListview.setAdapter(adapter);
        myListview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchResult();
            }
        });
        myListview.setOnLoadMoreListener(new MyListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                searchResult();
            }
        });
        myListview.setVisibility(View.GONE);
    }

    @OnClick(R.id.bt_search)
    public void onViewClicked() {
        searchResult();
    }

    private void searchResult() {
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etKeyword.getWindowToken(), 0);
        myListview.setVisibility(View.VISIBLE);
        Log.i("wangyu", musicSource + "音乐院");
        Observable.create(new Observable.OnSubscribe<ResultBean>() {
            @Override
            public void call(Subscriber<? super ResultBean> subscriber) {
                ResultBean resultBean = MusicService.GetMusic(musicSource).SongSearch(etKeyword.getText().toString().trim(), myListview.getCurPage(), 15);
                subscriber.onNext(resultBean);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResultBean>() {
                    @Override
                    public void call(ResultBean resultBean) {
                        if (resultBean.resultList != null && resultBean.resultList.size() != 0) {
                            myListview.setOnNetSuccess(resultBean.total,resultBean.resultList);
                        }
                    }
                });
    }
}

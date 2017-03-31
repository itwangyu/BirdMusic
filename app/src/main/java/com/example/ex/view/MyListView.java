package com.example.ex.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ex.music.R;

import java.util.List;

/**
 * Created by wangyu on 2016/11/29 0029.
 */

public class MyListView extends SwipeRefreshLayout {
    private Context context;
    /*
	 * listview相关
	 */
    private TextView tv_running;
    private ProgressBar pb_running;
    private int page = 1;
    private boolean canFresh;
    private boolean is_divPage;// 是否进行分页操作
    private int totalCount, pageCount = 15;
    private View listFooter;
    private OnLoadMoreListener loadMoreListener;
    private BaseXutilsAdapter adapter;
    public boolean isShowGoTop=false;
    private onListviewScrollListener listener;
    private onListViewRefreshListener refreshListener;
    private int rows=15;

    public ListView getListView() {
        return listView;
    }

    private ListView listView;

    public MyListView(Context context) {
        super(context);
        this.context=context;
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        setColorSchemeResources(R.color.d8b75d);
        listView = new ListView(context);
        addView(listView);
        // 初始化listview footer
        listFooter = View.inflate(context,R.layout.listview_footer_view, null);
        tv_running = (TextView) listFooter.findViewById(R.id.tv_running);
        pb_running = (ProgressBar) listFooter.findViewById(R.id.pb_running);
        listView.addFooterView(listFooter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (canFresh && is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    page++;
                    if (loadMoreListener!=null) {
                        loadMoreListener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount >= totalItemCount-1);
                if (listener != null) {
                    listener.onscroll(firstVisibleItem);
                }
            }
        });
    }


    public void setListViewRefreshListener(onListViewRefreshListener listener) {
        this.refreshListener=listener;
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    public interface onListViewRefreshListener {
        void onRefresh();
    }

    /**
     * refresh data
     */
    public void refresh() {
        if (refreshListener != null) {
            page=1;
            rows=15;
            refreshListener.onRefresh();
        }
    }

    public void start() {
        if (refreshListener != null) {
            rows=rows*page;
            page=1;
            refreshListener.onRefresh();
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener=loadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setAdapter(BaseXutilsAdapter adapter) {
        this.adapter=adapter;
        listView.setAdapter(adapter);
    }

    public void setOnNetSuccess(int total, List list) {
        setRefreshing(false);
        if (page == 1) {
            totalCount =total;
            adapter.removeAllData();
            setRefreshing(false);
        }
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(listFooter);
        }
        setListFooter(page, totalCount);
        if (list!=null) {
            adapter.addData(list);
        }
    }
    private void setListFooter(int page, int totalItem) {
        if (page * pageCount >= totalItem) {
            canFresh = false;
            tv_running.setText("暂无更多记录");
            pb_running.setVisibility(View.GONE);
        } else {
            canFresh = true;
            tv_running.setText("正在加载");
            pb_running.setVisibility(View.VISIBLE);
        }
    }
    public void setLoading() {
        adapter.removeAllData();
        tv_running.setText("正在加载");
        pb_running.setVisibility(View.VISIBLE);
    }
    public void scrollTop() {
        listView.smoothScrollToPosition(0);
    }

    public interface onListviewScrollListener {void onscroll(int firstVisiableItem);}
    public void setListViewOnscrollListener(onListviewScrollListener listener) {
        this.listener=listener;
    }
    public int getCurPage() {
        return page;
    }
    public int getRows() {
        return rows;
    }
}

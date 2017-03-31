package com.example.ex.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex.MusicService.SongResult;
import com.example.ex.music.R;
import com.example.ex.util.AppInfoUtil;
import com.example.ex.view.BaseXutilsAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by wangyu on 2017/3/29 0029.
 */

public class SearchResultAdapter extends BaseXutilsAdapter<SongResult> {
    private Context context;
    private TextView tv_total;
    private TextView tv_path;
    private TextView tv_progress;
    private ProgressBar progressBar;
    private Button bt;
    private String quilty;
    private final DecimalFormat df;

    public SearchResultAdapter(Context context) {
        super(context);
        this.context=context;
        df = new DecimalFormat();
        df.applyPattern("####.##");
    }

    @Override
    public int getConvertViewId(int layoutId) {
        return R.layout.item_resultlist;
    }

    @Override
    public BaseViewHolderInject<SongResult> getNewHolder(int position) {
        return new MyViewHolder();
    }

    public class MyViewHolder extends BaseViewHolderInject<SongResult> {
        @BindView(R.id.tv_songname)
        TextView tvSongname;
        @BindView(R.id.tv_artistname)
        TextView tvArtistname;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.bt_download)
        Button btDownload;
        String url="";
        @Override
        public void loadData(final SongResult data, int position) {
            tvArtistname.setText(data.getArtistName());
            tvTime.setText(data.getLength());

            if (data.getFlacUrl()!="") {
                url=data.getFlacUrl();
                quilty = "无损";
            } else if (data.getSqUrl()!="") {
                url=data.getSqUrl();
                quilty = "320k";
            } else if (data.getHqUrl() != "") {
                url = data.getHqUrl();
                quilty = "192k";
            } else {
                url = data.getLqUrl();
                quilty = "128k";
            }
            tvSongname.setText(data.getSongName()+"   音质："+quilty);
            btDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (url=="") {
                        Toast.makeText(context,"没有下载连接，请选择其他歌曲",Toast.LENGTH_LONG).show();
                        return;
                    }
                    OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aMusic",data.getSongName()+".mp3") {

                        private Dialog dialog;

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            dialog = showSignDiaolog((Activity) context);
                        }

                        @Override
                        public void inProgress(float progress, long total, int id) {
                            super.inProgress(progress, total, id);
                            String result = df.format((float)(total/1024f/1024f));
                            tv_total.setText("总大小："+result+"MB     当前品质："+quilty);
                            tv_progress.setText((int)(progress*100)+"%");
                            progressBar.setProgress((int)(progress*100));
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            bt.setText("下载失败");
                            bt.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            tv_path.setText("目录："+response.getAbsolutePath().toString());
                            bt.setText("下载完毕");
                            bt.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        }
    }
    /**
     * 签到成功对话框
     */
    public Dialog  showSignDiaolog(Activity context) {
        LinearLayout ll_dialog = (LinearLayout) View.inflate(context, R.layout.dialog_signin, null);
        tv_total = (TextView) ll_dialog.findViewById(R.id.tv_total);
        tv_path = (TextView) ll_dialog.findViewById(R.id.tv_mulu);
        tv_progress = (TextView) ll_dialog.findViewById(R.id.tv_progress);
        progressBar = (ProgressBar) ll_dialog.findViewById(R.id.progressBar);
        bt = (Button) ll_dialog.findViewById(R.id.bt_close);
        final Dialog dialog = new Dialog(context, R.style.dialog_signin);
        dialog.setContentView(ll_dialog, new LinearLayout.LayoutParams(
                AppInfoUtil.getScreenWidth(context) - AppInfoUtil.dip2px(context, 50), LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCanceledOnTouchOutside(false);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
}

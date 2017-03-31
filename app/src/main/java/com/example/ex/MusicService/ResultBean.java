package com.example.ex.MusicService;

import java.util.List;

/**
 * Created by wangyu on 2017/3/29 0029.
 */

public class ResultBean {
    public int total;
    public List<SongResult> resultList;

    public ResultBean(int total, List<SongResult> resultList) {
        this.total = total;
        this.resultList = resultList;
    }
}

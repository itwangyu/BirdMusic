package com.example.ex.MusicService;

/**
 * Created by qtfreet00 on 2017/2/4.
 */
public class MusicService {
    public static final int WY=0,TT=1,QQ=2,KG=3,XM=4;
    public static IMusic GetMusic(int type) {
        switch (type) {
            case WY:
                return new WyMusic();
//            case TT:
//                return new TtMusic();
            case QQ:
                return new TxMusic();
            case KG:
                return new KgMusic();
            case XM:
                return new XmMusic();
            default:
                return new WyMusic();
        }
    }
}

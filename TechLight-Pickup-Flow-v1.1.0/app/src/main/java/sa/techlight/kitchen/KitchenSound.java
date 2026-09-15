package sa.techlight.kitchen;
import android.content.*;
import android.media.*;
import android.os.*;
import java.util.*;

final class KitchenSound {
    static final int NEW=0,ADDED=1,LATE=2,READY=3;
    private final SoundPool pool;private final int[] ids=new int[4];private final Set<Integer> loaded=new HashSet<>();
    private final Handler main=new Handler(Looper.getMainLooper());private final SharedPreferences prefs;private boolean released;private int current;private long playedAt;
    KitchenSound(Context context){prefs=context.getSharedPreferences("premium_settings",0);pool=new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()).build();pool.setOnLoadCompleteListener((p,id,status)->{if(status==0)loaded.add(id);});int[] res={R.raw.alert_new,R.raw.alert_add,R.raw.alert_late,R.raw.alert_ready};for(int i=0;i<4;i++)ids[i]=pool.load(context,res[i],1);}
    void play(int type){play(type,false);}
    void play(int type,boolean force){
        if(released||type<0||type>=ids.length||!prefs.getBoolean("sound",true))return;
        long now=SystemClock.elapsedRealtime();if(!force&&now-playedAt<1200)return;
        if(!loaded.contains(ids[type])){main.postDelayed(()->{if(!released&&loaded.contains(ids[type]))play(type,force);},180);return;}
        float volume=Math.max(0,Math.min(100,prefs.getInt("volume",90)))/100f;if(volume<=0)return;
        if(current!=0)pool.stop(current);current=pool.play(ids[type],volume,volume,1,0,1f);playedAt=now;
    }
    void release(){released=true;main.removeCallbacksAndMessages(null);pool.release();loaded.clear();}
}

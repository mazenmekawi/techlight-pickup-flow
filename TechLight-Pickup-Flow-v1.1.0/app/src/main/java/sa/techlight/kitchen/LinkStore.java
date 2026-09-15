package sa.techlight.kitchen;
import android.content.*;import org.json.*;import java.util.*;
final class LinkStore {
    final SharedPreferences prefs;
    LinkStore(Context c,String scope){prefs=c.getSharedPreferences("pickup_"+scope,0);}
    synchronized JSONObject read(){try{return new JSONObject(prefs.getString("data","{}"));}catch(Exception e){return new JSONObject();}}
    synchronized void save(JSONObject data){prefs.edit().putString("data",data.toString()).apply();}
    static JSONArray array(JSONObject o,String key){JSONArray a=o.optJSONArray(key);return a==null?new JSONArray():a;}
    synchronized JSONArray screens(){return array(read(),"screens");}
    synchronized JSONArray providers(){JSONObject all=read();JSONArray a=all.optJSONArray("providers");if(a==null){a=new JSONArray();try{a.put(new JSONObject().put("id","hungerstation").put("name","هنقرستيشن / HungerStation").put("color","#FFBD35").put("enabled",true));a.put(new JSONObject().put("id","keeta").put("name","كيتا / Keeta").put("color","#E6DC41").put("enabled",true));a.put(new JSONObject().put("id","mrsool").put("name","مرسول / Mrsool").put("color","#67D58B").put("enabled",true));all.put("providers",a);save(all);}catch(Exception ignored){}}return a;}
    void putArray(String key,JSONArray value){try{JSONObject o=read();o.put(key,value);save(o);}catch(Exception e){throw new IllegalStateException(e);}}
    boolean automatic(){return prefs.getBoolean("auto",true);}boolean allowOffline(){return prefs.getBoolean("offline",true);}
    int fallback(){return prefs.getInt("eta_fallback",12);}int window(){return prefs.getInt("eta_window",20);}
    static JSONObject defaults()throws JSONException{return new JSONObject().put("mode","split").put("orientation",0).put("background","#0D1020").put("card","#1A2034").put("text","#F7F9FF").put("accent","#37D6A0").put("columns",3).put("numberSize",54).put("sound",true).put("volume",85).put("eta",true).put("title","جاهز للاستلام / Ready for pickup").put("logo","");}
    synchronized long nextSequence(){long seq=Math.max(prefs.getLong("sequence",0)+1,System.currentTimeMillis());if(!prefs.edit().putLong("sequence",seq).commit())throw new IllegalStateException("Cannot persist link revision");return seq;}
    String controller(){String id=prefs.getString("controller","");if(id.isEmpty()){id=UUID.randomUUID().toString();prefs.edit().putString("controller",id).apply();}return id;}
}

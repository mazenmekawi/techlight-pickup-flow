package sa.techlight.kitchen;
import android.content.*;
import android.security.keystore.*;
import android.util.Base64;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;

/** Credentials are never persisted. Session token is encrypted with an Android Keystore key. */
final class Session {
    private final SharedPreferences prefs;
    Session(Context c){prefs=c.getSharedPreferences("session_v680",0);}
    boolean exists(){return !prefs.getString("cipher","").isEmpty();}
    String user(){return prefs.getString("user","");}
    String pos(){return prefs.getString("pos","");}
    String namespace(){return hash(pos()+"|"+user());}
    static String hash(String s){try{byte[] h=MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));StringBuilder out=new StringBuilder();for(byte b:h)out.append(String.format(java.util.Locale.ROOT,"%02x",b));return out.toString();}catch(Exception e){throw new IllegalStateException("SHA-256 unavailable",e);}}
    private static synchronized SecretKey key() throws Exception {
        KeyStore ks=KeyStore.getInstance("AndroidKeyStore");ks.load(null);String alias="techlight_kitchen_session_v680";
        if(!ks.containsAlias(alias)){KeyGenerator g=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");g.init(new KeyGenParameterSpec.Builder(alias,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build());g.generateKey();}
        return (SecretKey)ks.getKey(alias,null);
    }
    void save(String token,String user,String pos)throws Exception{
        Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,key());byte[] encoded=c.doFinal(token.getBytes(StandardCharsets.UTF_8));
        if(!prefs.edit().putString("cipher",Base64.encodeToString(encoded,Base64.NO_WRAP)).putString("iv",Base64.encodeToString(c.getIV(),Base64.NO_WRAP)).putString("user",user).putString("pos",pos).commit())throw new java.io.IOException("تعذر حفظ الجلسة على الجهاز");
    }
    String token()throws Exception{
        if(!exists())return "";Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,key(),new GCMParameterSpec(128,Base64.decode(prefs.getString("iv",""),Base64.NO_WRAP)));
        return new String(c.doFinal(Base64.decode(prefs.getString("cipher",""),Base64.NO_WRAP)),StandardCharsets.UTF_8);
    }
    void saveTracking(String token)throws Exception{Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,key());prefs.edit().putString("tracking_cipher",Base64.encodeToString(c.doFinal(token.getBytes(StandardCharsets.UTF_8)),Base64.NO_WRAP)).putString("tracking_iv",Base64.encodeToString(c.getIV(),Base64.NO_WRAP)).commit();}
    String trackingToken()throws Exception{String v=prefs.getString("tracking_cipher","");if(v.isEmpty())return "";Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,key(),new GCMParameterSpec(128,Base64.decode(prefs.getString("tracking_iv",""),Base64.NO_WRAP)));return new String(c.doFinal(Base64.decode(v,Base64.NO_WRAP)),StandardCharsets.UTF_8);}
    void clear(){prefs.edit().clear().apply();}
}

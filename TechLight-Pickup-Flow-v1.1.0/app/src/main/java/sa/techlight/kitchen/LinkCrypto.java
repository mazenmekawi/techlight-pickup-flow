package sa.techlight.kitchen;
import javax.crypto.*;import javax.crypto.spec.*;import java.security.*;import java.util.*;import java.io.*;import java.nio.charset.StandardCharsets;
/** Authenticated LAN transport. A per-connection random challenge prevents replay across reconnects. */
final class LinkCrypto {
    static final int LIMIT=2*1024*1024;
    static String randomHex(int size){byte[] b=new byte[size];new SecureRandom().nextBytes(b);return hex(b);}
    static String hex(byte[] b){StringBuilder s=new StringBuilder();for(byte v:b)s.append(String.format(Locale.US,"%02x",v&255));return s.toString();}
    static byte[] unhex(String s){if(s==null||s.length()%2!=0||!s.matches("[a-fA-F0-9]+"))throw new IllegalArgumentException("Invalid key");byte[] b=new byte[s.length()/2];for(int i=0;i<b.length;i++)b[i]=(byte)Integer.parseInt(s.substring(i*2,i*2+2),16);return b;}
    static byte[] seal(String key,String text)throws Exception{byte[] iv=new byte[12];new SecureRandom().nextBytes(iv);Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(unhex(key),"AES"),new GCMParameterSpec(128,iv));c.updateAAD("TechLight-Pickup-v1".getBytes(StandardCharsets.UTF_8));byte[] enc=c.doFinal(text.getBytes(StandardCharsets.UTF_8));byte[] all=new byte[12+enc.length];System.arraycopy(iv,0,all,0,12);System.arraycopy(enc,0,all,12,enc.length);return all;}
    static String open(String key,byte[] data)throws Exception{if(data.length<28||data.length>LIMIT)throw new IOException("Invalid message length");Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,new SecretKeySpec(unhex(key),"AES"),new GCMParameterSpec(128,Arrays.copyOfRange(data,0,12)));c.updateAAD("TechLight-Pickup-v1".getBytes(StandardCharsets.UTF_8));return new String(c.doFinal(Arrays.copyOfRange(data,12,data.length)),StandardCharsets.UTF_8);}
    static void write(DataOutputStream out,byte[] data)throws IOException{if(data.length>LIMIT)throw new IOException("Message too large");out.writeInt(data.length);out.write(data);out.flush();}
    static byte[] read(DataInputStream in)throws IOException{int n=in.readInt();if(n<1||n>LIMIT)throw new IOException("Message too large");byte[] b=new byte[n];in.readFully(b);return b;}
}

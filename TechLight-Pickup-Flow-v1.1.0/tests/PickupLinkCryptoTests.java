package sa.techlight.kitchen;
import org.junit.*;import static org.junit.Assert.*;import java.io.*;
/** Four unchanged link tests extracted from the original LinkedTests class. */
public class PickupLinkCryptoTests {
    @Test public void authenticatedEncryptionRoundTrip()throws Exception{String key=LinkCrypto.randomHex(32);assertEquals("test عربي",LinkCrypto.open(key,LinkCrypto.seal(key,"test عربي")));}
    @Test public void tamperedMessageRejected()throws Exception{String key=LinkCrypto.randomHex(32);byte[] packet=LinkCrypto.seal(key,"safe");packet[packet.length-1]^=1;try{LinkCrypto.open(key,packet);fail();}catch(javax.crypto.AEADBadTagException expected){}}
    @Test public void wrongKeyRejected()throws Exception{byte[] packet=LinkCrypto.seal(LinkCrypto.randomHex(32),"safe");try{LinkCrypto.open(LinkCrypto.randomHex(32),packet);fail();}catch(javax.crypto.AEADBadTagException expected){}}
    @Test public void maxFrameEnforced()throws Exception{ByteArrayOutputStream bytes=new ByteArrayOutputStream();new DataOutputStream(bytes).writeInt(LinkCrypto.LIMIT+1);try{LinkCrypto.read(new DataInputStream(new ByteArrayInputStream(bytes.toByteArray())));fail();}catch(IOException expected){}}
}

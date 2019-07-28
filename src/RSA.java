import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

public class RSA {

	PublicKey publicKey;
	PrivateKey privateKey;
	
	public final PublicKey getPublicKey() {
		return publicKey;
	}
	
	public final PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	RSA() 
	{}
	
	public void generateKey() throws Exception
	{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair kp = kpg.generateKeyPair();
		this.publicKey = kp.getPublic();
		this.privateKey = kp.getPrivate();
		
	}
	
	public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
        return cipher.doFinal(encrypted);
    }
	
	public String getPublicKey_String ()
	{
		if (publicKey == null)
		{return null;}
		Base64.Encoder encoder = Base64.getEncoder();
		String temp = encoder.encodeToString(publicKey.getEncoded());
		return temp;
		
	}
	
	public static PublicKey convertStringToPubKey (String keyHolder) throws Exception
	{
		if (keyHolder == null)
		{return null;}
		byte[] publicBytes = Base64.getDecoder().decode(keyHolder);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		return pubKey;
		
	}
	
	
	
}

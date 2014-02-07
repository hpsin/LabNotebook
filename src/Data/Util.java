package Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import net.atlanticbb.tantlinger.io.IOUtils;

import org.apache.commons.codec.binary.Base64;


/**
 * @author Hirsch Singhal
 * 	Provides encryption, decryption, compression, and keymaking services. 
 */
public class Util {
	/**
	 * @param password String provided by user for password.
	 * @param saltString String provided by program to use as salt.
	 * @return AES key suitable for encryption and decryption of data.
	 */
	public static SecretKeySpec makeKey(String password, String saltString){
		if(password == null || password.isEmpty())return null;
		byte[] salt = (saltString+".nbk").getBytes();
		int iterations = 10000;
		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			SecretKey tmp= factory.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, 128));
			return new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Compresses, encrypts, and converts String to Base64 in that order. If
	 *  key is null, no encryption, compression, or encoding will be done.    
	 * @param data String to be encrypted.
	 * @param key Encryption key.   
	 * @return Base64 encoded String representation of data.
	 */
	public static String encrypt(String data, SecretKeySpec key) {
		if(key==null)return data;
		return encryptBytes(data.getBytes(), key);
	}
	
	
	/**
	 * Compresses, encrypts, and converts binary data to Base64 in that order.
	 *   If key is null, no encryption or compression will be done. 
	 * @param data Binary data to be encrypted.
	 * @param key Encryption key.   
	 * @return Base64 encoded String representation of data.
	 */
	public static String encryptBytes(byte[] data, SecretKeySpec key) {
		if(key==null) return Base64.encodeBase64String(data);
		Cipher aes;
		try {
			aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.ENCRYPT_MODE, key);
			return Base64.encodeBase64String(aes.doFinal(compress(data)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Performs decryption using provided {@link SecretKeySpec} key.  If no key
	 *  is provided, the original String is returned.  Wrapper method around 
	 *  decryptBytes
	 * @param data Base64-encoded compressed encrypted data.  
	 * @param key {@link SecretKeySpec} to decrypt data. 
	 * @return Decrypted, decompressed data.  
	 */
	public static String decrypt(String data, SecretKeySpec key){
		if(key!=null && !data.equals(" ")){
			return new String(decryptBytes(data.getBytes(), key));
		}
		return data;
	}
	
	/**
	 * Performs decryption using provided {@link SecretKeySpec} key.  If no key
	 *  is provided (unencrypted PDF), the the data is converted from Base64 and returned.
	 * @param data  compressed encrypted Base64-encoded data.  
	 * @param key {@link SecretKeySpec} to decrypt data. 
	 * @return Decrypted, decompressed data in a byte array.  
	 */
	public static byte[] decryptBytes(byte[] data, SecretKeySpec key){
		if(key==null)return Base64.decodeBase64(data);
		try {
			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.DECRYPT_MODE, key);
			return decompress(aes.doFinal(Base64.decodeBase64(data)));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	/**
	 * Compresses the provided byte array using GZip. 
	 * @param bytes byte array to be compressed.
	 * @return Compressed byte array.
	 */
	public static byte[] compress(byte[] bytes) {
		if (bytes.length == 0) {
			return bytes;
		}
		try{
			ByteArrayOutputStream obj=new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(obj);
			gzip.write(bytes);
			gzip.close();
			return obj.toByteArray();
		}catch(IOException e){
			e.printStackTrace();
		}
		return new byte[0];
	}

	/**
	 * Decompresses a byte array using GZip.
	 * @param bytes Compressed bytes.
	 * @return Decompressed byte array.
	 */
	public static byte[] decompress(byte[] bytes) {
		try{
			ByteArrayOutputStream bos= new ByteArrayOutputStream();
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
			IOUtils.copy(gis, bos);
			return bos.toByteArray();
		}catch(IOException e){
			e.printStackTrace();
		}
		return new byte[0];
	}
}

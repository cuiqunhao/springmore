package org.springmore.commons.security;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.springmore.commons.codec.Base64Util;
import static org.springmore.commons.codec.Charsets.UTF_8;

/**
 * RSA加解密工具类
 * 
 * @author 唐延波
 * @date 2015-6-9
 * @version 1.1
 * @date 2015-7-15
 * @update
 * 修改部分注释,使参数和返回值更容易阅读
 */
public abstract class RSAUtil {
	
	public static final String KEY_ALGORITHM = "RSA";
	
	public static final String PUBLIC_KEY = "RSAPublicKey";
	
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	
	public static final String RSA_model = "RSA/ECB/PKCS1Padding";

	private static final int KEY_SIZE = 512;

	/**
	 * 私钥解密
	 * 
	 * @param data 待解密二进制数据
	 * @param key 二进制密钥
	 * @return 二进制结果
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static byte[] decryptByPrivateKey(byte[] data, byte[] key)
			throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥解密 
	 * 
	 * @param base64data 密文数据base64
	 * @param base64key 密钥base64字符串
	 * @return 默认编码方式为utf-8
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static String decryptByPrivateKey(String base64data, String base64key)
			throws Exception {
		byte[] data = Base64Util.decodeBase64(base64data);
		byte[] key = Base64Util.decodeBase64(base64key);
		byte[] result = decryptByPrivateKey(data,key);
		return new String(result, UTF_8);
	}

	/**
	 * 公钥解密
	 * 
	 * @param data 密文数据
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static byte[] decryptByPublicKey(byte[] data, byte[] key)
			throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥解密
	 * 
	 * @param base64data base64(密文数据)
	 * @param base64key base64(密钥)
	 * @return UTF-8编码
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static String decryptByPublicKey(String base64data, String base64key)
			throws Exception {
		byte[] data = Base64Util.decodeBase64(base64data);
		byte[] key = Base64Util.decodeBase64(base64key);
		byte[] result = decryptByPublicKey(data,key);
		return new String(result,UTF_8);
	}

	/**
	 * 公钥加密
	 * 
	 * @param data 数据明文
	 * @param key 密钥        
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] key)
			throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥加密
	 * 
	 * @param data 数据明文字符串
	 * @param base64key 密钥base64
	 * @return 密文base64字符串
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static String encryptByPublicKey(String data, String base64key)
			throws Exception {
		byte[] key = Base64Util.decodeBase64(base64key);
		byte[] encryptData = encryptByPublicKey(data.getBytes(UTF_8),
				key);
		return Base64Util.encodeBase64String(encryptData);
	}

	/**
	 * 私钥加密
	 * 
	 * @param data 数据明文
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] key)
			throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥加密
	 * 
	 * @param data 明文字符串
	 * @param base64key 密钥base64
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-9
	 */
	public static String encryptByPrivateKey(String data, String base64key)
			throws Exception {
		byte[] signByte = encryptByPrivateKey(data.getBytes(),
				Base64Util.decodeBase64(base64key));
		return Base64Util.encodeBase64String(signByte);
	}

	// 私钥加密 RSA使用 RSA/ECB/PKCS1Padding 组合模式补位。
	public static byte[] encryptByPrivateKeyIss(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = Base64Util.decodeBase64(key);
		// 取得私钥
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(keySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(RSA_model);
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	// 私钥验证公钥密文
	public static boolean checkPublicEncrypt(String data, String sign,
			String pvKey) throws Exception {
		return data.equals(decryptByPrivateKey(sign, pvKey));
	}

	public static boolean checkPrivateEncrypt(String data, String sign,
			String pbKey) throws Exception {
		return data.equals(decryptByPublicKey(sign, pbKey));
	}

	// 取得私钥
	public static byte[] getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	// 取得公钥
	public static byte[] getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * 初始化密钥
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015年7月15日
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);

		keyPairGen.initialize(KEY_SIZE);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * 获取公钥
	 * 
	 * @param certPath 公钥证书地址(.cer文件)
	 * @return base64密钥
	 * @throws Exception
	 */
	public static String getPublicKeyByCert(String certPath) throws Exception {
		CertificateFactory cff;
		try {
			cff = CertificateFactory.getInstance("X.509");
			FileInputStream fis = new FileInputStream(certPath);
			java.security.cert.Certificate certificate = cff
					.generateCertificate(fis);
			PublicKey publicKey = certificate.getPublicKey();
			fis.close();
			return Base64Util.encodeBase64String(publicKey.getEncoded());
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 获得私钥
	 * 
	 * @param certPath 证书地址(.pfx)
	 * @param password 证书密码
	 * @return base64密钥
	 * @throws Exception
	 */
	public static String getPrivateKeyByCert(String certPath, String password)
			throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		InputStream fis = new FileInputStream(certPath);
		keyStore.load(fis, password.toCharArray());
		Enumeration<String> aliases = keyStore.aliases();
		String alias = null;
		if (aliases.hasMoreElements()) {
			alias = aliases.nextElement();
		}
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias,
				password.toCharArray());
		fis.close();
		return Base64Util.encodeBase64String(privateKey.getEncoded());
	}

}

package com.test.maven.rsasample;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * RSA 암복호화 JAVA 샘플 코드
 *
 */
public class App {

	static PublicKey publicKey;
	static PrivateKey privateKey;

	// main 메소드
	public static void main(String[] args) throws Exception {
		//1) RSA 키 생성
//	 	createRSAKeys(); // 최초에 키 만들때만 호출
//
//		//고정된 문자열 키 값(Base64 인코딩 된 값)으로 공개키,개인키 객체 생성
//		StringToPublicKey(
//				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoCWgdLBONBBuNrQMLcd8dM+3TkGLE+V1Vm46vHwby3Pjm1h8hqhYmu4ZW/a8niqCN0f+4Z9bEN7ygEoFwcVnCqFyXIf5eXkqgpdtuQSJduPZIL1CUufOGC1wTR4DgTr9+w0xVQHrOtu84Ruo9QvXb784HWYIOc/yCwuwsj67mW9QFIPO1dSy1tn0nHMH2lr9rYgrJ3BVBEhGJr3gXb+0t9TNNsqmBpp0pyj0PYJnxCy75GPvQOeJdYWmXOCIutqTELdMZRnkj4KbezmnzlwL1GmeYNgsG49mnZT4whUm1zpFqa0eVtF0XKhRqRPW00VxSLUCb1M2/O0dmTn2CRReLQIDAQAB");
//		StringToPrivateKey(
//				"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgJaB0sE40EG42tAwtx3x0z7dOQYsT5XVWbjq8fBvLc+ObWHyGqFia7hlb9ryeKoI3R/7hn1sQ3vKASgXBxWcKoXJch/l5eSqCl225BIl249kgvUJS584YLXBNHgOBOv37DTFVAes627zhG6j1C9dvvzgdZgg5z/ILC7CyPruZb1AUg87V1LLW2fSccwfaWv2tiCsncFUESEYmveBdv7S31M02yqYGmnSnKPQ9gmfELLvkY+9A54l1haZc4Ii62pMQt0xlGeSPgpt7OafOXAvUaZ5g2Cwbj2adlPjCFSbXOkWprR5W0XRcqFGpE9bTRXFItQJvUzb87R2ZOfYJFF4tAgMBAAECggEAVghArc81A+U7wClzTXiYVIxuFh1B4LtpELiA11b4+5BTWtXrd8+iYgC9INlpNLLzZ01HEOAx5NtQfTjG5PTuSRi4PhtSGz7LZoKBEF7uli8LKvphUfZzkit+4/7WtSsC3EaXFOrF+sWnL2tTlej5oG+ibrCshJpAlkx+bRMkZuU5dZDFYkHdPKjTjqbq1EiShCP8Sq+7oTfoO9XjSJ4Gl/Xi2VJF9gOJr3d89ELZVHJwmv5RZTjTdzBPF+3ucHkvVWa/GMwpaDuD3HHOVFpQLjP+RM6UyVcq/Py6ZeKgqayuv3PdZ2w+JjWHzgD2fFBNUirnN3/+yOO6NNKpNbp9BQKBgQDlAmi7h1H7v63sYj5RZxogV3rohYQpHoVi1AADSweHkreOIX0iLWiR/N9Qaqz0URJ2yxYpfW66QxQteurl7hoiqoDD1bTTzTeM6LVIfBCTZ1vYwWgQnkirM48i0FJb2i3ciKCn3fxsTsVmzx3r3hXe+gu7OgxroP/XCTWd/DzonwKBgQCzBYZXe2T42SIKMcSMuAeNxp9DtCp/pCYLDE+m4kTFtgq8MP2ufDEJb4UYGbc2ct1FUFc3D4y6SOQuj6fcEMNDABoR7NujArl+Egk0rJtR2vj1R712VH30f/ZaNFu/Y3n8XUH5IoRwj6MxGGpT1FXx2TbAr7zoMMhZ83l6DhvpswKBgQDGJbR69dYAzlPtYP/srRnU+/wzrjqw3WCqJyavsr6cFl1N1DoSVVnSXwrV6McnSfKA6K2PTqF89kdCJsWSFtd0QTTSvVkNndW/zSX1xqsM73Rv8sBZCKt1vlHN3JQ2N2xREcJ4HMGo8OsqTnDkQa+I069M2ygeBBr5c1V6EeKdnwKBgHxn33bVylJgWhZphWQg0ycEG1NqVYpEISfWU1fur8oRep5TEUd7CHy9IemgnUBaTEhJz4H07V/bHa75p7X3dCoQXQ2N4wgB8bOZhwoy1PDqUc3qH9CmYK1Ta3g+IG/9Ch2FFT6zbQPn3YlVpTZCPcOGplT7CzT0CZbw6r8WJPGfAoGAAnpThuBB+mYyiQ4r1QvDmRmn2mCGpFFAkO4kM7HFcCqPdXsL89s4NeLmn7Wf1uT+8AW/sCoot3FZp7ES5s7eVuVRIhjVlsZWLGo3gd/sdssQUC+N9t2jq/mIdUa3+AopNXwA/C39qthZ77dItPGWVafyH6ligtg337R5HwWz5Tw=");
//
//		// 공개키, 개인키 객체를 Base64 인코딩하여, 고정된 문자열 키 값과 동일한지 확인
//		String base64PublicKey = Base64.encodeBase64String(publicKey.getEncoded());
//		String base64PrivateKey = Base64.encodeBase64String(privateKey.getEncoded());
//		System.out.println("PublicKey : " + base64PublicKey);
//		System.out.println("PrivateKey : " + base64PrivateKey);
//
//		// RSA 암호화
//		String encrypted = encryptRSA("477615927", base64PublicKey);
//		System.out.println("암호화 결과 : " + encrypted);
//
//		// 2048 bits로 키 생성한 경우 암호화 데이터 바이트 크기는 256 byte
//		System.out.println("암호화 데이터 바이트 크기 : " + Base64.decodeBase64(encrypted).length);
//
//		// RSA 복호화
//		String decrypted = decryptRSA(encrypted, base64PrivateKey);
//		System.out.println("복호화 결과 : " + decrypted);


		//		3) 0508
//		String enc_ordr_idx = "";
//		try {
//			String AES256_KEY = "e49f78f36697460fb93efdecdcde804a";
//			String ordr_idxx = "pay202002060000166";
//			if(AES256_KEY != null && !AES256_KEY.equals("") && ordr_idxx != null && !ordr_idxx.equals("") ){
//				byte[] keyData = AES256_KEY.getBytes();
//				byte[] text = ordr_idxx.getBytes("UTF-8");
//				// AES/ECB/PKCS5Padding
//				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//				cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyData, "AES"));
//
//				// 암호화
//				byte[] encrypted = cipher.doFinal(text);
//				enc_ordr_idx = new String(Base64.encodeBase64(encrypted));
//				System.out.println(enc_ordr_idx);
//			}else {
//				enc_ordr_idx = "";
//			}
//		} catch (Exception e) {
//			enc_ordr_idx = "";
//		}
//
//		String date = "20180910";
//		String result = addDate(date,1,12,1);
//		System.out.println(result);

		Test_AES256_Encrypt();

	}
	
	public static void Test_AES256_Encrypt() {
		String AES256_KEY = "B0CB4jFJBjJAiRl+o5QINrcdogGU3zMN"; //random 키 평문
		String str = "{\"hash_key\":\"aa5a752cbc609fa9ec9f0d51dcce84b64e85194631ae2060d35abef87b70b5b5\",\"taxagent_biz_no\":\"2158623424\",\"suimcheo_biz_no\":\"4438701231\",\"smarta_company_name\":\"더존강촌상사\",\"client_keylock\":\"171026RNSM1000502780\"}"; //세션 아이디

		AES256_Encrypt_ECB(AES256_KEY, str);
	}


	private static String addDate(String dt, int y, int m, int d) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		Date date = format.parse(dt);
		cal.setTime(date);
		cal.add(Calendar.YEAR, y); //년 더하기
		cal.add(Calendar.MONTH, m); //년 더하기
		cal.add(Calendar.DATE, d-1); //년 더하기
		return format.format(cal.getTime());
	}


	public static String AES256_Encrypt_ECB(String AES256_KEY, String str) {

		String encStr = "";

		try {
			byte[] keyData = AES256_KEY.getBytes();
			byte[] text = str.getBytes("UTF-8");

			// AES/ECB/PKCS5Padding
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyData, "AES"));

			// 암호화
			byte[] encrypted = cipher.doFinal(text);
			encStr = new String(Base64.encodeBase64(encrypted));
		} catch (Exception e) {
//			logger.error(e);
			encStr = null;
		}

		System.out.println("암호화 결과 : " + encStr);

		return encStr;
	}


	// RSA 암호화
	public static String encryptRSA(String text, String publicKeyStr) throws Exception {
		// 전달받은 공개키 문자열을 공개=키 객체로 변환
		StringToPublicKey(publicKeyStr);

		// RSA 암호화
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytePlain = cipher.doFinal(text.getBytes()); // utf-8 문자열을 바이너리로 변환하여 사용

		// RSA 암호화 데이터를 Base64 인코딩하여, 문자열로 변환
		String encrypted = Base64.encodeBase64String(bytePlain);

		return encrypted;
	}

	// RSA 복호화
	public static String decryptRSA(String encrypted, String privateKeyStr) throws Exception {
		// 전달받은 개인키 문자열을 개인키 객체로 변환
		StringToPrivateKey(privateKeyStr);

		// RSA 복호화
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytePlain = cipher.doFinal(Base64.decodeBase64(encrypted)); // 암호화된 문자열은 Base64 인코딩 됬으므로 디코딩하여 사용

		// RSA 복호화 데이터를 utf-8 인코딩하여, 문자열로 변환
		String decrypted = new String(bytePlain, "utf-8");

		return decrypted;
	}

	// Base64 인코딩된 공개키를 PublicKey 객체로 변환
	public static void StringToPublicKey(String publicKeyStr) throws Exception {
		// 공개키 문자열은 base64 인코딩 됬으므로 디코딩하여 사용
		KeyFactory ukeyFactory = KeyFactory.getInstance("RSA");

		// 인코딩 대상 : 공개 키(X509EncodedSpec)
		X509EncodedKeySpec ukeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));

		// 공개키 객체 생성
		publicKey = ukeyFactory.generatePublic(ukeySpec);
	}

	// Base64 인코딩된 개인키를 PrivateKey 객체로 변환
	public static void StringToPrivateKey(String privateKeyStr) throws Exception {
		// 개인키 문자열은 base64 인코딩 됬으므로 디코딩하여 사용
		KeyFactory rkeyFactory = KeyFactory.getInstance("RSA");

		// 인코딩 대상 : 개인 키(PKCS8EncodedKeySpec)
		PKCS8EncodedKeySpec rkeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr));

		// 개인키 객체 생
		privateKey = rkeyFactory.generatePrivate(rkeySpec);
	}

	// RSA 공개키, 개인키 생성
	public static void createRSAKeys() throws Exception {
		// 필요한 객체 생성
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");

		// 2048 bits로 키 생성
		gen.initialize(2048, secureRandom);

		// 공개키, 개인키 생성
		KeyPair keyPair = gen.genKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();

		String base64PublicKey = Base64.encodeBase64String(publicKey.getEncoded());
		String base64PrivateKey = Base64.encodeBase64String(privateKey.getEncoded());
		System.out.println("PublicKey : " + base64PublicKey);
		System.out.println("PrivateKey : " + base64PrivateKey);
	}

}

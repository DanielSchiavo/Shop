package br.com.danielschiavo.filestorage;

import java.util.Base64;

public class LocalStorageUtil {

	public static byte[] encodeToBase64(byte[] bytes) {
		return Base64.getEncoder().encode(bytes);
	}
	
	public static byte[] decodeFromBase64(byte[] bytesBase64) {
		return Base64.getDecoder().decode(bytesBase64);
	}

}

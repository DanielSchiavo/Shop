package br.com.danielschiavo.shop.services.filestorage;

import java.util.Base64;

public class Base64Utils {

	public static String codificarParaBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	public static byte[] decodificarDeBase64(String stringBase64) {
		return Base64.getDecoder().decode(stringBase64);
	}
}

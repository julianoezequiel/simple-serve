package com.api.dto;

/**
 * 
 * @author juliano.ezequiel
 *
 */
public class TokenDTO {

	private String token;

	public TokenDTO(String token2) {
		this.token = token2;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static byte[] stringToByte(String entrada) {
		byte[] retorno = new byte[256];
		for (int i = 0; i < entrada.length(); i++) {
			retorno[i] = (byte) entrada.charAt(i);
		}
		return retorno;
	}

}

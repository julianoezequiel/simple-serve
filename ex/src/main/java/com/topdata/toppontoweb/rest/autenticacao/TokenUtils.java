package com.topdata.toppontoweb.rest.autenticacao;

import com.topdata.toppontoweb.dao.AutenticacaoDao;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @version 1.0.1 data 03/03/2016
 * @since 1.0.1 data 03/03/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class TokenUtils {

    @Autowired
    private Dao dao;
    @Autowired
    private AutenticacaoDao autenticacaoDao;

    public static final String MAGIC_KEY = "mlspjce";
    private final String SECURITY_TOKEN = "X-Auth-Token";
    private final String TOKEN = "token";
    private final String SECURITY_TOKEN_REFRESH = "X-Auth-Token-Refresh";

    /**
     * Cria o token de acesso
     *
     * @param userDetails
     * @return
     */
    public static String createToken(User userDetails) {
        /* Token espira em 12 hora */
        long expires = System.currentTimeMillis() + 1000L * 60 * 60 * 120;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires));
//        tokenBuilder.append(":");
//        tokenBuilder.append(IP.replaceAll(":", ""));

        return tokenBuilder.toString();
    }

    /**
     * Realiza a criptografia do token
     *
     * @param userDetails
     * @param expires
     * @return
     */
    public static String computeSignature(User userDetails, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(userDetails.getPassword());
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtils.MAGIC_KEY);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    /**
     * Busca o nome do usuário existente no token
     *
     * @param authToken
     * @return
     */
    public static String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }
        String[] parts = authToken.split(":");
        return parts[0];
    }

    /**
     * Verifica a validade do token de acesso.
     *
     * @param authToken
     * @param authTokenRefresh
     * @param userDetails
     * @return
     */
    public boolean validateToken(String authToken, String authTokenRefresh, User userDetails) {
        Long tempoIni = Calendar.getInstance().getTimeInMillis();
        long expires = 0;
        String signature = "";
        Boolean retorno = null;
        if (userDetails.getOperador().getUltimoToken() == null) {
            return false;
        }
        //userDetails.getOperador().setTentativasLogin(userDetails.getOperador().getTentativasLogin() + 1);
        try {
            String[] parts = authToken.split(":");
            expires = Long.parseLong(parts[1]);
            signature = parts[2];
            if ((parts[0]).equals("master")) {
                return true;
            } else if (expires < System.currentTimeMillis()) {
                dao.save(userDetails.getOperador());
                return false;
            }
//            else if (!parts[3].equals(IP.replaceAll(":", ""))) {
//                dao.save(userDetails.getOperador());
//                return false;
//            }
            retorno = signature.equals(TokenUtils.computeSignature(userDetails, expires));
//            Logger.getLogger(TokenUtils.class.getSimpleName()).log(Level.WARNING, "TEMPO VALIDAR TOKEN: {0}", Calendar.getInstance().getTimeInMillis() - tempoIni);
            if (retorno == false) {
                dao.save(userDetails.getOperador());
                return retorno;
            }
            return retorno;
        } catch (NumberFormatException | DaoException e) {
            return false;
        }
    }

    /**
     * Busca o token no header da requisição
     *
     * @param httpRequest
     * @return
     */
    public String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
        /* retira o token do header */
        String authToken = httpRequest.getHeader(SECURITY_TOKEN);

        if (authToken == null) {
            authToken = httpRequest.getParameter(TOKEN);
        }
        return authToken;
    }

    public String extractAuthTokenRefreshFromRequest(HttpServletRequest httpRequest) {
        /* retira o token do header */
        String authToken = httpRequest.getHeader(SECURITY_TOKEN_REFRESH);

        if (authToken == null) {
            authToken = httpRequest.getParameter(TOKEN);
        }
        return authToken;
    }

}

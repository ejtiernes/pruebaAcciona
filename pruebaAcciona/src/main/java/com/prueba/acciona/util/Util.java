package com.prueba.acciona.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class Util {

	private static Logger logger = Logger.getLogger(Util.class.getName());

	private static final String CONSUMER_KEY = "XXXXXXXXXXXXX";
	private static final String CONSUMER_SECRET = "XXXXXXXXXXXXX";
	private static ConfigurationBuilder configBuilder;
	private static RequestToken requestToken = null;
	private static AccessToken accessToken = null;
	private static String url = null;

	public static ResponseList<Status> autorizar() throws IOException, TwitterException {
		configBuilder = new ConfigurationBuilder();

		configBuilder.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET);
		Twitter OAuthTwitter = new TwitterFactory(configBuilder.build()).getInstance();
		do {
			try {
				requestToken = OAuthTwitter.getOAuthRequestToken();
				logger.info("Request Tokens obtenidos con éxito.");
				logger.info("Request Token: " + requestToken.getToken());
				logger.info("Request Token secret: " + requestToken.getTokenSecret());
				url = requestToken.getAuthorizationURL();
				logger.info("URL:");
				logger.info(requestToken.getAuthorizationURL());
			} catch (TwitterException ex) {
				Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
			}
			BufferedReader lectorTeclado = new BufferedReader(new InputStreamReader(System.in));
			// Abro el navegador. Firefox, en este caso.
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("chrome " + url);
			} catch (Exception e) {
			}
			// Nos avisa de que introduciremos el PIN a continuación
			logger.info("Introduce el PIN del navegador y pulsa intro.nn PIN: ");
			// Leemos el PIN
			String pin = lectorTeclado.readLine();
			if (pin.length() > 0) {
				accessToken = OAuthTwitter.getOAuthAccessToken(requestToken, pin);
			} else {
				accessToken = OAuthTwitter.getOAuthAccessToken(requestToken);
			}
		} while (accessToken == null);

		Paging pagina = new Paging();
		pagina.setCount(50);
		ResponseList<Status> listado = OAuthTwitter.getHomeTimeline(pagina);
		return listado;
	}
}
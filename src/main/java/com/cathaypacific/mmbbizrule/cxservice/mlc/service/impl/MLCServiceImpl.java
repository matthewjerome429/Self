package com.cathaypacific.mmbbizrule.cxservice.mlc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.config.MLCConfig;
import com.cathaypacific.mmbbizrule.constant.MLCConstants;
import com.cathaypacific.mmbbizrule.cxservice.mlc.model.MLCSession;
import com.cathaypacific.mmbbizrule.cxservice.mlc.model.Session;
import com.cathaypacific.mmbbizrule.cxservice.mlc.service.MLCService;

@Service
public class MLCServiceImpl implements MLCService{
	
	private static LogAgent logger = LogAgent.getLogAgent(MLCServiceImpl.class);
	@Autowired
	private MLCConfig mlcConfig;
	
	@Override
	public MLCSession verifyToken(final String mlcToken) throws  IOException, SAXException, ParserConfigurationException, KeyManagementException, NoSuchAlgorithmException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
		final String tokenURL = mlcConfig.getCxDomain() + mlcConfig.getVerifyTokenUrl();
		//LOGGER.info("verifyMLCToken:", "TokenURL:" + tokenURL);
		final StringBuilder strUrl = new StringBuilder();
		strUrl.append(tokenURL);
		strUrl.append(MLCConstants.URL_APPEND_TOKEN);
		strUrl.append(URLEncoder.encode(mlcToken, MLCConstants.ENCODING_STANDARD));
		logger.info("verifyMLCToken:", "strUrl:" + strUrl.toString());
		final URL url = new URL(strUrl.toString());
		
		return connectMLCForTokenVerification(url);
		
	}
	
	private MLCSession readXmlFromMlc(final InputStream xmlContentFromMLC)
			throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
		logger.debug("readXmlFromMlc START");
		final MLCSession userSession = new MLCSession();
		final Session userSubSession = new Session();
		/* DOM Parser Initiation for reading XML from MLC */
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		final Document doc = dBuilder.parse(xmlContentFromMLC);
		doc.getDocumentElement().normalize();
		StringWriter output = new StringWriter();
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));
		//System.out.println(output.toString());
		logger.info("doWithMessage", output.toString(), output.toString().getBytes().length, "mlc response");
		userSession.setStatusCode(Integer.parseInt(doc.getElementsByTagName(MLCConstants.STATUS_CODE)
				.item(MLCConstants.ZERO).getChildNodes().item(MLCConstants.ZERO).getNodeValue()));
		if (userSession.getStatusCode() != MLCConstants.INVALID_TOKEN_STATUS_CODE) {
			userSubSession.setIsJustLogin(doc.getElementsByTagName(MLCConstants.IS_JUST_LOGIN).item(MLCConstants.ZERO)
					.getChildNodes().item(MLCConstants.ZERO).getNodeValue());
			userSubSession.setMemID(doc.getElementsByTagName(MLCConstants.MEMBER_ID).item(MLCConstants.ZERO)
					.getChildNodes().item(MLCConstants.ZERO).getNodeValue());
			userSubSession.setPreLink(doc.getElementsByTagName(MLCConstants.PRE_LINK).item(MLCConstants.ZERO)
					.getChildNodes().item(MLCConstants.ZERO).getNodeValue());
			userSubSession.setSessionID(Integer.parseInt(doc.getElementsByTagName(MLCConstants.SESSION_ID)
					.item(MLCConstants.ZERO).getChildNodes().item(MLCConstants.ZERO).getNodeValue()));
			userSubSession.setUserType(doc.getElementsByTagName(MLCConstants.USER_TYPE).item(MLCConstants.ZERO)
					.getChildNodes().item(MLCConstants.ZERO).getNodeValue());
			userSession.setSession(userSubSession);
		}
		logger.debug("readXmlFromMlc END");
		return userSession;
	}
	
	/**
	 * This method establish connection to MLC and returns user details.
	 *
	 * @param url
	 *            - MLC URL required for token validation
	 * @return userSession Object to store user details
	 * @throws IOException
	 *             Might be thrown when connection not established
	 * @throws SAXException
	 *             Might be thrown during parsing the XML
	 * @throws ParserConfigurationException
	 *             Might be thrown during XML parsing
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws TransformerConfigurationException 
	 */
	private MLCSession connectMLCForTokenVerification(final URL url) throws IOException, ParserConfigurationException, SAXException, KeyManagementException, NoSuchAlgorithmException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
		InputStream xmlContentFromMLC = null;
		HttpsURLConnection connection = null;
		MLCSession userSession = null;
		TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1, Socket arg2)
					throws CertificateException {
				// TODO Auto-generated method stub
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1, Socket arg2)
					throws CertificateException {
				// TODO Auto-generated method stub
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2)
					throws CertificateException {
				// TODO Auto-generated method stub
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2)
					throws CertificateException {
				// TODO Auto-generated method stub
			}
		} };
	
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		logger.debug("connectMLCForTokenVerification START " + url);
		try {
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod(RequestMethod.GET.name());
			connection.setDoInput(true);
			//connection.setSSLSocketFactory(sslContext.getSocketFactory());
			connection.connect();
			xmlContentFromMLC = connection.getInputStream();
			userSession = readXmlFromMlc(xmlContentFromMLC);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			logger.debug("xmlContentFromMLC" + xmlContentFromMLC);
			if (xmlContentFromMLC != null) {
				xmlContentFromMLC.close();
			}
		}
		logger.debug("connectMLCForTokenVerification END");
		return userSession;
	}
}

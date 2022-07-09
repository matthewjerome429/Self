package com.cathaypacific.mmbbizrule.util.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;


/**
 * This class is used to load the KeyStore file from the JVM classpath and from within it fetch individual keys based on the application which the request contains.
 *
 * @author ashok.ramalingam
 */
@Component
public class KeyStoreReader {
	private static LogAgent LOGGER = LogAgent.getLogAgent(KeyStoreReader.class);
	private static final char[] PASSWORD = { 'h', 'y', 'p', 'e', 'r', 'i', 'o', 'n' };
	private KeyStore cepKeyStore;
	private KeyStore olssKeyStore;

	// /**
	// * The PropertiesConfiguration object into which the key store file name and type are loaded and read.
	// */
	// private final PropertiesConfiguration config;

	@Value("${mmb.cepKeystoreFilePath}")
	private String cepKeyStoreFilePath;
	
	@Value("${mmb.cepKeystoreType}")
	private String cepKeyStoreType;
	
	@Value("${mmb.olssKeystoreFilePath}")
	private String olssKeyStoreFilePath;
	
	@Value("${mmb.olssKeystoreType}")
	private String olssKeyStoreType;

	/**
	 * Getter method for the keyStore object.
	 *
	 * @return The keyStore object
	 */
	public KeyStore getKeyStore() {
		return this.cepKeyStore;
	}

	/**
	 * Searches the classpath for the parameterized file name, and returns it if one is found to exist. Else, it throws a {@link FileNotFoundException}.
	 *
	 * @param fileName
	 *            - The file name
	 * @return A {@link Resource} object which represents the file
	 * @throws FileNotFoundException
	 *             If the file does not exist in the classpath
	 */
//	private Resource getResource(final String fileName) throws FileNotFoundException {
//		Resource resource = new ClassPathResource(fileName);
//
//		if (!resource.exists()) {
//			throw new FileNotFoundException("Unable to locate '" + fileName + "' within the classpath");
//		}
//
//		return resource;
//	}

	@PostConstruct
	private void initKeyStore() throws UnexpectedException {
		this.cepKeyStore = loadKeyStore(cepKeyStoreFilePath, cepKeyStoreType);
		this.olssKeyStore = loadKeyStore(olssKeyStoreFilePath, olssKeyStoreType);
	}
	
	/**
	 * Convenience method which reads the key store file and creates the KeyStore object.
	 * <p/>
	 * If any issues occur while creating the KeyStore, or while loading the keys from the key store file, a SystemException is thrown by this method.
	 *
	 * @param keyStoreFilePath
	 *            - The file path from which the keys are to be loaded into the KeyStore
	 * @param keyStoreType
	 *            - The type of KeyStore
	 * @return A KeyStore object with the keys loaded from the key store file
	 * @throws UnexpectedException 
	 */
	private KeyStore loadKeyStore(final String keyStoreFilePath, final String keyStoreType) throws UnexpectedException {
		LOGGER.debug("Initializing the KeyStoreReader. Loading the KeyStore type:" + keyStoreType + " of file:" + keyStoreFilePath);
		KeyStore tempKeyStore = null;
		InputStream keyStoreFileInputStream = null;

		try {
			LOGGER.debug("Loading the keystore file from the system classpath");
			keyStoreFileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStoreFilePath);

			LOGGER.debug("Key store file loaded into memory. Creating the KeyStore object");
			tempKeyStore = KeyStore.getInstance(keyStoreType);

			LOGGER.debug("KeyStore object created. Loading keys from the file/stream");
			tempKeyStore.load(keyStoreFileInputStream, PASSWORD);
			LOGGER.debug("Loaded the keys successfully into the KeyStore object");
		} catch (KeyStoreException keyStoreExp) {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException(keyStoreExp.getMessage(), errorInfo);
		} catch (NoSuchAlgorithmException noAlogrithmExp) {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException(noAlogrithmExp.getMessage(), errorInfo);
		} catch (CertificateException certificationExp) {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException(certificationExp.getMessage(), errorInfo);
		} catch (IOException ioEx) {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException(ioEx.getMessage(), errorInfo);
		} catch (Exception exception) {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException(exception.getMessage(), errorInfo);
		} finally {
			if (keyStoreFileInputStream != null) {
				try {
					keyStoreFileInputStream.close();
				} catch (IOException ioEx2) {
					ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
					throw new UnexpectedException(ioEx2.getMessage(), errorInfo);
				}
			}
		}

		LOGGER.debug("Finished initializing the KeyStoreReader after loading the KeyStore");
		return tempKeyStore;
	}

	/**
	 * This method fetches the SecretKey object from the KeyStore based on the parameterized key name. The key name is the same as the "alias" used while storing/importing the SecretKey in the key store.
	 * <p/>
	 * If the key name provided is null, an IllegalArgumentException is thrown.
	 *
	 * @param cInfo
	 *            - The CorrelationInfo object used for logging purpose
	 * @param keyName
	 *            - The alias used to lookup the KeyStore. The application name is usually used as the alias (e.g. "ibe", "psc", etc.)
	 * @return The SecretKey as retreived from the KeyStore
	 * @throws BusinessException
	 *             In case of any errors while fetching the key from the KeyStore
	 */
	public final SecretKey getKey(final String keyName, final String keyStoreName) throws UnexpectedException {
		if("cep".equalsIgnoreCase(keyStoreName)) {
			return getCEPKey(keyName);
		}else if("olss".equalsIgnoreCase(keyStoreName)) {
			return getOLSSKey(keyName);
		}else {
			LOGGER.warn("KeyStoreName not found: ["+keyStoreName+"]");
			throw new IllegalArgumentException("Cannot find KeyStore file with KeyStoreName["+keyStoreName+"] while fetching the key");
		}
	}
	
	private final SecretKey getCEPKey(final String keyName) throws UnexpectedException {
		SecretKey key = null;
		if (keyName == null) {
			LOGGER.warn("Key name cannot be null while fetching the key");
			throw new IllegalArgumentException("Key name cannot be null while fetching the key");
		}

		synchronized (cepKeyStore) {
			try {
				key = (SecretKey) cepKeyStore.getKey(keyName, PASSWORD);
				LOGGER.debug("Is the key/alias '" + keyName + "' available: " + (key != null));
			} catch (Exception exp) {
				ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
				throw new UnexpectedException("Error occurred while fetching the key. Error:" + exp.getMessage(), errorInfo);
			}
		}

		return key;
	}
	
	private final SecretKey getOLSSKey(final String keyName) throws UnexpectedException {
		SecretKey key = null;
		if (keyName == null) {
			LOGGER.warn("Key name cannot be null while fetching the key");
			throw new IllegalArgumentException("Key name cannot be null while fetching the key");
		}

		synchronized (olssKeyStore) {
			try {
				key = (SecretKey) olssKeyStore.getKey(keyName, PASSWORD);
				LOGGER.debug("Is the key/alias '" + keyName + "' available: " + (key != null));
			} catch (Exception exp) {
				ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
				throw new UnexpectedException("Error occurred while fetching the key. Error:" + exp.getMessage(), errorInfo);
			}
		}

		return key;
	}

	/**
	 * Main method to test the class' operations.
	 *
	 * @param args
	 *            - The arguments passed to the main method
	 */
	public static void main(final String[] args) {
		try {
			KeyStoreReader reader = new KeyStoreReader();
			LOGGER.debug("KeyStoreReader: " + toString(reader));

			KeyStore store = reader.getKeyStore();
			LOGGER.debug("KeyStore: " + toString(store));

			Enumeration<String> aliases = store.aliases();
			LOGGER.debug("KeyStore Aliases: " + toString(aliases));

			while (aliases.hasMoreElements()) {
				String keyName = aliases.nextElement();
				LOGGER.debug("\t Key Name: " + keyName);
				SecretKey key = reader.getKey(keyName, "cep");
				LOGGER.debug("\t Key: " + key);
				LOGGER.debug("\t Key Algorithm: " + key.getAlgorithm());
				LOGGER.debug("\t Key Format: " + key.getFormat());
				LOGGER.debug("\t Key Class Name: " + key.getClass().getName());
			}
		} catch (KeyStoreException keyStoreException) {
			LOGGER.warn(keyStoreException.getMessage(), keyStoreException);
		} catch (UnexpectedException unexpectedException) {
			LOGGER.warn(unexpectedException.getMessage(), unexpectedException);
		} catch (Exception exception) {
			LOGGER.warn(exception.getMessage(), exception);
		}
	}

	/**
	 * Convenience method which uses the Apache Commons API provided ToStringBuilder's reflection based builder to generate the value object's equivalent representation in String format.
	 * <p/>
	 * If it throws an exception, mainly due to security permissions setup, the value returned by the object's toString() method is used.
	 *
	 * @param value
	 *            - The object for which the string representation is required
	 * @return A string based representation of the object
	 */
	public static String toString(final Object value) {
		String response = null;

		try {
			response = ToStringBuilder.reflectionToString(value);
		} catch (Exception exception) {
			response = String.valueOf(value);
		}

		return response;
	}
}

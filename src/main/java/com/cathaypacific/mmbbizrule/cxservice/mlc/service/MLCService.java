package com.cathaypacific.mmbbizrule.cxservice.mlc.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.cathaypacific.mmbbizrule.cxservice.mlc.model.MLCSession;

public interface MLCService {
	public MLCSession verifyToken(String mlcToken) throws UnsupportedEncodingException, MalformedURLException,
			KeyManagementException, NoSuchAlgorithmException, IOException, SAXException, ParserConfigurationException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError;
}

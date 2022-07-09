package com.cathaypacific.mmbbizrule.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import com.cathaypacific.mbcommon.loging.LogAgent;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

public class PDFUtil {
	
	private static LogAgent logger = LogAgent.getLogAgent(PDFUtil.class);

	private PDFUtil(){}

	/**
	 * Necessary to set PDF font cache when start application.
	 * @param cachePath
	 */
	public static void setPDFBoxCache(String cachePath) {
		System.setProperty("pdfbox.fontcache", cachePath);
	}
	
	/**
	 * Fill in values to the specified PDF template. The template must have correspondent text field id in the textFieldValues.
	 * @param templatePath - the template must be a PDF files
	 * @param textFieldValues
	 * @return byte[]
	 * @throws InvalidPasswordException 
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static OutputStream fillInPdfTemplate(InputStream pdfTemplate, Map<String, Object> textFieldValues) throws IOException {
		// Read the PDF
		PDDocument pdfDoc = readPdf(pdfTemplate);

		// Fill in acro form
		fillAcroForm(pdfDoc, textFieldValues);

        // Disable edit fields in output
		disableEditField(pdfDoc);

        // Save PDF as byte array
		OutputStream pdfResult = toOutputStream(pdfDoc);

		// Close
		pdfDoc.close();
		
        return pdfResult;
	}
	
	/**
	 * Call this can make the field non-editable in the generated PDF
	 * @param pdfDoc
	 * @throws IOException
	 */
	public static void disableEditField(PDDocument pdfDoc) throws IOException {
		pdfDoc.getDocumentCatalog().getAcroForm().flatten();
	}
	
	
	/**
	 * Fill in acro field of PDF files
	 * @param pdfDoc - The pdf you want to fill in
	 * @param textFieldValues - the map with key-value pair where key is acro field name
	 * @throws IOException
	 */
	public static void fillAcroForm(PDDocument pdfDoc, Map<String, Object> textFieldValues) throws IOException {
        PDAcroForm acroForm = pdfDoc.getDocumentCatalog().getAcroForm();
        for (Map.Entry<String, Object> entry : textFieldValues.entrySet()) {
        	PDField field = acroForm.getField(entry.getKey());
        	if (field != null) {
        		if(entry.getValue() instanceof String) {
        			field.setValue((String) entry.getValue());        			
        		} else if(entry.getValue() instanceof Boolean && BooleanUtils.isTrue((Boolean) entry.getValue())){
        			((PDCheckBox) field).check();
        		}
        	} else {
        		logger.warn(String.format("Acro field %s cannot be found in the PDF template. The field is ignored.", entry.getKey()));
        	}
        }
	}
	
	/**
	 * Read the pdf file as a template
	 * @param pdfTemplate
	 * @return PDDocument
	 * @throws IOException
	 */
	public static PDDocument readPdf(InputStream pdfTemplate) throws IOException {
		// Use temp file instead of memory for less memory usage
		return PDDocument.load(pdfTemplate, MemoryUsageSetting.setupTempFileOnly());
	}
	
	
	/**
	 * Convery PDF doc to output stream so that you can use this as the generated PDF
	 * @param doc - the PDF you want to output
	 * @return OutputStream
	 * @throws IOException
	 */
	public static OutputStream toOutputStream(PDDocument doc) throws IOException {
		// Wrap it in buffered output stream for less memory usage
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        doc.save(bufferedOutputStream);
        
        // Return byte stream because we want to retrieve the bytes later
        return byteArrayOutputStream;
	}
}

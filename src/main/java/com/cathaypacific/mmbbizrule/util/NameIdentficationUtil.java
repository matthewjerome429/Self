package com.cathaypacific.mmbbizrule.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;

public class NameIdentficationUtil {

	/**
	 * Identify the input familyName and givenName by pax list 
	 * 
	 * @param familyName
	 * @param givenName
	 * @param paxs
	 * @return matchedPax
	 * @throws BusinessBaseException
	 * @author haiwei.jia
	 */
	public static  List<String> nameIdentification(String familyName, String givenName,
			Map<String, String[]> paxMap, List<String> nameTitleList, Integer shortCompareSize) {
		// familyName and givenName without space
		String allSpaceTrimedInputFamilyName = BizRulesUtil.removeSpecialCharactersFromStr(replaceName(familyName));
		String allSpaceTrimedInputGivenName  = BizRulesUtil.removeSpecialCharactersFromStr(replaceName(givenName));

		// matching result
		List<String> nameMatchingResultList = new ArrayList<>();

		// start matching
		for (Map.Entry<String, String[]> entry : paxMap.entrySet()) {
			// familyName and givenName in pax without space
			String allSpaceTrimedPnrGivenName =BizRulesUtil.removeSpecialCharactersFromStr( replaceName(entry.getValue()[0]));
			String allSpaceTrimedPnrFamilyName = BizRulesUtil.removeSpecialCharactersFromStr(replaceName(entry.getValue()[1])); 
			// compare familyName
			if (allSpaceTrimedPnrFamilyName.equalsIgnoreCase(allSpaceTrimedInputFamilyName)) {
				// if the length of givenName is bigger than 4, then compare the
				// first 4 characters
				if (allSpaceTrimedPnrGivenName.length() >= shortCompareSize
						&& allSpaceTrimedInputGivenName.length() >= shortCompareSize) {
					if (allSpaceTrimedPnrGivenName.substring(0, shortCompareSize)
							.equalsIgnoreCase(allSpaceTrimedInputGivenName.substring(0, shortCompareSize))) {
						nameMatchingResultList.add(entry.getKey());
					}
				}
				// if the length of givenName is not bigger than 4, compare the
				// whole String
				else {
					if (allSpaceTrimedPnrGivenName.equalsIgnoreCase(allSpaceTrimedInputGivenName)) {
						nameMatchingResultList.add(entry.getKey());
					}
					// get rid of the title and then compare
					else {
						if (compareWithoutTitle(entry.getValue()[0], givenName, nameTitleList)) {
							nameMatchingResultList.add(entry.getKey());
						}
					}
				}
			}
		}
		
		if(nameMatchingResultList.size() >1){
			List<String> accurateMatchedPax = new ArrayList<>();
			for (String pax : nameMatchingResultList) {
				if (compareWithoutTitle(paxMap.get(pax)[0], givenName, nameTitleList)) {
					accurateMatchedPax.add(pax);
				}
			}
			nameMatchingResultList = (accurateMatchedPax.size()==1?accurateMatchedPax:nameMatchingResultList);
		}
		
			return nameMatchingResultList;
	}
	
	/**
	 * Remove the title and compare the two given names
	 * 
	 * @param pnrGivenName
	 * @param inputGivenName
	 * @param nameTitleList
	 * @return compareResult
	 * @author haiwei.jia
	 */
	public static boolean compareWithoutTitle(String pnrGivenName, String inputGivenName, List<String> nameTitleList) {
		String allSpaceTrimedInputGivenName = inputGivenName.replace(" ", "");
		String allSpaceTrimedPnrGivenName = pnrGivenName.replace(" ", "");
		
		//if the lengths of the two given names are the same, then compare them
		if (allSpaceTrimedPnrGivenName.length() == allSpaceTrimedInputGivenName.length()) {
			return allSpaceTrimedPnrGivenName.equalsIgnoreCase(allSpaceTrimedInputGivenName);
		} 
		//if PnrGivenName is longer than InputGivenName,remove the title in PnrGivenName and compare them
		else if (allSpaceTrimedPnrGivenName.length() > allSpaceTrimedInputGivenName.length()) {
			return getRidOfTitle(pnrGivenName, nameTitleList).replace(" ", "")
					.equalsIgnoreCase(allSpaceTrimedInputGivenName);
		} 
		//if InputGivenName is longer than PnrGivenName,return false
		else {
			return false;
		}
	}

	/**
	 * Remove the title in the given name
	 * 
	 * @param givenName
	 * @param nameTitleList
	 * @return givenName
	 * @author haiwei.jia
	 */
	private  static String getRidOfTitle(String givenName, List<String> nameTitleList) {
		String[] strParts = givenName.split(" ");

		if (strParts.length > 1) {
			String endingStr = strParts[strParts.length - 1];
			for (String title : nameTitleList) {
				if (endingStr.equalsIgnoreCase(title)) {
					return givenName.substring(0, givenName.length() - title.length()).trim();
				}
			}
		} else {
			for (String title : nameTitleList) {
				if (givenName.toUpperCase().endsWith(title.toUpperCase())) {
					return givenName.substring(0, givenName.length() - title.length()).trim();
				}
			}
		}
		return givenName;
	}
	
	/**
	 * Replace the name as white space if null
	 * @param name
	 * @return
	 */
	private static String replaceName(String name) {
		 
		return name == null ? StringUtils.EMPTY : name;
	}

}

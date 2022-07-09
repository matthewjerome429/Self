package com.cathaypacific.mmbbizrule.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class NameIdentficationUtilTest {

	@Test
	public void nameIdentificationLongGivenNamePnrTitleWithoutSpace() throws Exception {
		String familyName = "TEST";
		String givenName = "HELLO";
		String[] nameArray = new String[] { "HELLOMR", "TEST" };
		
		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MR");
		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);

		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(1, matchedPaxIdList.size());
	}

	@Test
	public void nameIdentificationShortGivenNamePnrTitleWithoutSpace() throws Exception {
		String familyName = "TEST";
		String givenName = "JON";
		String[] nameArray = new String[] { "JONMR", "TEST" };

		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MR");
		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);
		
		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(1, matchedPaxIdList.size());
	}

	@Test
	public void nameIdentificationShortGivenNamePnrTitleWithSpace() throws Exception {
		String familyName = "TEST";
		String givenName = "JOMR";
		String[] nameArray = new String[] { "JO MR", "TEST" };

		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MR");

		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);
		
		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(1, matchedPaxIdList.size());
	}

	@Test
	public void nameIdentificationShortGivenNameLongTitlePnrTitleWithSpace() throws Exception {
		String familyName = "TEST";
		String givenName = "YUMISS";
		String[] nameArray = new String[] { "YU MISS", "TEST" };

		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MISS");

		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);
		
		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(1, matchedPaxIdList.size());
	}
	
	@Test
	public void nameIdentificationShortGivenNameLongTitlePnrWithoutTitle() throws Exception {
		String familyName = "TEST";
		String givenName = "YUMISS";
		String[] nameArray = new String[] { "YU", "TEST" };

		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MISS");

		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);
		
		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(0, matchedPaxIdList.size());
	}
	
	@Test
	public void nameIdentificationShortGivenNameLongTitleInputHalfTitle() throws Exception {
		String familyName = "TEST";
		String givenName = "YUMI";
		String[] nameArray = new String[] { "YU", "TEST" };

		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MISS");

		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);
		
		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(0, matchedPaxIdList.size());
	}

	@Test
	public void nameIdentificationShortGivenNameShortTitlePnrDuplicatedTitle() throws Exception {
		String familyName = "TEST";
		String givenName = "RKMR";
		String[] nameArray = new String[] { "RKMR MR", "TEST" };

		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MR");

		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);
		
		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(1, matchedPaxIdList.size());
	}

	@Test
	public void nameIdentificationShortGivenNameShortTitlePnrWithoutTitle() throws Exception {
		String familyName = "TEST";
		String givenName = "RK";
		String[] nameArray = new String[] { "RKMR", "TEST" };

		Map<String, String[]> pnrNameMap = new HashMap<>();
		List<String> nameTitleList = Arrays.asList("MR");

		Integer shortCompareSize = 4;
		pnrNameMap.put("1", nameArray);
		
		List<String> matchedPaxIdList = 
				NameIdentficationUtil.nameIdentification(familyName, givenName, pnrNameMap, nameTitleList, shortCompareSize);
		
		Assert.assertEquals(1, matchedPaxIdList.size());
	}

}

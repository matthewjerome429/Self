package com.cathaypacific.mmbbizrule.util;

import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;

@RunWith(MockitoJUnitRunner.class)
public class FreeTextUtilTest {
	
	@Test
	public void paymentInfo_test1() throws ParseException {
		String[] strArr= FreeTextUtil.getPaymentInfoFromFreeText("PAX 160-2366992330/DTCX/NZD5477.55/17JUL18/AKLCX08AA/24395114");
		Assert.assertEquals("NZD",strArr[0]);
		Assert.assertEquals("5477.55",strArr[1]);
		Assert.assertEquals("17JUL18",strArr[2]);
		Assert.assertEquals("AKLCX08AA",strArr[3]);
	}
	
	@Test
	public void paymentInfo_test2() throws ParseException {
		String[] strArr= FreeTextUtil.getPaymentInfoFromFreeText("PAX 160-2366992330/DTCX/NZD5477/17JUL18/AKLCX08AA/24395114");
		Assert.assertEquals("NZD",strArr[0]);
		Assert.assertEquals("5477",strArr[1]);
		Assert.assertEquals("17JUL18",strArr[2]);
		Assert.assertEquals("AKLCX08AA",strArr[3]);
	}
	
	@Test
	public void os_site_test_mached() throws ParseException {
		String[] siteInfos = FreeTextUtil.parseOSISiteInfo("BOOKING SITE US");
		Assert.assertEquals("US",siteInfos[0]);
	}
	@Test
	public void os_site_test_mached_2() throws ParseException {
		String[] siteInfos = FreeTextUtil.parseOSISiteInfo("BOOKING SITE US TEST");
		Assert.assertEquals("US",siteInfos[0]);
	}
	@Test
	public void os_site_test_matched_non_us() throws ParseException {
		String[] siteInfos = FreeTextUtil.parseOSISiteInfo("BOOKING SITE HK");
		Assert.assertEquals("HK",siteInfos[0]);
	}
	@Test
	public void os_site_test_no_match() throws ParseException {
		String[] siteInfos = FreeTextUtil.parseOSISiteInfo("BOOKINGS SITE US");
		Assert.assertTrue(siteInfos.length == 0);
	}
	@Test
	public void os_site_test_no_match_2() throws ParseException {
		String[] siteInfos = FreeTextUtil.parseOSISiteInfo("a BOOKING SITE USA");
		Assert.assertTrue(siteInfos.length == 0);
	}
	@Test
	public void rm_onhold_test_matched() throws ParseException {
		String[] strs= FreeTextUtil.parserOnHoldText("STATUS:ON HOLD/24JUN2018 1049/27JUN2018 1100");
		Assert.assertNull(strs[0]);
		Assert.assertNull(strs[1]);
	}
	
	@Test
	public void rm_onhold_test_matched_2() throws ParseException {
		String[] strs= FreeTextUtil.parserOnHoldText("STATUS:ON HOLD/FEE25.00 AUD/18SEP2018 2010/21SEP2018 2030");
		Assert.assertEquals("25.00",strs[0]);
		Assert.assertEquals("AUD",strs[1]);
	}
	
	@Test
	public void rm_onhold_test_matched_3() throws ParseException {
		String[] strs= FreeTextUtil.parserOnHoldText("STATUS:ON HOLD/FEE25.00/18SEP2018 2010/21SEP2018 2030");
		Assert.assertEquals("25.00",strs[0]);
		Assert.assertNull(strs[1]);
	}
	
	@Test
	public void rm_onhold_test_no_match() throws ParseException {
		String[] strs= FreeTextUtil.parserOnHoldText("STATUS:ON AHOLD/24JUN2018 1049/27JUN2018 1100");
		Assert.assertEquals(0, strs.length);
	}
	
	@Test
	public void test_eticket_freetext() {
		/** Code for reference
		 *  String number = matcher.group(E_TICKET_FREE_TEXT_NUMBER_GROUP);
			String range = matcher.group(E_TICKET_FREE_TEXT_RANGE_GROUP);
			List<String> eTickets = getETicketNumbers(number, range);
			String date = matcher.group(E_TICKET_FREE_TEXT_DATE_GROUP);
			String passengerType = matcher.group(E_TICKET_FREE_TEXT_PASSENGER_TYPE_GROUP);
			String currency = matcher.group(E_TICKET_FREE_TEXT_CURRENCY_GROUP);
			String amount = matcher.group(E_TICKET_FREE_TEXT_AMOUNT_GROUP);
			return new Object[]{eTickets, date, passengerType, currency, amount, officalId};
		 */
		
		String case1 = "PAX 160-2372032016";
		Object[] eTicketInfos = FreeTextUtil.getETicketInfoFromFreeText(case1);
		Assert.assertEquals(((List<String>)eTicketInfos[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos[1], null);
		Assert.assertEquals(eTicketInfos[2], "PAX");
		Assert.assertEquals(eTicketInfos[3], null);
		Assert.assertEquals(eTicketInfos[4], null);
		Assert.assertEquals(eTicketInfos[5], null);
		
		String case2 = "PAX 160-2372032098-03/ETCX";
		Object[] eTicketInfos2 = FreeTextUtil.getETicketInfoFromFreeText(case2);
		Assert.assertEquals(((List<String>)eTicketInfos2[0]).get(0), "1602372032098");
		Assert.assertEquals(((List<String>)eTicketInfos2[0]).get(1), "1602372032099");
		Assert.assertEquals(((List<String>)eTicketInfos2[0]).get(2), "1602372032100");
		Assert.assertEquals(((List<String>)eTicketInfos2[0]).get(3), "1602372032101");
		Assert.assertEquals(((List<String>)eTicketInfos2[0]).get(4), "1602372032102");
		Assert.assertEquals(((List<String>)eTicketInfos2[0]).get(5), "1602372032103");
		Assert.assertEquals(((List<String>)eTicketInfos2[0]).size(), 6);
		Assert.assertEquals(eTicketInfos2[1], null);
		Assert.assertEquals(eTicketInfos2[2], "PAX");
		Assert.assertEquals(eTicketInfos2[3], null);
		Assert.assertEquals(eTicketInfos2[4], null);
		Assert.assertEquals(eTicketInfos2[5], null);
		
		String case3 = "PAX 160-2372032016/ETCX/HKD4019";
		Object[] eTicketInfos3 = FreeTextUtil.getETicketInfoFromFreeText(case3);
		Assert.assertEquals(((List<String>)eTicketInfos3[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos3[1], null);
		Assert.assertEquals(eTicketInfos3[2], "PAX");
		Assert.assertEquals(eTicketInfos3[3], "HKD");
		Assert.assertEquals(eTicketInfos3[4], "4019");
		Assert.assertEquals(eTicketInfos3[5], null);
		
		
		String case4 = "PAX 160-2372032016/ETCX/HKD4019.20";
		Object[] eTicketInfos4 = FreeTextUtil.getETicketInfoFromFreeText(case4);
		Assert.assertEquals(((List<String>)eTicketInfos4[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos4[1], null);
		Assert.assertEquals(eTicketInfos4[2], "PAX");
		Assert.assertEquals(eTicketInfos4[3], "HKD");
		Assert.assertEquals(eTicketInfos4[4], "4019.20");
		Assert.assertEquals(eTicketInfos4[5], null);
		
		String case5 = "PAX 160-2372032016/ETCX/HKD4019/03MAY19";
		Object[] eTicketInfos5 = FreeTextUtil.getETicketInfoFromFreeText(case5);
		Assert.assertEquals(((List<String>)eTicketInfos5[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos5[1], "03MAY19");
		Assert.assertEquals(eTicketInfos5[2], "PAX");
		Assert.assertEquals(eTicketInfos5[3], "HKD");
		Assert.assertEquals(eTicketInfos5[4], "4019");
		Assert.assertEquals(eTicketInfos5[5], null);
		
		String case6 = "PAX 160-2372032016/ETCX/HKD4019.20/03MAY19";
		Object[] eTicketInfos6 = FreeTextUtil.getETicketInfoFromFreeText(case6);
		Assert.assertEquals(((List<String>)eTicketInfos6[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos6[1], "03MAY19");
		Assert.assertEquals(eTicketInfos6[2], "PAX");
		Assert.assertEquals(eTicketInfos6[3], "HKD");
		Assert.assertEquals(eTicketInfos6[4], "4019.20");
		Assert.assertEquals(eTicketInfos6[5], null);
		
		String case7 = "PAX 160-2372032016/ETCX/HKD4019/03MAY19/HKGCX08AA";
		Object[] eTicketInfos7 = FreeTextUtil.getETicketInfoFromFreeText(case7);
		Assert.assertEquals(((List<String>)eTicketInfos7[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos7[1], "03MAY19");
		Assert.assertEquals(eTicketInfos7[2], "PAX");
		Assert.assertEquals(eTicketInfos7[3], "HKD");
		Assert.assertEquals(eTicketInfos7[4], "4019");
		Assert.assertEquals(eTicketInfos7[5], "HKGCX08AA");
		
		String case8 = "PAX 160-2372032016/ETCX/HKD4019/03MAY19/HKGCX08AA/13393855";
		Object[] eTicketInfos8 = FreeTextUtil.getETicketInfoFromFreeText(case8);
		Assert.assertEquals(((List<String>)eTicketInfos8[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos8[1], "03MAY19");
		Assert.assertEquals(eTicketInfos8[2], "PAX");
		Assert.assertEquals(eTicketInfos8[3], "HKD");
		Assert.assertEquals(eTicketInfos8[4], "4019");
		Assert.assertEquals(eTicketInfos8[5], "HKGCX08AA");
		
		String case9 = "PAX 160-2372032016/ETCX/HKD4019/03MAY19/HKGCX0388/13393855";
		Object[] eTicketInfos9 = FreeTextUtil.getETicketInfoFromFreeText(case9);
		Assert.assertEquals(((List<String>)eTicketInfos9[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos9[1], "03MAY19");
		Assert.assertEquals(eTicketInfos9[2], "PAX");
		Assert.assertEquals(eTicketInfos9[3], "HKD");
		Assert.assertEquals(eTicketInfos9[4], "4019");
		Assert.assertEquals(eTicketInfos9[5], "HKGCX0388");
		
		String case10 = "PAX 160-2372032016/ETCX/HKD4019/03MAY19/SWI1G/13393855";
		Object[] eTicketInfos10 = FreeTextUtil.getETicketInfoFromFreeText(case10);
		Assert.assertEquals(((List<String>)eTicketInfos10[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos10[1], "03MAY19");
		Assert.assertEquals(eTicketInfos10[2], "PAX");
		Assert.assertEquals(eTicketInfos10[3], "HKD");
		Assert.assertEquals(eTicketInfos10[4], "4019");
		Assert.assertEquals(eTicketInfos10[5], "SWI1G");
		
		String case11 = "PAX 160-2372032016/ETCX/HKD4019/03MAY19/TYO10F2LYG/13393855";
		Object[] eTicketInfos11 = FreeTextUtil.getETicketInfoFromFreeText(case11);
		Assert.assertEquals(((List<String>)eTicketInfos11[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos11[1], "03MAY19");
		Assert.assertEquals(eTicketInfos11[2], "PAX");
		Assert.assertEquals(eTicketInfos11[3], "HKD");
		Assert.assertEquals(eTicketInfos11[4], "4019");
		Assert.assertEquals(eTicketInfos11[5], "TYO10F2LYG");
		
		String case12 = "PAX 160-2372032016/ETCX/03MAY19/HKGCX08AA/13393855";
		Object[] eTicketInfos12 = FreeTextUtil.getETicketInfoFromFreeText(case12);
		Assert.assertEquals(((List<String>)eTicketInfos12[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos12[1], "03MAY19");
		Assert.assertEquals(eTicketInfos12[2], "PAX");
		Assert.assertEquals(eTicketInfos12[3], null);
		Assert.assertEquals(eTicketInfos12[4], null);
		Assert.assertEquals(eTicketInfos12[5], "HKGCX08AA");
		
		String case13 = "PAX 160-2372032016/03MAY19/HKGCX08LA/13393855";
		Object[] eTicketInfos13 = FreeTextUtil.getETicketInfoFromFreeText(case13);
		Assert.assertEquals(((List<String>)eTicketInfos13[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos13[1], "03MAY19");
		Assert.assertEquals(eTicketInfos13[2], "PAX");
		Assert.assertEquals(eTicketInfos13[3], null);
		Assert.assertEquals(eTicketInfos13[4], null);
		Assert.assertEquals(eTicketInfos13[5], "HKGCX08LA");
		
		String case14 = "PAX 160-2372032016/03MAY19/CX0001-0012/13393855";
		Object[] eTicketInfos14 = FreeTextUtil.getETicketInfoFromFreeText(case14);
		Assert.assertEquals(((List<String>)eTicketInfos14[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos14[1], "03MAY19");
		Assert.assertEquals(eTicketInfos14[2], "PAX");
		Assert.assertEquals(eTicketInfos14[3], null);
		Assert.assertEquals(eTicketInfos14[4], null);
		Assert.assertEquals(eTicketInfos14[5], "CX0001-0012");
		
		String case15 = "PAX 160-2372032016/HKGCX0168/13393855";
		Object[] eTicketInfos15 = FreeTextUtil.getETicketInfoFromFreeText(case15);
		Assert.assertEquals(((List<String>)eTicketInfos15[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos15[1], null);
		Assert.assertEquals(eTicketInfos15[2], "PAX");
		Assert.assertEquals(eTicketInfos15[3], null);
		Assert.assertEquals(eTicketInfos15[4], null);
		Assert.assertEquals(eTicketInfos15[5], "HKGCX0168");
		
		String case16 = "PAX 160-2372032016/SWI1G/13393855";
		Object[] eTicketInfos16 = FreeTextUtil.getETicketInfoFromFreeText(case16);
		Assert.assertEquals(((List<String>)eTicketInfos16[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos16[1], null);
		Assert.assertEquals(eTicketInfos16[2], "PAX");
		Assert.assertEquals(eTicketInfos16[3], null);
		Assert.assertEquals(eTicketInfos16[4], null);
		Assert.assertEquals(eTicketInfos16[5], "SWI1G");
		
		String case17 = "PAX 160-2372032016/TYO10F2LYG/13393855";
		Object[] eTicketInfos17 = FreeTextUtil.getETicketInfoFromFreeText(case17);
		Assert.assertEquals(((List<String>)eTicketInfos17[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos17[1], null);
		Assert.assertEquals(eTicketInfos17[2], "PAX");
		Assert.assertEquals(eTicketInfos17[3], null);
		Assert.assertEquals(eTicketInfos17[4], null);
		Assert.assertEquals(eTicketInfos17[5], "TYO10F2LYG");
		
		String case18 = "PAX 160-2372032016/13393855";
		Object[] eTicketInfos18 = FreeTextUtil.getETicketInfoFromFreeText(case18);
		Assert.assertEquals(((List<String>)eTicketInfos18[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos18[1], null);
		Assert.assertEquals(eTicketInfos18[2], "PAX");
		Assert.assertEquals(eTicketInfos18[3], null);
		Assert.assertEquals(eTicketInfos18[4], null);
		Assert.assertEquals(eTicketInfos18[5], null);
		
		String case19 = "PAX 160-2372032016/ETCX////";
		Object[] eTicketInfos19 = FreeTextUtil.getETicketInfoFromFreeText(case19);
		Assert.assertEquals(((List<String>)eTicketInfos19[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos19[1], null);
		Assert.assertEquals(eTicketInfos19[2], "PAX");
		Assert.assertEquals(eTicketInfos19[3], null);
		Assert.assertEquals(eTicketInfos19[4], null);	
		Assert.assertEquals(eTicketInfos19[5], null);
		
		String case20 = "PAX 160-2372032016//HKD4019//";
		Object[] eTicketInfos20 = FreeTextUtil.getETicketInfoFromFreeText(case20);
		Assert.assertEquals(((List<String>)eTicketInfos20[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos20[1], null);
		Assert.assertEquals(eTicketInfos20[2], "PAX");
		Assert.assertEquals(eTicketInfos20[3], "HKD");
		Assert.assertEquals(eTicketInfos20[4], "4019");
		Assert.assertEquals(eTicketInfos20[5], null);
		
		String case21 = "PAX 160-2372032016///03MAY19/";
		Object[] eTicketInfos21 = FreeTextUtil.getETicketInfoFromFreeText(case21);
		Assert.assertEquals(((List<String>)eTicketInfos21[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos21[1], "03MAY19");
		Assert.assertEquals(eTicketInfos21[2], "PAX");
		Assert.assertEquals(eTicketInfos21[3], null);
		Assert.assertEquals(eTicketInfos21[4], null);
		Assert.assertEquals(eTicketInfos21[5], null);
		
		String case22 = "PAX 160-2372032016////HKGCX08AA";
		Object[] eTicketInfos22 = FreeTextUtil.getETicketInfoFromFreeText(case22);
		Assert.assertEquals(((List<String>)eTicketInfos22[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos22[1], null);
		Assert.assertEquals(eTicketInfos22[2], "PAX");
		Assert.assertEquals(eTicketInfos22[3], null);
		Assert.assertEquals(eTicketInfos22[4], null);
		Assert.assertEquals(eTicketInfos22[5], "HKGCX08AA");
		
		String case23 = "PAX 160-2372032016/////13393855";
		Object[] eTicketInfos23 = FreeTextUtil.getETicketInfoFromFreeText(case23);
		Assert.assertEquals(((List<String>)eTicketInfos23[0]).get(0), "1602372032016");
		Assert.assertEquals(eTicketInfos23[1], null);
		Assert.assertEquals(eTicketInfos23[2], "PAX");
		Assert.assertEquals(eTicketInfos23[3], null);
		Assert.assertEquals(eTicketInfos23[4], null);
		Assert.assertEquals(eTicketInfos23[5], null);
		
		String case24 = "PAX 160-2372247258/ETCX/06MAY19/HKGCX08LA/13393881";
		Object[] eTicketInfos24 = FreeTextUtil.getETicketInfoFromFreeText(case24);
		Assert.assertEquals(((List<String>)eTicketInfos24[0]).get(0), "1602372247258");
		Assert.assertEquals(eTicketInfos24[1], "06MAY19");
		Assert.assertEquals(eTicketInfos24[2], "PAX");
		Assert.assertEquals(eTicketInfos24[3], null);
		Assert.assertEquals(eTicketInfos24[4], null);
		Assert.assertEquals(eTicketInfos24[5], "HKGCX08LA");
		
		String case25 = "PAX 160-2372032016-18/ETCX/03MAY19/HKGCX08AA/13393855";
		Object[] eTicketInfos25 = FreeTextUtil.getETicketInfoFromFreeText(case25);
		Assert.assertEquals(((List<String>)eTicketInfos25[0]).get(0), "1602372032016");
		Assert.assertEquals(((List<String>)eTicketInfos25[0]).get(1), "1602372032017");
		Assert.assertEquals(((List<String>)eTicketInfos25[0]).get(2), "1602372032018");
		Assert.assertEquals(((List<String>)eTicketInfos25[0]).size(), 3);
		Assert.assertEquals(eTicketInfos25[1], "03MAY19");
		Assert.assertEquals(eTicketInfos25[2], "PAX");
		Assert.assertEquals(eTicketInfos25[3], null);
		Assert.assertEquals(eTicketInfos25[4], null);
		Assert.assertEquals(eTicketInfos25[5], "HKGCX08AA");
		
		String case26 = "PAX 589-2137084488/ET9W/30JAN19/HKGCX0ERS/13010454";
		Object[] eTicketInfos26 = FreeTextUtil.getETicketInfoFromFreeText(case26);
		Assert.assertEquals(((List<String>)eTicketInfos26[0]).get(0), "5892137084488");
		Assert.assertEquals(((List<String>)eTicketInfos26[0]).size(), 1);
		Assert.assertEquals(eTicketInfos26[1], "30JAN19");
		Assert.assertEquals(eTicketInfos26[2], "PAX");
		Assert.assertEquals(eTicketInfos26[3], null);
		Assert.assertEquals(eTicketInfos26[4], null);
		Assert.assertEquals(eTicketInfos26[5], "HKGCX0ERS");
		
		String case27 = "PAX 589-2137084488/ETWWW/30JAN19/HKGCX0ERS/13010454";
		Object[] eTicketInfos27 = FreeTextUtil.getETicketInfoFromFreeText(case27);
		Assert.assertEquals(((List<String>)eTicketInfos27[0]).get(0), "5892137084488");
		Assert.assertEquals(((List<String>)eTicketInfos27[0]).size(), 1);
		Assert.assertEquals(eTicketInfos27[1], "30JAN19");
		Assert.assertEquals(eTicketInfos27[2], "PAX");
		Assert.assertEquals(eTicketInfos27[3], null);
		Assert.assertEquals(eTicketInfos27[4], null);
		Assert.assertEquals(eTicketInfos27[5], "HKGCX0ERS");
	}
	
	@Test
	public void test_fp_freetext() {
		String case1 = "PAX CCVI/4444333322221111/1020/HKD8977/N417";
		Object[] fpInfos1 = FreeTextUtil.getFpNoStoredCardInfo(case1);
		Assert.assertEquals(fpInfos1[0], "CCVI");
		Assert.assertEquals(fpInfos1[1], "VI");
		Assert.assertEquals(fpInfos1[2], "4444333322221111");
		Assert.assertEquals(fpInfos1[3], "1020");
		Assert.assertEquals(fpInfos1[4], "HKD");
		Assert.assertEquals(fpInfos1[5], "8977");
		Assert.assertEquals(fpInfos1[6], "N417");
		
		String case2 = "PAX CCVI/4444333322221111/1020/HKD8977";
		Object[] fpInfos2 = FreeTextUtil.getFpNoStoredCardInfo(case2);
		Assert.assertEquals(fpInfos2[0], "CCVI");
		Assert.assertEquals(fpInfos2[1], "VI");
		Assert.assertEquals(fpInfos2[2], "4444333322221111");
		Assert.assertEquals(fpInfos2[3], "1020");
		Assert.assertEquals(fpInfos2[4], "HKD");
		Assert.assertEquals(fpInfos2[5], "8977");
		Assert.assertEquals(fpInfos2[6], null);
		
		String case3 = "PAX CCVI/4444333XXXX1111/1020/N417";
		Object[] fpInfos3 = FreeTextUtil.getFpNoStoredCardInfo(case3);
		Assert.assertEquals(fpInfos3[0], "CCVI");
		Assert.assertEquals(fpInfos3[1], "VI");
		Assert.assertEquals(fpInfos3[2], "4444333XXXX1111");
		Assert.assertEquals(fpInfos3[3], "1020");
		Assert.assertEquals(fpInfos3[4], null);
		Assert.assertEquals(fpInfos3[5], null);
		Assert.assertEquals(fpInfos3[6], "N417");
		
		String case4 = "PAX CCVI/4444333322221111/1020";
		Object[] fpInfos4 = FreeTextUtil.getFpNoStoredCardInfo(case4);
		Assert.assertEquals(fpInfos4[0], "CCVI");
		Assert.assertEquals(fpInfos4[1], "VI");
		Assert.assertEquals(fpInfos4[2], "4444333322221111");
		Assert.assertEquals(fpInfos4[3], "1020");
		Assert.assertEquals(fpInfos4[4], null);
		Assert.assertEquals(fpInfos4[5], null);
		Assert.assertEquals(fpInfos4[6], null);
		
		String case5 = "PAX CCVI/4444333322221111";
		Object[] fpInfos5 = FreeTextUtil.getFpNoStoredCardInfo(case5);
		Assert.assertEquals(fpInfos5, null);
		
		String case6 = "PAX CCVI/4444333322221111/1020-CV/HKD8977/N417";
		Object[] fpInfos6 = FreeTextUtil.getFpNoStoredCardInfo(case6);
		Assert.assertEquals(fpInfos6[0], "CCVI");
		Assert.assertEquals(fpInfos6[1], "VI");
		Assert.assertEquals(fpInfos6[2], "4444333322221111");
		Assert.assertEquals(fpInfos6[3], "1020");
		Assert.assertEquals(fpInfos6[4], "HKD");
		Assert.assertEquals(fpInfos6[5], "8977");
		Assert.assertEquals(fpInfos6[6], "N417");
		
		String case7 = "PAX CCVI/4444333322221111/0020-CV/HKD8977/N417";
		Object[] fpInfos7 = FreeTextUtil.getFpNoStoredCardInfo(case7);
		Assert.assertEquals(fpInfos7[0], "CCVI");
		Assert.assertEquals(fpInfos7[1], "VI");
		Assert.assertEquals(fpInfos7[2], "4444333322221111");
		Assert.assertEquals(fpInfos7[3], "0020");
		Assert.assertEquals(fpInfos7[4], "HKD");
		Assert.assertEquals(fpInfos7[5], "8977");
		Assert.assertEquals(fpInfos7[6], "N417");
		
		String case8 = "PAX CCVI/4444333322221111/A0123457/HKD8977/N417";
		Object[] fpInfos8 = FreeTextUtil.getFpNoStoredCardInfo(case8);
		Assert.assertEquals(fpInfos8, null);
	}
}

package com.coingi.exchange.client.utils;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class HexEncoderTest {

	@DataProvider
	public static Object[][] provideData() {
		return new Object[][]{
				{
						new byte[]{(byte) 0xFF, (byte) 0x0F, (byte) 0x00},
						"FF0F00"
				},
				{
						new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF},
						"0123456789ABCDEF"
				}
		};
	}

	@Test
	@UseDataProvider("provideData")
	public void test(byte[] data, String expectedOutput) {
		assertEquals(expectedOutput, HexEncoder.encode(data));
	}

}

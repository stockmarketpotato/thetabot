package com.stockmarketpotato.broker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class ApiUtilitiesTest {
	private final BigDecimal TICK_SIZE = BigDecimal.valueOf(0.05);
	@Test
		public void testRounding() {
			BigDecimal b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.43));
			assertEquals(BigDecimal.valueOf(5.45), b);

			b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.48));
			assertEquals(BigDecimal.valueOf(5.50).setScale(2), b);
			
			b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.47));
			assertEquals(BigDecimal.valueOf(5.45), b);
			
			b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.46));
			assertEquals(BigDecimal.valueOf(5.45), b);
			
			b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.44));
			assertEquals(BigDecimal.valueOf(5.45), b);
			
			b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.43));
			assertEquals(BigDecimal.valueOf(5.45), b);
			
			b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.42));
			assertEquals(BigDecimal.valueOf(5.40).setScale(2), b);
			
			b = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(5.41));
			assertEquals(BigDecimal.valueOf(5.40).setScale(2), b);
		}
}

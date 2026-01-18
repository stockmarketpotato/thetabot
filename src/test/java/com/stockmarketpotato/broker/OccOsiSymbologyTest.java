package com.stockmarketpotato.broker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class OccOsiSymbologyTest {

	@Test
	public void testOccOsiSymbology() {
		OccOsiSymbology symbol = OccOsiSymbology.fromFuturesOptionSymbol("./ESH4 E2AH4 240311C4200");
		assertEquals("./ESH4 11 Mar 2024 4200 Call", symbol.getName());
		assertEquals("./ESH4 E2AH4 240311C4200", symbol.getOccKey());

		symbol = OccOsiSymbology.fromFuturesOptionSymbol("./ESH4 E2AH4 240311P4200");
		assertEquals("./ESH4 11 Mar 2024 4200 Put", symbol.getName());
		assertEquals("./ESH4 E2AH4 240311P4200", symbol.getOccKey());

		symbol = OccOsiSymbology.fromSymbol("AAPL  220916C00040000");
		assertEquals("AAPL 16 Sep 2022 40.000 Call", symbol.getName());
		assertEquals("AAPL220916C00040000", symbol.getOccKey());
		assertEquals("AAPL  220916C00040000", symbol.getStrictOccKey());
		
		symbol = OccOsiSymbology.fromSymbol("TQQQ230406P00027000");
		assertEquals("TQQQ 06 Apr 2023 27.000 Put", symbol.getName());
		assertEquals("TQQQ230406P00027000", symbol.getOccKey());
		assertEquals("TQQQ 06 Apr. 2023 27,000 Put", symbol.getName(Locale.GERMAN));
	}
}

package com.stockmarketpotato.broker;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Options Symbology Initiative (OSI)
 * Options Clearing Corporation (OCC)
 * <p>
 * This class handles parsing and formatting of option symbols according to the OCC OSI standard.
 * It supports both standard equity options and futures options (which often have specific broker-dependent formats, 
 * here tailored for Tastytrade's representation of futures options).
 */
public class OccOsiSymbology {
	private final static Logger log = LoggerFactory.getLogger(OccOsiSymbology.class);
	
	private final static TimeZone TIME_ZONE = TimeZone.getTimeZone("America/New_York");
	
    private String symbol;
    private Date expiration;
    private OptionType type = OptionType.CALL;
    private BigDecimal strike;
    private String subsymbol;
    private enum OptionType {
        CALL,
        PUT
    };

    private OccOsiSymbology(final String symbol, final Date expiration, final OptionType optionType, final BigDecimal s)
    {
        super();
        this.symbol = symbol;
        this.type = optionType;
        this.strike = s;
        this.expiration = expiration;
    }
    private OccOsiSymbology(final String symbol, final String subsymbol, final Date expiration, final OptionType optionType, final BigDecimal s)
    {
        this(symbol, expiration, optionType, s);
        this.subsymbol = subsymbol;
    }
    
    /**
     * Parses a futures option symbol string (Tastytrade format) into an OccOsiSymbology object.
     * <p>
     * Expected format example: {@code ./ESH4 E2AH4 240311C4200}
     * @param symbolString The symbol string to parse.
     * @return An instance of OccOsiSymbology, or null if parsing fails.
     */
    static public OccOsiSymbology fromFuturesOptionSymbol(final String symbolString) {
    	if (!symbolString.startsWith("./")) {
    		log.error("Construction from '" + symbolString + "' failed. Symbol does not start with './'");
    		return null;
    	}
    	final String regex = "(?<symbol>\\.\\/[\\w\\d]{1,6})\\s+(?<subsymbol>[\\w\\d]+)\\s+(?<date>\\d{6})(?<type>[PC])(?<strike>\\d{4})";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(symbolString);
        
        if (matcher.matches()) {
        	final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd", Locale.US);
            sdf.setTimeZone(TIME_ZONE);
        	String symbol = matcher.group("symbol").trim();
        	String subsymbol = matcher.group("subsymbol").trim();
        	Date expiration = null;
            try
            {
                expiration = sdf.parse(matcher.group("date"));
            }
            catch (ParseException e)
            {
            	e.printStackTrace();
            }
            OptionType optionType = OptionType.PUT;
            if (matcher.group("type").equals("C"))
                optionType = OptionType.CALL;
            BigDecimal strike = new BigDecimal(matcher.group("strike"));

    		return new OccOsiSymbology(symbol, subsymbol, expiration, optionType, strike);        	
        }
        log.error("Construction from '" + symbolString + "' failed.");
        return null;
    }
    
    /**
     * Parses a standard OCC option symbol string into an OccOsiSymbology object.
     * <p>
     * Expected format example: {@code AAPL  230120C00150000}
     * 
     * @param occString The OCC symbol string.
     * @return An instance of OccOsiSymbology, or null if parsing fails.
     */
    static public OccOsiSymbology fromSymbol(final String occString) {
        final String regex = "(?<symbol>[\\w ]{1,6})(?<date>\\d{6})(?<type>[PC])(?<strike>\\d{8})";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(occString);
        
        if (matcher.matches()) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd", Locale.US);
            sdf.setTimeZone(TIME_ZONE);
        	String symbol = matcher.group("symbol").trim();
        	Date expiration = null;
            try
            {
                expiration = sdf.parse(matcher.group("date"));
            }
            catch (ParseException e)
            {
            	e.printStackTrace();
            }

            OptionType optionType = OptionType.PUT;
            if (matcher.group("type").equals("C"))
                optionType = OptionType.CALL;
            BigDecimal strike = new BigDecimal(matcher.group("strike")).setScale(4).divide(new BigDecimal(1000.));

    		return new OccOsiSymbology(symbol, expiration, optionType, strike);        	
        }
        return null;
    }

    
    /**
     * Generates the OCC key for this option.
     * 
     * @return The OCC key string.
     */
    public String getOccKey() {
    	return getOccKey(false);
    }

    /**
     * Generates the strict OCC key for this option (padded symbol).
     * 
     * @return The strict OCC key string.
     */
    public String getStrictOccKey() {
    	return getOccKey(true);
    }
    
    private String getOccKey(final boolean strict) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd", Locale.US);
        sdf.setTimeZone(TIME_ZONE);
        String symbolName = symbol;
        if (strict)
        	symbolName = String.format("%-6s", symbol);
        	
        String strikePrice = String.format("%08.0f", strike.doubleValue() * 1000.);
        if (isFuturesOption()) {
        	symbolName += " " + this.subsymbol + " ";
        	strikePrice = String.format("%.0f", strike);
        }
        return symbolName + sdf.format(expiration) + (type == OptionType.PUT ? "P" : "C") + strikePrice; 
    }

    /**
     * Generates a human-readable name for the option using the default US locale.
     * 
     * @return The human-readable name.
     */
    public String getName() {
    	return getName(Locale.US); 
    }
    
    /**
     * Generates a human-readable name for the option using the specified locale.
     * 
     * @param locale The locale to use for formatting numbers/dates.
     * @return The human-readable name.
     */
    public String getName(final Locale locale) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", locale);
        sdf.setTimeZone(TIME_ZONE);
        String symbolName = symbol;
        String strikePrice = String.format(locale, "%.3f", strike);
        if (isFuturesOption())
        	strikePrice = String.format(locale, "%.0f", strike);
        return symbolName + " " + sdf.format(expiration) + " " + strikePrice + (type == OptionType.PUT ? " Put" : " Call");
    }
    
    private boolean isFuturesOption() {
    	return this.subsymbol != null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OccOsiSymbology {\n");
        sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
        sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    strike: ").append(toIndentedString(strike)).append("\n");
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
      if (o == null) {
        return "null";
      }
      return o.toString().replace("\n", "\n    ");
    }
    
    /**
     * Gets the strike price.
     * @return The strike price.
     */
	public BigDecimal getStrike() {
		return this.strike;
	}
	
    /**
     * Gets the expiration date.
     * @return The expiration date.
     */
	public Date getExpiration() {
		return expiration;
	}
}

package com.stockmarketpotato.feeds;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;

public class FeedUtilities {
	private final static Logger logger = LoggerFactory.getLogger(FeedUtilities.class.getName());
	private static final String ecbUrl = "https://data-api.ecb.europa.eu/service/data/EXR/D.USD.EUR.SP00.A?startPeriod=";
	
	public static boolean notOlderThanSeconds(final Date date, final long maxSeconds) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), TimeZone.getDefault().toZoneId());
        ZonedDateTime now = ZonedDateTime.now();
        long diff = ChronoUnit.SECONDS.between(zdt, now);
        return diff <= maxSeconds;
	}
	
	public static HashMap<LocalDate, Double> getForexEurUsd(LocalDate startDate) {
		HashMap<LocalDate, Double> exchangeRates = new HashMap<LocalDate, Double>();
	    try {
	        // Fetch the XML data
	        URL url = new URL(ecbUrl + startDate.toString());
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Accept", "application/xml");
	        
	        // Check the response code
	        int responseCode = connection.getResponseCode();
	        if (responseCode != HttpURLConnection.HTTP_OK) {
	            logger.error("Failed to fetch data: HTTP error code: " + responseCode);
	            return exchangeRates;
	        }

	        // Check if the input stream is empty
	        InputStream inputStream = connection.getInputStream();
	        if (inputStream == null || inputStream.available() == 0) {
	            return exchangeRates;
	        }
	        
	        // Parse the XML response
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(connection.getInputStream());
	        doc.getDocumentElement().normalize();
	        
	        // Extract exchange rates from the specified start date up to today
	        NodeList nodes = doc.getElementsByTagName("generic:Obs");
	        for (int i = 0; i < nodes.getLength(); i++) {
	            Element element = (Element) nodes.item(i);
	            Element timeElement = (Element) element.getElementsByTagName("generic:ObsDimension").item(0);
	            Element rateElement = (Element) element.getElementsByTagName("generic:ObsValue").item(0);
	            
	            LocalDate date = LocalDate.parse(timeElement.getAttribute("value"));
	            Double rate = Double.parseDouble(rateElement.getAttribute("value"));
	            exchangeRates.put(date, rate);
	        }
	        
	    } catch (SAXParseException e) {
	        e.printStackTrace();
	        logger.error("Parsing exception");
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("Failed to fetch the exchange rates.");
	    }
	    return exchangeRates;
	}
}
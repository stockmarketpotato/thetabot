package com.stockmarketpotato.integration.tastytrade.model.orders;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class OrdersFactory {
	private final static Logger logger = LoggerFactory.getLogger(OrdersFactory.class);
	public static PostAccountsAccountNumberOrdersDryRun createPostAccountsAccountNumberOrdersDryRun(
			PostAccountsAccountNumberOrders postAccountsAccountNumberOrders) {
		PostAccountsAccountNumberOrdersDryRun order = new PostAccountsAccountNumberOrdersDryRun();
		if (postAccountsAccountNumberOrders.getAdvancedInstructions() != null) {
			order.setAdvancedInstructions(postAccountsAccountNumberOrders.getAdvancedInstructions());
		}

		if (postAccountsAccountNumberOrders.getAutomatedSource() != null) {
			order.setAutomatedSource(postAccountsAccountNumberOrders.getAutomatedSource());
		}

		if (postAccountsAccountNumberOrders.getGtcDate() != null) {
			order.setGtcDate(postAccountsAccountNumberOrders.getGtcDate());
		}

		if (postAccountsAccountNumberOrders.getLegs() != null) {
			order.setLegs(postAccountsAccountNumberOrders.getLegs());
		}

		if (postAccountsAccountNumberOrders.getOrderType() != null) {
			if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.LIMIT)
		        order.setOrderType(OrderTypeEnum.LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.MARKET)
		        order.setOrderType(OrderTypeEnum.MARKET);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.MARKETABLE_LIMIT)
		        order.setOrderType(OrderTypeEnum.MARKETABLE_LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.STOP)
		        order.setOrderType(OrderTypeEnum.STOP);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.STOP_LIMIT)
		        order.setOrderType(OrderTypeEnum.STOP_LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.NOTIONAL_MARKET)
		        order.setOrderType(OrderTypeEnum.NOTIONAL_MARKET);
		}

		if (postAccountsAccountNumberOrders.getPartitionKey() != null) {
			order.setPartitionKey(postAccountsAccountNumberOrders.getPartitionKey());
		}

		if (postAccountsAccountNumberOrders.getPreflightId() != null) {
			order.setPreflightId(postAccountsAccountNumberOrders.getPreflightId());
		}

		if (postAccountsAccountNumberOrders.getPrice() != null) {
			order.setPrice(postAccountsAccountNumberOrders.getPrice());
		}

		if (postAccountsAccountNumberOrders.getPriceEffect() != null) {
			if (postAccountsAccountNumberOrders.getPriceEffect() == PriceEffectEnum.CREDIT)
				order.setPriceEffect(PriceEffectEnum.CREDIT);
			if (postAccountsAccountNumberOrders.getPriceEffect() == PriceEffectEnum.DEBIT)
				order.setPriceEffect(PriceEffectEnum.DEBIT);
		}

		if (postAccountsAccountNumberOrders.getRules() != null) {
			order.setRules(postAccountsAccountNumberOrders.getRules());
		}

		if (postAccountsAccountNumberOrders.getSource() != null) {
			order.setSource(postAccountsAccountNumberOrders.getSource());
		}

		if (postAccountsAccountNumberOrders.getStopTrigger() != null) {
			order.setStopTrigger(postAccountsAccountNumberOrders.getStopTrigger());
		}

		if (postAccountsAccountNumberOrders.getTimeInForce() != null) {
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.DAY)
			    order.setTimeInForce(TimeInForceEnum.DAY);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTC)
			    order.setTimeInForce(TimeInForceEnum.GTC);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTD)
			    order.setTimeInForce(TimeInForceEnum.GTD);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.EXT)
			    order.setTimeInForce(TimeInForceEnum.EXT);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTC_EXT)
			    order.setTimeInForce(TimeInForceEnum.GTC_EXT);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.IOC)
			    order.setTimeInForce(TimeInForceEnum.IOC);
		}

		if (postAccountsAccountNumberOrders.getValue() != null) {
			order.setValue(postAccountsAccountNumberOrders.getValue());
		}

		if (postAccountsAccountNumberOrders.getValueEffect() != null) {
			if (postAccountsAccountNumberOrders.getValueEffect() == ValueEffectEnum.CREDIT)
				order.setValueEffect(ValueEffectEnum.CREDIT);
			if (postAccountsAccountNumberOrders.getValueEffect() == ValueEffectEnum.DEBIT)
				order.setValueEffect(ValueEffectEnum.DEBIT);
		}

		return order;
	}

	public static PostAccountsAccountNumberOrdersIdDryRun createPostAccountsAccountNumberOrdersIdDryRun(
			PostAccountsAccountNumberOrders postAccountsAccountNumberOrders) {

		PostAccountsAccountNumberOrdersIdDryRun order = new PostAccountsAccountNumberOrdersIdDryRun();

		if (postAccountsAccountNumberOrders.getAutomatedSource() != null) {
			order.setAutomatedSource(postAccountsAccountNumberOrders.getAutomatedSource());
		}

		if (postAccountsAccountNumberOrders.getGtcDate() != null) {
			order.setGtcDate(postAccountsAccountNumberOrders.getGtcDate());
		}

		if (postAccountsAccountNumberOrders.getLegs() != null) {
			List<PostAccountsAccountNumberOrdersIdDryRunLegsInner> legs = new ArrayList<>();
			for (PostAccountsAccountNumberOrdersDryRunLegsInner l : postAccountsAccountNumberOrders.getLegs())
				legs.add(createPostAccountsAccountNumberOrdersIdDryRunLegsInner(l));
			order.setLegs(legs);
		}
		  
		if (postAccountsAccountNumberOrders.getOrderType() != null) {
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.LIMIT)
		        order.setOrderType(OrderTypeEnum.LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.MARKET)
		        order.setOrderType(OrderTypeEnum.MARKET);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.MARKETABLE_LIMIT)
		        order.setOrderType(OrderTypeEnum.MARKETABLE_LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.STOP)
		        order.setOrderType(OrderTypeEnum.STOP);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.STOP_LIMIT)
		        order.setOrderType(OrderTypeEnum.STOP_LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.NOTIONAL_MARKET)
		        order.setOrderType(OrderTypeEnum.NOTIONAL_MARKET);
		}

		if (postAccountsAccountNumberOrders.getPartitionKey() != null) {
			order.setPartitionKey(postAccountsAccountNumberOrders.getPartitionKey());
		}
		  
		if (postAccountsAccountNumberOrders.getPreflightId() != null) {
			order.setPreflightId(postAccountsAccountNumberOrders.getPreflightId());
		}

		if (postAccountsAccountNumberOrders.getPrice() != null) {
			order.setPrice(postAccountsAccountNumberOrders.getPrice());
		}

		if (postAccountsAccountNumberOrders.getPriceEffect() != null) {
			if (postAccountsAccountNumberOrders.getPriceEffect() == PriceEffectEnum.CREDIT)
				order.setPriceEffect(PriceEffectEnum.CREDIT);
			if (postAccountsAccountNumberOrders.getPriceEffect() == PriceEffectEnum.DEBIT)
				order.setPriceEffect(PriceEffectEnum.DEBIT);
		}
		
		if (postAccountsAccountNumberOrders.getSource() != null) {
			order.setSource(postAccountsAccountNumberOrders.getSource());
		}

		if (postAccountsAccountNumberOrders.getStopTrigger() != null) {
			order.setStopTrigger(postAccountsAccountNumberOrders.getStopTrigger());
		}

		if (postAccountsAccountNumberOrders.getTimeInForce() != null) {
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.DAY)
			    order.setTimeInForce(TimeInForceEnum.DAY);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTC)
			    order.setTimeInForce(TimeInForceEnum.GTC);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTD)
			    order.setTimeInForce(TimeInForceEnum.GTD);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.EXT)
			    order.setTimeInForce(TimeInForceEnum.EXT);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTC_EXT)
			    order.setTimeInForce(TimeInForceEnum.GTC_EXT);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.IOC)
			    order.setTimeInForce(TimeInForceEnum.IOC);
		}

		if (postAccountsAccountNumberOrders.getValue() != null) {
			order.setValue(postAccountsAccountNumberOrders.getValue());
		}

		if (postAccountsAccountNumberOrders.getValueEffect() != null) {
			if (postAccountsAccountNumberOrders.getValueEffect() == ValueEffectEnum.CREDIT)
				order.setValueEffect(ValueEffectEnum.CREDIT);
			if (postAccountsAccountNumberOrders.getValueEffect() == ValueEffectEnum.DEBIT)
				order.setValueEffect(ValueEffectEnum.DEBIT);
		}
		
		return order;
	}
	
	
	public static PostAccountsAccountNumberOrdersIdDryRunLegsInner createPostAccountsAccountNumberOrdersIdDryRunLegsInner(
			PostAccountsAccountNumberOrdersDryRunLegsInner postAccountsAccountNumberOrdersDryRunLegsInner) {
		ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).registerModule(new JavaTimeModule());
		String postAccountsAccountNumberOrdersDryRunLegsInnerString;
		PostAccountsAccountNumberOrdersIdDryRunLegsInner order = null;
		try {
			postAccountsAccountNumberOrdersDryRunLegsInnerString = mapper
					.writeValueAsString(postAccountsAccountNumberOrdersDryRunLegsInner);
			order = mapper.readValue(
					postAccountsAccountNumberOrdersDryRunLegsInnerString,
					PostAccountsAccountNumberOrdersIdDryRunLegsInner.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		return order;
	}

	public static PutAccountsAccountNumberOrdersId createPutAccountsAccountNumberOrdersId(
			PostAccountsAccountNumberOrders postAccountsAccountNumberOrders) {
		PutAccountsAccountNumberOrdersId order = new PutAccountsAccountNumberOrdersId();
		if (postAccountsAccountNumberOrders.getAutomatedSource() != null) {
			order.setAutomatedSource(postAccountsAccountNumberOrders.getAutomatedSource());
		}

		if (postAccountsAccountNumberOrders.getGtcDate() != null) {
			order.setGtcDate(postAccountsAccountNumberOrders.getGtcDate());
		}

		if (postAccountsAccountNumberOrders.getOrderType() != null) {
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.LIMIT)
		        order.setOrderType(OrderTypeEnum.LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.MARKET)
		        order.setOrderType(OrderTypeEnum.MARKET);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.MARKETABLE_LIMIT)
		        order.setOrderType(OrderTypeEnum.MARKETABLE_LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.STOP)
		        order.setOrderType(OrderTypeEnum.STOP);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.STOP_LIMIT)
		        order.setOrderType(OrderTypeEnum.STOP_LIMIT);
		    if (postAccountsAccountNumberOrders.getOrderType() == OrderTypeEnum.NOTIONAL_MARKET)
		        order.setOrderType(OrderTypeEnum.NOTIONAL_MARKET);
		}

		if (postAccountsAccountNumberOrders.getPartitionKey() != null) {
			order.setPartitionKey(postAccountsAccountNumberOrders.getPartitionKey());
		}

		if (postAccountsAccountNumberOrders.getPreflightId() != null) {
			order.setPreflightId(postAccountsAccountNumberOrders.getPreflightId());
		}

		if (postAccountsAccountNumberOrders.getPrice() != null) {
			order.setPrice(postAccountsAccountNumberOrders.getPrice());
		}

		if (postAccountsAccountNumberOrders.getPriceEffect() != null) {
			if (postAccountsAccountNumberOrders.getPriceEffect() == PriceEffectEnum.CREDIT)
				order.setPriceEffect(PriceEffectEnum.CREDIT);
			if (postAccountsAccountNumberOrders.getPriceEffect() == PriceEffectEnum.DEBIT)
				order.setPriceEffect(PriceEffectEnum.DEBIT);
		}

		if (postAccountsAccountNumberOrders.getSource() != null) {
			order.setSource(postAccountsAccountNumberOrders.getSource());
		}

		if (postAccountsAccountNumberOrders.getStopTrigger() != null) {
			order.setStopTrigger(postAccountsAccountNumberOrders.getStopTrigger());
		}

		if (postAccountsAccountNumberOrders.getTimeInForce() != null) {
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.DAY)
			    order.setTimeInForce(TimeInForceEnum.DAY);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTC)
			    order.setTimeInForce(TimeInForceEnum.GTC);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTD)
			    order.setTimeInForce(TimeInForceEnum.GTD);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.EXT)
			    order.setTimeInForce(TimeInForceEnum.EXT);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.GTC_EXT)
			    order.setTimeInForce(TimeInForceEnum.GTC_EXT);
			if (postAccountsAccountNumberOrders.getTimeInForce() == TimeInForceEnum.IOC)
			    order.setTimeInForce(TimeInForceEnum.IOC);
		}

		if (postAccountsAccountNumberOrders.getValue() != null) {
			order.setValue(postAccountsAccountNumberOrders.getValue());
		}

		if (postAccountsAccountNumberOrders.getValueEffect() != null) {
			if (postAccountsAccountNumberOrders.getValueEffect() == ValueEffectEnum.CREDIT)
				order.setValueEffect(ValueEffectEnum.CREDIT);
			if (postAccountsAccountNumberOrders.getValueEffect() == ValueEffectEnum.DEBIT)
				order.setValueEffect(ValueEffectEnum.DEBIT);
		}

		return order;
	}

	public static PostAccountsAccountNumberOrders createPostAccountsAccountNumberOrders(Order theOrder) {
		PostAccountsAccountNumberOrders order = new PostAccountsAccountNumberOrders();
//		PostAccountsAccountNumberOrders.JSON_PROPERTY_TIME_IN_FORCE,
		if (theOrder.getTimeInForce() != null)
			order.setTimeInForce(TimeInForceEnum.fromValue(theOrder.getTimeInForce()));
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_ORDER_TYPE,
		if (theOrder.getOrderType() != null)
			order.setOrderType(OrderTypeEnum.fromValue(theOrder.getOrderType()));
//		PostAccountsAccountNumberOrders.JSON_PROPERTY_PRICE,		
		if (theOrder.getPrice() != null)
			order.setPrice(theOrder.getPrice());
		
		// PostAccountsAccountNumberOrders.JSON_PROPERTY_PRICE_EFFECT,
//		.priceEffect(PostAccountsAccountNumberOrders.PriceEffectEnum.DEBIT);
		if (theOrder.getPriceEffect() != null)
			order.setPriceEffect(PriceEffectEnum.fromValue(theOrder.getPriceEffect()));
		
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_GTC_DATE,

//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_STOP_TRIGGER,
//		  
//		  
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_VALUE,
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_VALUE_EFFECT,
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_SOURCE,
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_PARTITION_KEY,
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_PREFLIGHT_ID,
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_AUTOMATED_SOURCE,
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_LEGS,
		for (OrderLegsInner orderLegsInner : theOrder.getLegs())
			order.getLegs().add(createPostAccountsAccountNumberOrdersDryRunLegsInner(orderLegsInner));
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_RULES,
//		  PostAccountsAccountNumberOrders.JSON_PROPERTY_ADVANCED_INSTRUCTIONS
		return order;
	}
	
	protected static PostAccountsAccountNumberOrdersDryRunLegsInner createPostAccountsAccountNumberOrdersDryRunLegsInner(OrderLegsInner oleg) {
		PostAccountsAccountNumberOrdersDryRunLegsInner leg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
			.action(ActionEnum.fromValue(oleg.getAction()))
			.instrumentType(InstrumentTypeEnum.fromValue(oleg.getInstrumentType()))
			.quantity(Double.valueOf(oleg.getQuantity()))
			.symbol(oleg.getSymbol());
		return leg;
	}
}

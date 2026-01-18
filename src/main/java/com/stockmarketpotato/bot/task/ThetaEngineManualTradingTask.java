package com.stockmarketpotato.bot.task;

// Use static star import
import static j2html.TagCreator.table;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.strategy.BaseStrategy;
import com.stockmarketpotato.bot.strategy.ThetaEngine45DTE;
import com.stockmarketpotato.broker.OccOsiSymbology;
import com.stockmarketpotato.feeds.AggregatedQuote;

import j2html.tags.specialized.TableTag;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChain;
import com.stockmarketpotato.integration.tastytrade.model.orders.ActionEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.InstrumentTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.OrderTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrders;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrdersDryRunLegsInner;
import com.stockmarketpotato.integration.tastytrade.model.orders.PriceEffectEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.TimeInForceEnum;

/**
 * Task that generates manual trading instructions based on the strategy's logic.
 * Instead of placing orders automatically, it sends an email with the suggested trade details.
 * This class was created to test the scheduling and trade logic.
 */
public class ThetaEngineManualTradingTask extends BaseStrategyTimedTask {
	/**
	 * Constructor for ThetaEngineManualTradingTask.
	 * 
	 * @param baseStrategy The parent strategy.
	 * @param name The name of the task.
	 */
	public ThetaEngineManualTradingTask(BaseStrategy baseStrategy, String name) {
		super(baseStrategy, name);
	}

	@Override
	protected boolean isExecutable(final BotConfiguration configuration) {
		if (!configuration.isManualTradingActive())
			return false;
		return true;
	}

	@Override
	protected void runStrategy(final BotConfiguration configuration) {
		Preconditions.checkNotNull(baseStrategy.getQuoteRepository(), "Invalid Quote Repository");
		String symbol = configuration.getUnderlying().getDisplayValue();
		symbol = symbol.replace("/", "");
		final String account = configuration.getAccountNumber();
		final Integer minDte = configuration.getOpenDte().intValue();
		final Integer maxDte = minDte + 7;
		final BigDecimal minDelta = configuration.getMaxDelta().subtract(new BigDecimal("0.01")).max(new BigDecimal("0.00"));
		FuturesOptionChain chain = ((ThetaEngine45DTE) baseStrategy).getFilteredOptionChain(account, symbol, minDte, maxDte, minDelta,
				configuration.getMaxDelta());
		if (chain.getExpirations().size() == 0)
			return;

		final String putSymbol = chain.getExpirations().get(0).getStrikes()
				.get(chain.getExpirations().get(0).getStrikes().size() - 1).getPut();

		Map<String, Object> model = new HashMap<>();
		Optional<AggregatedQuote> q = baseStrategy.getQuoteRepository().findById(putSymbol);
		if (q.isEmpty())
			return;
		PostAccountsAccountNumberOrders o = new PostAccountsAccountNumberOrders();
		o.setTimeInForce(TimeInForceEnum.DAY);
		o.setOrderType(OrderTypeEnum.LIMIT);
		o.setPrice(q.get().getBidPrice().doubleValue());
		o.setPriceEffect(PriceEffectEnum.CREDIT);
		o.setSource("Thetabot");

		PostAccountsAccountNumberOrdersDryRunLegsInner leg = new PostAccountsAccountNumberOrdersDryRunLegsInner();
		leg.setInstrumentType(InstrumentTypeEnum.FUTURE_OPTION);
		leg.symbol(putSymbol);
		leg.quantity(1.);
		leg.action(ActionEnum.SELL_TO_OPEN);
		o.addLegsItem(leg);

		// now assume multiplier 50
		BigDecimal multiplier = new BigDecimal("50.0");
		Map<String, Double> tradeCost = new HashMap<>(), delta = new HashMap<>();
		Map<String, PostAccountsAccountNumberOrders> orders = new HashMap<>();
		OccOsiSymbology occ = OccOsiSymbology.fromFuturesOptionSymbol(putSymbol);
		orders.put(occ.getName(), o);
		tradeCost.put(occ.getName(),
				new BigDecimal("1.0").multiply(q.get().getBidPrice()).multiply(multiplier).doubleValue());
		delta.put(occ.getName(), q.get().getDelta().doubleValue());
		model.put("orders", orders);
		model.put("tradeCost", tradeCost);
		model.put("delta", delta);

		TableTag t = table();
		baseStrategy.getMailManager().sendEmailToUser("bots/emailReportManualTrading.html",
				"🤖 ThetaBot | Manual Trading Instructions", t);
	}
}

package com.stockmarketpotato.bot.task;

// Use static star import
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.strategy.BaseStrategy;

import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.TableTag;
import j2html.tags.specialized.TrTag;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;

/**
 * Task responsible for monitoring open positions and account status.
 * Checks for take profit, stop loss, and expiration proximity, sending email reports if necessary.
 */
public class MonitoringTask extends BaseStrategyTimedTask {
	/**
	 * Constructor for MonitoringTask.
	 * 
	 * @param baseStrategy The parent strategy.
	 * @param name The name of the task.
	 */
	public MonitoringTask(BaseStrategy baseStrategy, String name) {
		super(baseStrategy, name);
	}

	@Override
	protected boolean isExecutable(BotConfiguration botConfiguration) {
		if (!botConfiguration.isMonitoringAfterOpen())
			return false;
		return true;
	}

	@Override
	protected void runStrategy(BotConfiguration botConfiguration) {
		String accountNumber = botConfiguration.getAccountNumber();
		List<TrTag> info = new ArrayList<>(), warn = new ArrayList<>(), action = new ArrayList<>();
		List<AccountPosition> positions = baseStrategy.getBrokerManager().prodGetPositionsWithQuote(accountNumber);
		if (positions != null) {
			for (AccountPosition a : positions) {

				if (a.pnl != null
						&& a.pnl.compareTo(botConfiguration.getTakeProfit().subtract(new BigDecimal("0.05"))) > 0) {
					/**
					 * report all positions that are 5% points away from take profit
					 */
					info.add(tr(td(a.getName()), td(a.pnl.toString() + "%"), td("🚀🎯!"), td()));

				} else if (a.pnl != null
						&& a.pnl.compareTo(botConfiguration.getStopLoss().add(new BigDecimal("0.2"))) < 0) {
					/**
					 * report all positions that perform bad and are 20% close to Stop Loss
					 */
					warn.add(tr(td(a.getName()), td(a.pnl.toString() + "%"), td("😱!"), td()));

				} else if (a.pnl != null && a.pnl.compareTo(botConfiguration.getStopLoss()) < 0) {
					/**
					 * close all positions that are perform worse than Stop Loss
					 */
					action.add(tr(td(a.getName()), td(a.pnl.toString() + "%"), td("☠️"),
							td("ACTION REQUIRED: reached Stop Loss! Close this position!")));
				} else if (a.instrument_type.contains("Option") && a.getDte() + 1 == botConfiguration.getExitDte()) {
					/**
					 * Close all positions that are too close to expiration.
					 */
					info.add(tr(td(a.getName()), td(a.pnl.toString() + "%"), td("✋!"), td("Approaching Exit DTE!")));
				} else if (a.instrument_type.contains("Option") && a.getDte() == botConfiguration.getExitDte()) {
					/**
					 * Close all positions that are too close to expiration.
					 */
					action.add(tr(td(a.getName()), td(a.pnl.toString() + "%"), td("🛑!"), td("Reached Closing DTE!")));
				}
			}
			TableTag t1 = table(tbody(each(info, i -> i))).attr("border", "0").attr("cellpadding", "3px")
					.attr("cellspacing", "0").attr("height", "100%").attr("width", "100%");
			TableTag t2 = table(tbody(each(warn, i -> i))).attr("border", "0").attr("cellpadding", "3px")
					.attr("cellspacing", "0").attr("height", "100%").attr("width", "100%");
			TableTag t3 = table(tbody(each(action, i -> i))).attr("border", "0").attr("cellpadding", "3px")
					.attr("cellspacing", "0").attr("height", "100%").attr("width", "100%");
			DivTag d = div(t1, t2, t3);
			baseStrategy.getMailManager().sendEmailToUser("bots/emailReport.html", "🤖 ThetaBot | Monitoring Report",
					d);
		}
	}
}

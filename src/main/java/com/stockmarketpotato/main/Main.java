package com.stockmarketpotato.main;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.BotManager;
import com.stockmarketpotato.broker.BrokerManager;
import com.stockmarketpotato.tradelog.TradeManager;
import com.stockmarketpotato.tradelog.TradingStatistics;

import com.stockmarketpotato.integration.tastytrade.model.positions.AccountBalanceSnapshot;
import com.stockmarketpotato.integration.tradingcalendar.TradingCalendar;
import com.stockmarketpotato.integration.tradingcalendar.TradingHours;

/**
 * Main controller for the application's dashboard.
 * <p>
 * This controller handles the root endpoint ("/") and prepares the data required
 * for the main dashboard view, including net liquidating value history,
 * trading statistics, and current market/bot status.
 */
@Controller
public class Main {
	@Autowired
	private BrokerManager broker;
	
	@Autowired
	private BotManager botManager;
	
	@Autowired
	private NetLiqHistoryRepository netLiqHistory;
	
	@Autowired
	private TradeManager tradeManager;
	
    /**
     * Retrieves all NetLiqHistory entities and maps them by ID.
     * 
     * @return a Map where the key is the ID and the value is the NetLiqHistory entity.
     */
    private Map<String, NetLiqHistory> findAllNetLiqHistoryAsMap() {
        List<NetLiqHistory> entities = (List<NetLiqHistory>) netLiqHistory.findAll();
        
        return entities.stream()
        		.collect(Collectors.toMap(NetLiqHistory::getId, entity -> entity));
    }
	
    /**
     * Populates the model with market status (open/closed) and active bot configuration.
     * 
     * @param model the Spring UI model to populate.
     */
    private void setMarketAndBotStatus(Model model) {
		String marketOpen = "";
		String marketClose = "";
		String marketStatus = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); 
		String message = "Regular Market Hours";
    	TradingHours th = TradingCalendar.getTradingHoursToday();
		if (th == null) {
			message = "No trading today.";
		} else{
			marketOpen = th.getOpen_time().format(formatter);
			marketClose = th.getClose_time().format(formatter);
			marketStatus = TradingCalendar.getMarketStatus();
			if (th.getIs_early_close())
				message = "Market closes early today because of " + th.getHoliday_name();
		}
    	model.addAttribute("message", message);
    	model.addAttribute("marketClose", marketClose);
    	model.addAttribute("marketOpen", marketOpen);
    	model.addAttribute("marketStatus", marketStatus);
        model.addAttribute("bot", botManager.getActiveConfiguration());
        model.addAttribute("scheduledTasks", botManager.getScheduledTasks());
    }
    
    /**
     * Handles requests to the root URL ("/") and renders the main dashboard.
     * Aggregates data from the broker, bot manager, and trade history to display
     * charts and status information.
     * 
     * @param model the Spring UI model.
     * @return the name of the view to render ("main").
     */
	@GetMapping("/")
	public String main(Model model) {
		BotConfiguration cfg = botManager.getActiveConfiguration();
		Map<String, NetLiqHistory> nlh = findAllNetLiqHistoryAsMap();
		
		if (!nlh.containsKey("USD")) nlh.put("USD", new NetLiqHistory("USD"));
		if (!nlh.containsKey("EUR")) nlh.put("EUR", new NetLiqHistory("EUR"));
		if (!nlh.containsKey("CashUsd")) nlh.put("CashUsd", new NetLiqHistory("CashUsd"));
		if (!nlh.containsKey("CashEur")) nlh.put("CashEur", new NetLiqHistory("CashEur"));
		
		List<DataPoint> dpUsd = nlh.get("USD").getDataPoints();
		List<DataPoint> dpEur = nlh.get("EUR").getDataPoints();
		List<DataPoint> dpCashUsd = nlh.get("CashUsd").getDataPoints();
		List<DataPoint> dpCashEur = nlh.get("CashEur").getDataPoints();

		LocalDate startDate = dpUsd.isEmpty() ? LocalDate.of(1980, 1, 1) : dpUsd.get(dpUsd.size() - 1).getDate();
		
		if (cfg != null) {
			List<AccountBalanceSnapshot> absUsd = broker.getAccountAllBalanceSnapshotsEodUsd(cfg.getAccountNumber(), startDate);
			for (AccountBalanceSnapshot day : absUsd) {
				if (day.getNetLiquidatingValue() != null && !day.getNetLiquidatingValue().isNaN() && day.getCashBalance() != null) {
					double rounded = Math.round(day.getNetLiquidatingValue()*100) / 100;
					double cash = Math.round(day.getCashBalance()*100) / 100;
					long date = day.getSnapshotDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
					dpUsd.add(new DataPoint(date, rounded));
					dpCashUsd.add(new DataPoint(date, cash));
				}
			}
			List<AccountBalanceSnapshot> absEur = broker.convertAccountBalanceSnapshotsUsdToEur(absUsd);
			for (AccountBalanceSnapshot day : absEur) {
				if (day.getNetLiquidatingValue() != null && !day.getNetLiquidatingValue().isNaN() && day.getCashBalance() != null) {
					double rounded = Math.round(day.getNetLiquidatingValue()*100) / 100;
					double cash = Math.round(day.getCashBalance()*100) / 100;
					long date = day.getSnapshotDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
					dpEur.add(new DataPoint(date, rounded));
					dpCashEur.add(new DataPoint(date, cash));
				}
			}
		}
		
		nlh.get("USD").addAll(dpUsd);
		nlh.get("EUR").addAll(dpEur);
		nlh.get("CashUsd").addAll(dpCashUsd);
		nlh.get("CashEur").addAll(dpCashEur);
		netLiqHistory.saveAll(nlh.values());
		
		// netLiqHistory.saveAll(datapointsEur);
		model.addAttribute("netLiqUsd", dpUsd);
		model.addAttribute("netLiqEur", dpEur);
		model.addAttribute("cashUsd", dpCashUsd);
		model.addAttribute("cashEur", dpCashEur);
		
		TradingStatistics stats = tradeManager.getTradingStatistics();
		model.addAttribute("trades", stats.getTrades());
		model.addAttribute("transactions", stats.getTransactions());
		model.addAttribute("metrics", stats.getMetrics());
		model.addAttribute("performance", stats.getPerformance());
		
		setMarketAndBotStatus(model);
		return "main"; // view
	}
}

package com.stockmarketpotato.tradelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TradelogView {
	@Autowired
	private TradeManager tradeManager;
	
	@GetMapping("/tradelog")
	public String tradelog(Model model) {
		TradingStatistics stats = tradeManager.getTradingStatistics();
		model.addAttribute("trades", stats.getTrades());
		model.addAttribute("transactions", stats.getTransactions());
		model.addAttribute("metrics", stats.getMetrics());
		model.addAttribute("performance", stats.getPerformance());
		return "tradelog";
	}



}

package com.stockmarketpotato.bot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stockmarketpotato.broker.BrokerManager;
import com.stockmarketpotato.feeds.AggregatedQuote;
import com.stockmarketpotato.feeds.QuoteRepository;

import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChain;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChainExpirations;

/**
 * Controller for managing Bot configurations via the web interface.
 * Handles creation, editing, deletion, and viewing of trading bots and their option chains.
 */
@Controller
public class BotView {
	@Autowired
	private BrokerManager brokerManager;
	
	@Autowired
	private BotManager botManager;
	
	@Autowired
	private QuoteRepository quotes;
    
    /**
     * Displays the form to create a new bot configuration.
     * 
     * @param model The Spring UI model.
     * @return The view name for editing/creating a bot.
     */
    @GetMapping("/bots/createBot")
    public String createBot(Model model) {
    	createModelForEditOrCreateBot(model);
    	model.addAttribute("form", new BotConfiguration());
        return "bots/editBot";
    }

    /**
     * Saves a bot configuration (new or updated).
     * 
     * @param bot The bot configuration object from the form.
     * @param model The Spring UI model.
     * @return Redirects to the main page.
     */
    @PostMapping("/bots/saveBot")
    public String saveBot(@ModelAttribute BotConfiguration bot, Model model) {
    	botManager.saveConfiguration(bot);
    	botManager.onConfigurationUpdate();
        model.addAttribute("bots", botManager.getAllBotConfigurations());
        return "redirect:/";
    }
    
    /**
     * Displays the form to edit an existing bot configuration.
     * 
     * @param id The ID of the bot to edit.
     * @param model The Spring UI model.
     * @return The view name for editing/creating a bot.
     */
    @GetMapping("/bots/editBot")
    public String editBot(@RequestParam("id") Long id, Model model) {
    	createModelForEditOrCreateBot(model);
    	model.addAttribute("form", botManager.findConfigurationById(id).get());
        return "bots/editBot";
    }

    /**
     * Helper method to populate the model with necessary data for the edit/create view.
     * Loads accounts, balances, and dropdown options for take profit/stop loss.
     * 
     * @param model The Spring UI model to populate.
     */
    private void createModelForEditOrCreateBot(Model model) {
    	List<Account> accounts = new ArrayList<>();
    	Map<String, Double> balances = new HashMap<>();
    	for (Account a : brokerManager.getAccounts()) {
    		if (a.margin_or_cash.equals("Margin")) {
    			accounts.add(a);
    			balances.put(a.account_number, brokerManager.getBalances(a.account_number).cash_balance);
    		}
    	}

    	// Short Puts only work with margin accounts
    	model.addAttribute("accounts", accounts);
    	model.addAttribute("balances", balances);
    	
        Map<String, String> takeProfitOptions = new LinkedHashMap<>();
		takeProfitOptions.put("0.025", "2.5% of credit");
		for (BigDecimal i = BigDecimal.valueOf(0.05); i.compareTo(BigDecimal.valueOf(1.0)) < 1; i = i
				.add(BigDecimal.valueOf(0.05))) {
			takeProfitOptions.put(String.format(Locale.ROOT, "%.2f", i),
					String.format(Locale.ROOT, "%.1f%% of credit", i.multiply(BigDecimal.valueOf(100.))));
		}
    	model.addAttribute("takeProfitOptions", takeProfitOptions);
    	
        Map<String, String> stopLossOptions = new LinkedHashMap<>();
        stopLossOptions.put("-0.05", "-5% of credit");
		for (BigDecimal i = BigDecimal.valueOf(0.05); i.compareTo(BigDecimal.valueOf(1.0)) < 1; i = i
				.add(BigDecimal.valueOf(0.05))) {
			stopLossOptions.put(String.format(Locale.ROOT, "-%.2f", i),
					String.format(Locale.ROOT, "-%.0f%% of credit", i.multiply(BigDecimal.valueOf(100.))));
		}
		for (BigDecimal i = BigDecimal.valueOf(1.1); i.compareTo(BigDecimal.valueOf(2.0)) < 1; i = i
				.add(BigDecimal.valueOf(0.1))) {
			stopLossOptions.put(String.format(Locale.ROOT, "-%.2f", i),
					String.format(Locale.ROOT, "-%.0f%% of credit", i.multiply(BigDecimal.valueOf(100.))));
		}
    	model.addAttribute("stopLossOptions", stopLossOptions);		
	}

	/**
     * Deletes a bot configuration.
     * 
     * @param id The ID of the bot to delete.
     * @param model The Spring UI model.
     * @return Redirects to the main page.
     */
	@GetMapping("/bots/deleteBot")
    public String deleteBot(@RequestParam("id") Long id, Model model) {
		botManager.deleteConfigurationById(id);
    	botManager.onConfigurationDelete();
        model.addAttribute("bots", botManager.getAllBotConfigurations());
        return "redirect:/";
    }
    
	/**
     * Displays the option chain for a specific bot based on its configuration parameters.
     * Shows filtered strikes and expirations relevant to the strategy.
     * 
     * @param botId The ID of the bot.
     * @param model The Spring UI model.
     * @return The view name for the option chain.
     */
	@GetMapping("/bots/chain")
	public String orders(@RequestParam("id") Long botId, Model model) {
		Optional<BotConfiguration> bot = botManager.findConfigurationById(botId); 
		if (bot.isPresent()) {
			String symbol = bot.get().getUnderlying().getDisplayValue();
			model.addAttribute("symbol", symbol);
			symbol = symbol.replace("/", "");
			final String account = bot.get().getAccountNumber();
			final Integer minDte = bot.get().getOpenDte().intValue();
			final Integer maxDte = minDte + 7;
			final BigDecimal maxDelta = bot.get().getMaxDelta();
			final BigDecimal minDelta = maxDelta.subtract(new BigDecimal("0.01")).max(new BigDecimal("0.00"));
			FuturesOptionChain chain = botManager.getFilteredOptionChain(account, symbol, minDte, maxDte, minDelta, maxDelta);
			model.addAttribute("chain", chain);
			Map<String, AggregatedQuote> quotesMap = new HashMap<>();
			for (AggregatedQuote q: quotes.findAll())
				quotesMap.put(q.getSymbol(), q);
			for (FuturesOptionChainExpirations e : chain.getExpirations()) {
				Optional<AggregatedQuote> quote = quotes.findById(e.getUnderlyingSymbol());
				if (quote.isPresent())
					quotesMap.put(e.getUnderlyingSymbol(), quote.get());
			}
			model.addAttribute("quotes", quotesMap);
			return "bots/chain";			
		}
		model.addAttribute("bots", botManager.getAllBotConfigurations());
		return "redirect:/";
	}
}
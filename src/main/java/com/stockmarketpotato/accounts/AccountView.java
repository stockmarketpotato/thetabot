package com.stockmarketpotato.accounts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.stockmarketpotato.broker.BrokerManager;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;

/**
 * Controller for handling account-related views.
 * Provides endpoints to view account lists, positions, and orders.
 */
@Controller
public class AccountView {
	@Autowired
	private BrokerManager brokerManager;

	/**
	 * Displays a list of all available brokerage accounts.
	 * 
	 * @param model the Spring UI model
	 * @return the view with a list of accounts
	 */
	@GetMapping("/accounts")
	public String accounts(Model model) {
		model.addAttribute("accounts", brokerManager.getAccounts());
		return "accounts";
	}

	/**
	 * Displays the positions for a specific account.
	 * Also generates alert links for short positions.
	 * 
	 * @param accountNumber the account number to view
	 * @param model the Spring UI model
	 * @return the view name for the positions page
	 */
	@GetMapping("/accounts/positions")
	public String positions(@RequestParam("no") String accountNumber, Model model) {
		model.addAttribute("accountNumber", accountNumber);
		model.addAttribute("balances", brokerManager.getBalances(accountNumber));
		List<AccountPosition> positions = brokerManager.prodGetPositionsWithQuote(accountNumber);
		model.addAttribute("positions", positions);
		Map<String, String> alertButtons = new HashMap<>();
		for (AccountPosition p : positions) {
			// Generate alert links only for short positions (selling options/stocks)
			if (p.quantity_direction.equals("Long"))
				continue;
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/feeds/addalert");
			builder.queryParam("account", p.account_number);
			builder.queryParam("symbol", p.symbol);
			builder.queryParam("streamer", p.streamerSymbol);
			builder.queryParam("open", p.average_open_price);
			alertButtons.put(p.symbol, builder.build().encode().toUriString());
		}
		model.addAttribute("alertButtons", alertButtons);
		return "accounts/positions";
	}

	/**
	 * Displays the live and historical orders for a specific account.
	 * 
	 * @param accountNumber the account number to view
	 * @param model the Spring UI model
	 * @return the view name for the orders page
	 */
	@GetMapping("/accounts/orders")
	public String orders(@RequestParam("no") String accountNumber, Model model) {
		model.addAttribute("accountNumber", accountNumber);
		model.addAttribute("liveOrders", brokerManager.getLiveOrders(accountNumber));
		model.addAttribute("orders", brokerManager.getOrders(accountNumber));
		return "accounts/orders";
	}
}

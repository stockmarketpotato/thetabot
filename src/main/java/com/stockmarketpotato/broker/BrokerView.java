package com.stockmarketpotato.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.annotation.PostConstruct;

@Controller
public class BrokerView {
	@Autowired
	private BrokerManager broker;

	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;

	@PostConstruct
	private void postConstruct() {
		if (tastytradeSettings.findAll().size() == 0)
			tastytradeSettings.save(new TastytradeSettings());
	}

	@GetMapping("/broker")
	public String broker(Model model) {
		model.addAttribute("settings", tastytradeSettings.findAll().get(0));
		return "broker";
	}

	@GetMapping("/broker/edit")
	public String editConfiguration(Model model) {
		model.addAttribute("settings", tastytradeSettings.findAll().get(0));
		return "broker/edit";
	}

	@PostMapping("/broker/save")
	public String save(@ModelAttribute TastytradeSettings t, Model model) {
		tastytradeSettings.save(t);
		broker.reconnect();
		model.addAttribute("settings", tastytradeSettings.findAll().get(0));
		return "redirect:/broker";
	}
}

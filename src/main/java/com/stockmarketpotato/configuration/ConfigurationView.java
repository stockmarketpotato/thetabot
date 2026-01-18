package com.stockmarketpotato.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.stockmarketpotato.main.NetLiqHistoryRepository;

import jakarta.annotation.PostConstruct;

@Controller
public class ConfigurationView {
	@Autowired
	private SettingsRepository settings;
	
	@Autowired
	private NetLiqHistoryRepository netLiqHistory;
	
	@PostConstruct
    private void postConstruct() {
        if (settings.findAll().size() == 0)
        	settings.save(new Settings());
	}

    @GetMapping("/configuration")
    public String configuration(Model model) {
    	model.addAttribute("settings", settings.findAll().get(0));
    	return "configuration";
    }

    @GetMapping("/configuration/edit")
    public String editConfiguration(Model model) {
        model.addAttribute("settings", settings.findAll().get(0));
        return "configuration/editConfiguration";
    }
    
    @PostMapping("/configuration/save")
    public String save(@ModelAttribute Settings s, Model model) {
        settings.save(s);
    	model.addAttribute("settings", settings.findAll().get(0));
        return "redirect:/configuration";
    }
    
    @GetMapping("/configuration/clearNetLiqHistory")
    public String clearNetLiqHistory(Model model) {
    	netLiqHistory.deleteAll();
    	return "redirect:/";
    }
}

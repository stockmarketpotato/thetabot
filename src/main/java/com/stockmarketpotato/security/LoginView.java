package com.stockmarketpotato.security;

import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginView {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messages;

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public ModelAndView login(final HttpServletRequest request, final ModelMap model,
			@RequestParam("messageKey") final Optional<String> messageKey,
			@RequestParam("error") final Optional<String> error) {

		if (userService.acceptNewUser()) {
			logger.info("No users registered. Redirect to user registration.");
			return new ModelAndView("redirect:/user/registration");
		}

		messageKey.ifPresent(key -> {
			String message = messages.getMessage(key, null, Locale.US);
			model.addAttribute("message", message);
		});

		error.ifPresent(e -> model.addAttribute("error", e));

		return new ModelAndView("login", model);
	}
}

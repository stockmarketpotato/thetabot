package com.stockmarketpotato.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messages;

    public RegistrationController() {
        super();
    }

    // API

    @GetMapping("/user/registration")
    public String showRegistrationForm(final HttpServletRequest request, final Model model) {
    	logger.info("No new user registrations accepted. Redirect to login.");
    	if (!userService.acceptNewUser())
    		return "redirect:/login";

    	logger.info("Rendering registration page.");
    	final UserDto accountDto = new UserDto();
        model.addAttribute("user", accountDto);
        return "user/registration";
    }

	// Registration
	@PostMapping("/user/registration")
	public ModelAndView registerUserAccount(@Valid UserDto userDto, final HttpServletRequest request) {
		logger.info("Registering user account with information: {}", userDto);

		try {
			User registered = userService.registerNewUserAccount(userDto);
			registered.setEnabled(true);
			logger.info("User account registered and enabled.");	
		} catch (final UserAlreadyExistException uaeEx) {
			ModelAndView mav = new ModelAndView("user/registration", "user", userDto);
			String errMessage = messages.getMessage("message.regError", null, request.getLocale());
			mav.addObject("message", errMessage);
			logger.warn(errMessage);
			return mav;
		} catch (final RuntimeException ex) {
			logger.warn("Unable to register user", ex);
			return new ModelAndView("emailError", "user", userDto);
		}
		return new ModelAndView("user/successRegister", "user", userDto);
    }
    
}

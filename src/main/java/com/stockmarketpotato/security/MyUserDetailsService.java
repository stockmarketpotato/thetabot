package com.stockmarketpotato.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginAttemptService loginAttemptService;

	public MyUserDetailsService() {
		super();
	}

	// API

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		if (loginAttemptService.isBlocked()) {
			throw new RuntimeException("blocked");
		}

		try {
			final User user = userRepository.findByEmail(email);
			if (user == null) {
				throw new UsernameNotFoundException("No user found with username: " + email);
			}
			final List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("READ_PRIVILEGE"));
			authorities.add(new SimpleGrantedAuthority("WRITE_PRIVILEGE"));
			authorities.add(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"));
			org.springframework.security.core.userdetails.User u = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					true, true, true, true, authorities);
			return u;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}

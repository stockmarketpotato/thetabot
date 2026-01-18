package com.stockmarketpotato.security;

import com.stockmarketpotato.security.validation.PasswordMatches;
import com.stockmarketpotato.security.validation.ValidEmail;
import com.stockmarketpotato.security.validation.ValidPassword;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class UserDto {
    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;
    
    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [email=")
                .append(email)
                .append("]");
        return builder.toString();
    }

    
}
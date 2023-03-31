package com.bozo.issuetracker.filter;

import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class VerifyAccessFilter extends GenericFilterBean {

    private ApplicationUserDetailsService applicationUserDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(authentication.getName());
//            if (userDetails instanceof ApplicationUser) {
//                User byUserName = ((ApplicationUser) userDetails).getUser();
//                if (authentication.getPrincipal() instanceof ApplicationUser) {
//                    User user = ((ApplicationUser) authentication.getPrincipal()).getUser();
//                    if (user.getRole() != byUserName.getRole()) {
//                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
//                    }
//                }
//            }
//        }

        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).ifPresent(auth -> {
            if (auth.getPrincipal() instanceof ApplicationUser) {
                UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(auth.getName());
                User user = ((ApplicationUser) auth.getPrincipal()).getUser();
                if (userDetails
                        .getAuthorities()
                        .stream().noneMatch(grantedAuthority ->
                                grantedAuthority
                                        .getAuthority()
                                        .equalsIgnoreCase("ROLE_" + user.getRole()))){
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            userDetails.getPassword(),
                                            userDetails.getAuthorities()));
                }
            }
        });

        chain.doFilter(request, response);
    }

}

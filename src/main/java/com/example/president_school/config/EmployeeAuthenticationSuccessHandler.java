package com.example.president_school.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class EmployeeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1,
			Authentication authentication) throws IOException, ServletException {

		boolean hasUserRole = false;
		boolean hasAdminRole = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_TEACHER")) {
				hasUserRole = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				hasAdminRole = true;
				break;
			}
		}

		if (hasUserRole) {
			redirectStrategy.sendRedirect(arg0, arg1, "/api/teacher/dashboard");
		} else if (hasAdminRole) {
			redirectStrategy.sendRedirect(arg0, arg1, "/api/admin/dashboard");
		} else {
			throw new IllegalStateException();
		}
	}
}

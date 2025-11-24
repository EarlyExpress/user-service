package com.early_express.user_service.infrastructure.security.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class KeycloakClientRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {

		Object roles = jwt.getClaim("roles");
		if (!(roles instanceof Collection)) return Collections.emptyList();

		return ((Collection<?>)roles).stream()
									 .map(Object::toString)
									 .filter(role -> role.startsWith("ROLE_"))
									 .map(SimpleGrantedAuthority::new)
									 .collect(Collectors.toList());
	}
}

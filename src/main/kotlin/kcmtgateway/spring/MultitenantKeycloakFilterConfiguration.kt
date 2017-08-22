package kcmtgateway.spring

import org.keycloak.adapters.servlet.KeycloakOIDCFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.stereotype.Component

@Component
@ConditionalOnWebApplication
@ConditionalOnBean(name = arrayOf("multitenantKeycloakConfigurationResolver"))
@ConditionalOnProperty("kcmt.enabled")
class MultitenantKeycloakFilterConfiguration(resolver: MultitenantKeycloakConfigurationResolver) : FilterRegistrationBean() {
	init {
		this.filter = KeycloakOIDCFilter(resolver)
		this.addUrlPatterns("/*")
		this.setName("keycloakSecurityFilter")
		this.order = 10
	}
}
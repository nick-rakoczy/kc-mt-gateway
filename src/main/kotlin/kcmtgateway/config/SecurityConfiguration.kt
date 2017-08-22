package kcmtgateway.config

import org.keycloak.common.enums.SslRequired
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kcmt.realms", ignoreUnknownFields = true)
class SecurityConfiguration {
	var baseUrl: String? = null
	var sslRequired: SslRequired = SslRequired.EXTERNAL
	var providers: List<SecurityConfigurationItem> = ArrayList()
}
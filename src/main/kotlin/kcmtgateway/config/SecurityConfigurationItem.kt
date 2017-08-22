package kcmtgateway.config

import org.keycloak.common.enums.SslRequired

class SecurityConfigurationItem {
	lateinit var hostname: String
	lateinit var realm: String
	lateinit var clientId: String
	lateinit var clientSecret: String

	var baseUrl: String? = null
	var sslRequired: SslRequired? = null
}
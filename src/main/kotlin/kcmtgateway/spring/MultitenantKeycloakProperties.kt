package kcmtgateway.spring

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = "kcmt", ignoreUnknownFields = true)
class MultitenantKeycloakProperties {
	var enabled: Boolean = false
	var realmConfigurationClass: Class<*>? = null

	var rootRedirect: String? = null

	@NestedConfigurationProperty
	val services: Map<String, ProxyPass> = HashMap()

	class ProxyPass {
		var src: String? = null
		var dst: String? = null
		var requireIssuer: String? = null
		var requireRole: List<String> = ArrayList()
	}
}
package kcmtgateway

import org.keycloak.adapters.KeycloakDeploymentBuilder
import org.keycloak.common.enums.SslRequired
import org.keycloak.representations.adapters.config.AdapterConfig

data class RealmConfiguration(
		val baseUrl: String,
		val realm: String,
		val clientId: String,
		val clientSecret: String,
		val sslRequired: SslRequired
) {
	val adapterConfig = AdapterConfig().also {
		it.realm = this.realm
		it.resource = this.clientId
		it.credentials["secret"] = this.clientSecret
		it.sslRequired = this.sslRequired.name
		it.authServerUrl = this.baseUrl
	}

	val keycloakDeployment = KeycloakDeploymentBuilder.build(adapterConfig)!!
}
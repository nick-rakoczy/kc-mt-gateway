package kcmtgateway.config

import org.keycloak.adapters.spi.HttpFacade
import kcmtgateway.RealmConfiguration
import kcmtgateway.RealmConfigurationProvider
import java.net.URI

class ConfigurationDrivenSecurityPropertyResolver(private val config: SecurityConfiguration) : RealmConfigurationProvider {
	override fun getRealmConfiguration(req: HttpFacade.Request): RealmConfiguration? {
		val hostname = req.uri.let(::URI).host
		val provider = config.providers.find {
			it.hostname == hostname
		}

		return provider?.let {
			RealmConfiguration(
					baseUrl = it.baseUrl ?: config.baseUrl ?: error(""),
					realm = it.realm,
					clientId = it.clientId,
					clientSecret = it.clientSecret,
					sslRequired = it.sslRequired ?: config.sslRequired
			)
		}
	}
}
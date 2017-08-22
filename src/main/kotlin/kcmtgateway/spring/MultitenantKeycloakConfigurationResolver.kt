package kcmtgateway.spring

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.spi.HttpFacade
import kcmtgateway.RealmConfigurationProvider
import kcmtgateway.config.SecurityConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty("kcmt.enabled")
@EnableConfigurationProperties(MultitenantKeycloakProperties::class, SecurityConfiguration::class)
class MultitenantKeycloakConfigurationResolver(private val properties: MultitenantKeycloakProperties) : KeycloakConfigResolver, ApplicationContextAware {

	private var realmConfigurationProvider: RealmConfigurationProvider? = null

	override fun setApplicationContext(context: ApplicationContext?) {
		context?.autowireCapableBeanFactory?.createBean(properties.realmConfigurationClass)?.let {
			realmConfigurationProvider = it as RealmConfigurationProvider
		}
	}

	override fun resolve(req: HttpFacade.Request) = req.let {
		realmConfigurationProvider?.getRealmConfiguration(it)?.keycloakDeployment
	}
}

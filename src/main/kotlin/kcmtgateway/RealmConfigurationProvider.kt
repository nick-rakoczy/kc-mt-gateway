package kcmtgateway

import org.keycloak.adapters.spi.HttpFacade

interface RealmConfigurationProvider {
	fun getRealmConfiguration(req: HttpFacade.Request): RealmConfiguration?
}
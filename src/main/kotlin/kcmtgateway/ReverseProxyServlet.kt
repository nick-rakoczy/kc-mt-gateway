package kcmtgateway

import com.google.gson.GsonBuilder
import io.undertow.Handlers
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient
import org.keycloak.KeycloakPrincipal
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ReverseProxyServlet(
		private val forwardTo: URI,
		private val requiredIssuer: String?,
		private val requiredRoles: List<String>?
) : UndertowHttpServlet() {
	private val logger = LoggerFactory.getLogger(ReverseProxyServlet::class.java.name)

	private val dispatcher by lazy {
		LoadBalancingProxyClient().apply {
			addHost(forwardTo)
		}.let {
			Handlers.proxyHandler(it)
		}
	}

	data class LogEvent(private val uri: String?,
						private val query: Map<String, Array<String>>,
						private val issuer: String?,
						private val subject: String?,
						private val username: String?) {
		override fun toString(): String = gson.toJson(this)

		companion object {
			val gson by lazy {
				GsonBuilder()
						.disableHtmlEscaping()
						.serializeNulls()
						.create()
			}
		}
	}

	override fun process(req: HttpServletRequest, res: HttpServletResponse, dispatch: (HttpHandler) -> HttpServerExchange) {
		val principal = req.userPrincipal as? KeycloakPrincipal<*>
		val resourceAccess = principal?.keycloakSecurityContext?.token?.getResourceAccess("test-env")
		val issuer = principal?.keycloakSecurityContext?.token?.issuer

		val rolesPass = requiredRoles?.all {
			resourceAccess?.isUserInRole(it) ?: false
		} ?: true

		val issuerPass = requiredIssuer?.let {
			it == issuer
		} ?: true

		val permitted = rolesPass && issuerPass

		if (!permitted) {
			res.sendError(HttpStatus.FORBIDDEN.value())
			return
		}

		LogEvent(req.requestURI, req.parameterMap, issuer, principal?.name, principal?.keycloakSecurityContext?.token?.preferredUsername).toString().let(logger::info)

		dispatch(this.dispatcher)

	}
}
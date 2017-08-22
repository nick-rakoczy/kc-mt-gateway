package kcmtgateway.spring

import kcmtgateway.ReverseProxyServlet
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Configuration
import java.net.URI
import javax.servlet.ServletContext

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty("kcmt.enabled")
@EnableConfigurationProperties(MultitenantKeycloakProperties::class)
class ReverseProxyServletConfiguration(val properties: MultitenantKeycloakProperties) : ServletContextInitializer {
	private val logger = LoggerFactory.getLogger(ReverseProxyServletConfiguration::class.java)

	override fun onStartup(context: ServletContext?) {
		if (context == null) {
			return
		}

		properties.services.entries.forEach {
			val forwardToUri = URI.create(it.value.dst)
			val servlet = ReverseProxyServlet(
					forwardTo = forwardToUri,
					requiredIssuer = it.value.requireIssuer,
					requiredRoles = it.value.requireRole
			)

			context.addServlet(it.key, servlet).apply {
				setLoadOnStartup(1)
				setAsyncSupported(true)
				addMapping("${it.value.src}/*")
				logger.info("Mapping servlet: '${it.value.src}' to [${it.value.dst}]")
			}
		}
	}
}

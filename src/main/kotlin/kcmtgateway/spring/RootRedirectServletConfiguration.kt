package kcmtgateway.spring

import kcmtgateway.RedirectServlet
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Configuration
import javax.servlet.ServletContext

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty("kcmt.enabled")
@EnableConfigurationProperties(MultitenantKeycloakProperties::class)
class RootRedirectServletConfiguration(val properties: MultitenantKeycloakProperties) : ServletContextInitializer {
	private val logger = LoggerFactory.getLogger(RootRedirectServletConfiguration::class.java)

	override fun onStartup(context: ServletContext?) {
		if (context == null) {
			return
		}

		properties.rootRedirect?.let(::RedirectServlet)?.let {
			context.addServlet("root-redirect-servlet", it)
		}?.apply {
			setLoadOnStartup(1)
			setAsyncSupported(true)
			addMapping("/", "")
			logger.info("Mapping root redirect servlet: ${properties.rootRedirect}")
		}
	}
}
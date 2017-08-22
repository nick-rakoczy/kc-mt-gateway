package kcmtgateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class GatewayApp {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(GatewayApp::class.java, *args)
		}
	}
}
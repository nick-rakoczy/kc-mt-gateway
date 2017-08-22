package kcmtgateway

import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.spec.HttpServletRequestImpl
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse

abstract class UndertowHttpServlet : HttpServlet() {
	private val logger = LoggerFactory.getLogger(UndertowHttpServlet::class.java.name)

	tailrec fun unwrapToUndertowRequest(req: HttpServletRequest?): HttpServletRequestImpl? {
		if (req == null) {
			return null
		}

		if (req is HttpServletRequestImpl) {
			return req
		}

		if (req is HttpServletRequestWrapper) {
			val req2 = req.request
			if (req2 is HttpServletRequest) {
				return unwrapToUndertowRequest(req2)
			}
		}

		return null
	}


	override final fun service(req: HttpServletRequest?, res: HttpServletResponse?) {
		if (req != null && res != null) {
			val req2 = unwrapToUndertowRequest(req) ?: error("Unknown response type")
			this.process(req, res, req2.exchange::dispatch)
		}
	}

	abstract fun process(req: HttpServletRequest, res: HttpServletResponse, dispatch: (HttpHandler) -> HttpServerExchange)
}
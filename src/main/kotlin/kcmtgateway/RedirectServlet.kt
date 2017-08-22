package kcmtgateway

import io.undertow.Handlers
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RedirectServlet(redirectTo: String) : UndertowHttpServlet() {
	private val dispatcher by lazy {
		Handlers.redirect(redirectTo)
	}

	override fun process(req: HttpServletRequest, res: HttpServletResponse, dispatch: (HttpHandler) -> HttpServerExchange) {
		dispatch(dispatcher)
	}
}
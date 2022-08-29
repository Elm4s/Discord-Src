package de.corruptedbytes.webserver.webserverindexes.sites;

import java.io.File;
import java.io.IOException;

import com.sun.net.httpserver.HttpServer;

import de.corruptedbytes.User;
import de.corruptedbytes.utils.Config;
import de.corruptedbytes.webserver.WebServerIndex;
import de.corruptedbytes.webserver.WebServerUtil;

public class Panel implements WebServerIndex {

	@Override
	public void index(HttpServer httpServer) throws IOException {
		httpServer.createContext("/panel", exchange -> {
			if (!exchange.getRequestMethod().equals("GET")) {
				exchange.close();
				return;
			}
			
			if (!Config.CONFIG_FILE.exists()) {
				WebServerUtil.redirect(exchange, "/setup");
			} else {
				String session = WebServerUtil.getCookie(exchange, "session");
				if (new User(session).verifyUser()) {
					WebServerUtil.sendFile(exchange, 200, new File(Config.WEB_DIRECTORY + "/panel.html"));
				} else {
					WebServerUtil.redirect(exchange, "/login");
				}
			}
		});
	}

}

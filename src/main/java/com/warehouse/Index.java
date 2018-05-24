package com.warehouse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Index implements ErrorController {
	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}

	@Override
	@RequestMapping("/error")
	public String getErrorPath() {
		return "404";
	}
}

package com.warehouse;

import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {
	private String defaultLayout = "layout";
	private String viewAttributeName = "view";

	public void setDefaultLayout(String defaultLayout) {
		Assert.hasLength(defaultLayout, "Name must not be empty");
		this.defaultLayout = defaultLayout;
	}

	public void setViewAttributeName(String viewAttributeName) {
		Assert.hasLength(defaultLayout, "Name must not be empty");
		this.viewAttributeName = viewAttributeName;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView == null || !modelAndView.hasView()) {
			return;
		}
		String originalViewName = modelAndView.getViewName();
		if (isRedirectOrForward(originalViewName)) {
			return;
		}
		System.out.println("originalViewName : " + originalViewName);
		String layoutName = originalViewName;
		if(handler instanceof HandlerMethod)
			layoutName = getLayoutName(handler);
		
		modelAndView.setViewName(layoutName);
		modelAndView.addObject(this.viewAttributeName, originalViewName);
	}

	private boolean isRedirectOrForward(String viewName) {
		return viewName.startsWith("redirect:")
				|| viewName.startsWith("forward:");
	}

	private String getLayoutName(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Layout layout = getMethodOrTypeAnnotation(handlerMethod);
		if (layout == null) {
			return this.defaultLayout;
		} else {
			return layout.value();
		}
	}

	private Layout getMethodOrTypeAnnotation(HandlerMethod handlerMethod) {
		Layout layout = handlerMethod.getMethodAnnotation(Layout.class);
		if (layout == null) {
			return handlerMethod.getBeanType().getAnnotation(Layout.class);
		}
		return layout;
	}
}
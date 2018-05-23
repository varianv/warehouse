package com.warehouse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
//	@Value("${default.locale.arg0}")
//	private String localeArg0;
//	@Value("${default.locale.arg1}")
//	private String localeArg1;
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/bootstrap").setViewName("bootstrap");
        registry.addViewController("/loginbasic").setViewName("loginbasic");
    }
	
	/* Uncomment this section if want to use ReloadableResourceBundleMessageSource instead of MessageSourceAutoConfiguration
	 * 
	@Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(1);
        return messageSource;
    }*/
    
//    @Bean(name="localeResolver")
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(new Locale(localeArg0, localeArg1));
//        return slr;
//    }
    
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        lci.setParamName("lang");
//        return lci;
//    }
    
//    //This bean is to override bean on ErrorMvcAutoConfiguration.class, so we can customize the error attributes
//    @Bean
//    public CustomErrorAttributes errorAttributes() {
//		return new CustomErrorAttributes();
//	}
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//    	registry.addInterceptor(localeChangeInterceptor());
    	registry.addInterceptor(new ThymeleafLayoutInterceptor());
    }
    
//    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(-1);
//        return multipartResolver;
//    }
}

package com.onap.configdb.app.main;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import static com.google.common.base.Predicates.*;
import static com.google.common.collect.Lists.*;
import static springfox.documentation.builders.PathSelectors.*;
//param
/**
 * Class for Swagger Configuration
 * @author Devendra Chauhan
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private ApiInfo apiInfo() {
	    springfox.documentation.service.Contact contact=new springfox.documentation.service.Contact("Devendra Chauhan",
	            "","devendra.chauhan@techmahindra.com");

	    return new ApiInfoBuilder()
	            .title("SDNC ConfigDB  API")
	            .description("SDNC ConfigDB API")
	            .contact(contact)
	            .version("3.0.0")
	            .build();
	}
	
    @Bean
   
    public Docket productApi() {
    	
		
		
    	//ApiInfo apiInfo = new ApiInfo("SDNC Config db API", "SDNC Config db API", "v3.0","xxxx",contact,"Apache 2.0","http://www.apache.org/licenses/LICENSE-2.0");
		//ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder().title()contact(contact).build();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("com.onap.configdb.web.controller"))
                .paths(regex("/api/sdnc-config-db/v3.*"))
                .build();
    }
    
   
}

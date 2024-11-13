package schwarz.jobs.interview.coupon.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Profile("dev") // if we have diferent environments (e.g. development, production, stagging, we can 
				// enable or disable the documentation depending on the profile
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("schwarz.jobs.interview"))
            .paths(PathSelectors.any())
            .build()
            .apiInformation();
    }
    
    // If we want to costumize our API documentation's metadata
    private ApiInfo apiInformation() {
    	return new ApiInfoBuilder()
            .title("Company API")
            .description("API documentation")
            .version("1.0.0")
            .contact(new Contact("Company Support", "urlCompany", "urlSuport"))
            .license("Company License")
            .licenseUrl("urlCompany/license")
            .build();
    }
}


package com.taskify.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String TITLE = "Taskify API";
  private static final String DESCRIPTION =
      "API for managing tasks and task lists within the Taskify application.";
  private static final String VERSION = "1.0.0";
  private static final String CONTACT_NAME = "Sagar Nath";
  private static final String CONTACT_URL = "https://github.com/nathsagar96";
  private static final String LICENSE_NAME = "MIT";
  private static final String LICENSE_URL = "https://opensource.org/licenses/MIT";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title(TITLE)
                .description(DESCRIPTION)
                .version(VERSION)
                .contact(new Contact().name(CONTACT_NAME).url(CONTACT_URL))
                .license(new License().name(LICENSE_NAME).url(LICENSE_URL)));
  }
}

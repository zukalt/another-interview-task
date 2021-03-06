package ru.oskelly.interview.task;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories(value = "ru.oskelly.interview.task.repositories")
@EntityScan("ru.oskelly.interview.task.model")
@EnableAsync
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

    @Bean
    public Docket swaggerSpringMvcPluginRest() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.oskelly.interview.task.api"))
                .paths(PathSelectors.any())
                .build()
                .genericModelSubstitutes(ResponseEntity.class, CompletableFuture.class)
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfoBuilder()
                        .title("OSKELLY.ru")
                        .description("Demo API docs")
                        .version("1.0.0")
                        .build());
    }

    @Bean
    public WebMvcConfigurer configureMvc() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/webjars/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/");
                registry.addResourceHandler("/swagger-ui.html**")
                        .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
            }
        };
    }

    @Bean("comments.queue")
    public Queue newComments() {
        return new Queue("comments.new");
    }

    @Bean("notifications.queue")
    public Queue newCommentNotifications() {
        return new Queue("comments.notify");
    }

}

package com.api;

import java.text.SimpleDateFormat;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.api.filter.DebugFilter;
import com.api.filter.TokenFilter;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Classe Principal
 *
 * @author juliano.ezequiel
 *
 */
@SpringBootApplication
@EnableScheduling
public class SimpleApiApplication extends SpringBootServletInitializer {

    @Value("${proxy.user}")
    private String username;
    @Value("${proxy.password}")
    private String password;
    @Value("${proxy.host}")
    private String proxyUrl;
    @Value("${proxy.port}")
    private Integer port;
    
    @Autowired
    private TokenFilter tokenFilter;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(username, password, null, null));

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(proxyUrl, port));
        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

        CloseableHttpClient client = clientBuilder.build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);

        return new RestTemplate(factory);

    }

    // filtro para debug,
    @Bean
    public FilterRegistrationBean filtroDebug() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new DebugFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }

    // filtro para as requisicoes restritas
    @Bean
    public FilterRegistrationBean filtroJwt() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(tokenFilter);
        bean.addUrlPatterns("/restrict/*");

        return bean;
    }

    // configurar o mapper para não enviar \r\n no json
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(false).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        b.failOnUnknownProperties(false);
        b.configure(mapper);
        return b;
    }

    // configura o multipart
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("10MB");
        factory.setLocation(System.getProperty("java.io.tmpdir"));
        return factory.createMultipartConfig();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // Customize the application or call application.sources(...) to add
        // sources
        // Since our example is itself a @Configuration class (via
        // @SpringBootApplication)
        // we actually don't need to override this method.
        return application.sources(SimpleApiApplication.class);
    }

    // Start inicial do app
    public static void main(String[] args) {
        SpringApplication.run(SimpleApiApplication.class, args);
    }
}

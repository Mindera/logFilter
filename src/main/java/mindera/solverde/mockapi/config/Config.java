package mindera.solverde.mockapi.config;

import mindera.solverde.mockapi.service.RequestResponseInterceptor;
import mindera.solverde.mockapi.service.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class Config {

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter(){
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean =
                new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestResponseLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors
                = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new RequestResponseInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}


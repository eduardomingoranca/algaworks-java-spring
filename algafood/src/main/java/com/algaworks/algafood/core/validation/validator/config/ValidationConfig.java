package com.algaworks.algafood.core.validation.validator.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

//    LocalValidatorFactoryBean -> responsavel por fazer a integracao e configuracao do bean validation
//    com o spring
//    MessageSource -> responsavel por ler o arquivo de mensagens [messages.properties] e fazer a resolucao
//    de mensagens
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}

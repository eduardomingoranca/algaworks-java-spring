package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import static org.springframework.boot.WebApplicationType.NONE;

public class InclusaoCozinhaMain {

    public static void main(String[] args) {
        // configurando a aplicacao para nao web
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(NONE)
                .run(args);

        CadastroCozinha cadastroCozinha = applicationContext.getBean(CadastroCozinha.class);

        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Brasileira");

        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("Japonesa");

        cozinha = cadastroCozinha.salvar(cozinha);
        cozinha2 = cadastroCozinha.salvar(cozinha2);

        System.out.printf("%d - %s\n", cozinha.getId(), cozinha.getNome());
        System.out.printf("%d - %s\n", cozinha2.getId(), cozinha2.getNome());
    }
}
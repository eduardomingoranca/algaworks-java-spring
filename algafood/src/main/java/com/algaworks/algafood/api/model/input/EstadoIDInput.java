package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class EstadoIDInput {
    @NotNull
    private Long id;

}

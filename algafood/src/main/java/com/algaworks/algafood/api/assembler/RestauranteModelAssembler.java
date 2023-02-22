package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class RestauranteModelAssembler {

    public RestauranteModel toModel(Restaurante restaurante) {
        CozinhaModel cozinhaModel = new CozinhaModel();
        cozinhaModel.setId(restaurante.getCozinha().getId());
        cozinhaModel.setNome(restaurante.getCozinha().getNome());

        // conversao da entidade Restaurante para RestauranteModel
        RestauranteModel restauranteModel = new RestauranteModel();
        restauranteModel.setId(restaurante.getId());
        restauranteModel.setNome(restaurante.getNome());
        restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
        restauranteModel.setCozinha(cozinhaModel);

        return restauranteModel;
    }

    public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
        // convertendo cada restaurante em restaurante model
//        return restaurantes.stream().map(restaurante -> toModel(restaurante)).collect(Collectors.toList());
        return restaurantes.stream().map(this::toModel).collect(toList());
    }

}

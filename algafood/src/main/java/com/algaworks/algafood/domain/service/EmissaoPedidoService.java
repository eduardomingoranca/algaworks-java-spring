package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;

@Service
public class EmissaoPedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamento;

    @Autowired
    private CadastroUsuarioService cadastroUsuario;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Autowired
    private CadastroProdutoService cadastroProduto;

    @Transactional
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido buscarOuFalhar(String codigo) {
        return pedidoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new PedidoNaoEncontradoException(codigo));
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        pedido.setCliente(new Usuario());
        pedido.getCliente().setId(1L);

        Long cidadeID = pedido.getEnderecoEntrega().getCidade().getId();
        Long clienteID = pedido.getCliente().getId();
        Long restauranteID = pedido.getRestaurante().getId();
        Long formaPagamentoID = pedido.getFormaPagamento().getId();

        Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeID);
        Usuario cliente = cadastroUsuario.buscarOuFalhar(clienteID);
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteID);
        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoID);

        pedido.getEnderecoEntrega().setCidade(cidade);
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setFormaPagamento(formaPagamento);

        if (naoAceitaFormaPagamento(restaurante, formaPagamento)) {
            throw new NegocioException(format("A forma de pagamento %s nao eh aceita por esse restaurante.",
                    formaPagamento.getDescricao()));
        }

        pedido.getItens().forEach(item -> {
            Produto produto = cadastroProduto.buscarOuFalhar(
                    pedido.getRestaurante().getId(), item.getProduto().getId());

            item.setPedido(pedido);
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
        });

        pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
        calcularValorTotal(pedido);

        return pedidoRepository.save(pedido);
    }

    private void calcularValorTotal(Pedido pedido) {
        pedido.getItens().forEach(this::calcularPrecoTotal);

        BigDecimal subtotal = pedido.getItens().stream()
                .map(ItemPedido::getPrecoTotal)
                .reduce(ZERO, BigDecimal::add);

        pedido.setSubtotal(subtotal);
        BigDecimal taxaFrete = pedido.getTaxaFrete();

        BigDecimal valorTotal = pedido.getSubtotal().add(taxaFrete);
        pedido.setValorTotal(valorTotal);

    }

    private void calcularPrecoTotal(ItemPedido itemPedido) {
        BigDecimal precoUnitario = itemPedido.getPrecoUnitario();
        Integer quantidade = itemPedido.getQuantidade();

        if (precoUnitario == null)
            precoUnitario = ZERO;

        if (quantidade == null)
            quantidade = 0;

        BigDecimal precoTotal = precoUnitario.multiply(new BigDecimal(quantidade));

        itemPedido.setPrecoTotal(precoTotal);
    }

    private boolean aceitaFormaPagamento(Restaurante restaurante, FormaPagamento formaPagamento) {
        return restaurante.getFormasPagamento().contains(formaPagamento);
    }

    private boolean naoAceitaFormaPagamento(Restaurante restaurante, FormaPagamento formaPagamento) {
        return !aceitaFormaPagamento(restaurante, formaPagamento);
    }

}

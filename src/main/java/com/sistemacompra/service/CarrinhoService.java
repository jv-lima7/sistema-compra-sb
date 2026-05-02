package com.sistemacompra.service;

import com.sistemacompra.model.ItemCarrinho;
import com.sistemacompra.model.Produto;
import com.sistemacompra.model.Usuario;
import com.sistemacompra.repository.CarrinhoRepository;
import com.sistemacompra.repository.ProdutoRepository;
import com.sistemacompra.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ProdutoRepository produtoRepository,
                           UsuarioRepository usuarioRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ItemCarrinho> listarItens(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        return carrinhoRepository.findByUsuario(usuario);
    }

    public double calcularTotal(Long usuarioId) {
        return listarItens(usuarioId).stream()
                .mapToDouble(ItemCarrinho::getSubtotal)
                .sum();
    }

    public ItemCarrinho adicionarItem(Long usuarioId, String nomeProduto, int quantidade) {
        Usuario usuario = buscarUsuario(usuarioId);

        Produto produto = produtoRepository.findByNomeIgnoreCase(nomeProduto)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        if (produto.getQuantidade() < quantidade)
            throw new IllegalArgumentException("Estoque insuficiente.");

        Optional<ItemCarrinho> existente = carrinhoRepository
                .findByUsuarioAndProduto_NomeIgnoreCase(usuario, nomeProduto);

        if (existente.isPresent()) {
            ItemCarrinho item = existente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
            return carrinhoRepository.save(item);
        }

        ItemCarrinho novoItem = new ItemCarrinho();
        novoItem.setUsuario(usuario);
        novoItem.setProduto(produto);
        novoItem.setQuantidade(quantidade);
        return carrinhoRepository.save(novoItem);
    }

    public void removerItem(Long usuarioId, Long itemId) {
        ItemCarrinho item = carrinhoRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));

        if (!item.getUsuario().getId().equals(usuarioId))
            throw new IllegalArgumentException("Item não pertence a este usuário.");

        carrinhoRepository.delete(item);
    }

    @Transactional
    public void finalizarCompra(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        List<ItemCarrinho> itens = carrinhoRepository.findByUsuario(usuario);

        if (itens.isEmpty())
            throw new IllegalArgumentException("Carrinho vazio.");

        for (ItemCarrinho item : itens) {
            Produto produto = item.getProduto();
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoRepository.save(produto);
        }

        carrinhoRepository.deleteByUsuario(usuario);
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }
}

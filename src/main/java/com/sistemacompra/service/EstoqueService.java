package com.sistemacompra.service;

import com.sistemacompra.model.Produto;
import com.sistemacompra.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;

    public EstoqueService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeIgnoreCase(nome);
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
}
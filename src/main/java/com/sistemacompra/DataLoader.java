package com.sistemacompra;

import com.sistemacompra.model.Produto;
import com.sistemacompra.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;

    public DataLoader(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) {
        if (produtoRepository.count() == 0) {
            produtoRepository.save(new Produto("iPhone 13",          3999.99, 10));
            produtoRepository.save(new Produto("iPhone 13 Pro",      5499.99,  8));
            produtoRepository.save(new Produto("iPhone 13 Pro Max",  6499.99,  5));
            produtoRepository.save(new Produto("iPhone 14 Pro",      7999.99,  7));
            produtoRepository.save(new Produto("iPhone 14 Pro Max",  8999.99,  4));
            produtoRepository.save(new Produto("iPhone 15 Pro",      9499.99,  6));
            produtoRepository.save(new Produto("iPhone 16 Pro",     10999.99,  3));
            produtoRepository.save(new Produto("iPhone 17",         11499.99,  5));
            produtoRepository.save(new Produto("iPhone 17 Pro Max", 13999.99,  2));
            System.out.println("✔ Produtos carregados no banco!");
        }
    }
}
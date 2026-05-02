package com.sistemacompra.controller;

import com.sistemacompra.model.Produto;
import com.sistemacompra.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final EstoqueService estoqueService;

    public ProdutoController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public List<Produto> listar() {
        return estoqueService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Produto> adicionar(@RequestBody Produto produto) {
        return ResponseEntity.ok(estoqueService.salvar(produto));
    }
}
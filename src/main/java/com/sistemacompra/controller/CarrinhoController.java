package com.sistemacompra.controller;

import com.sistemacompra.model.ItemCarrinho;
import com.sistemacompra.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> listar(@PathVariable Long usuarioId) {
        List<ItemCarrinho> itens = carrinhoService.listarItens(usuarioId);
        double total = carrinhoService.calcularTotal(usuarioId);
        return ResponseEntity.ok(Map.of("itens", itens, "total", total));
    }

    @PostMapping("/{usuarioId}/adicionar")
    public ResponseEntity<?> adicionar(@PathVariable Long usuarioId,
                                       @RequestBody Map<String, Object> body) {
        try {
            String nomeProduto = (String) body.get("nomeProduto");
            int quantidade = (int) body.get("quantidade");
            ItemCarrinho item = carrinhoService.adicionarItem(usuarioId, nomeProduto, quantidade);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}/remover/{itemId}")
    public ResponseEntity<?> remover(@PathVariable Long usuarioId,
                                     @PathVariable Long itemId) {
        try {
            carrinhoService.removerItem(usuarioId, itemId);
            return ResponseEntity.ok("Item removido.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{usuarioId}/finalizar")
    public ResponseEntity<?> finalizar(@PathVariable Long usuarioId) {
        try {
            carrinhoService.finalizarCompra(usuarioId);
            return ResponseEntity.ok("Compra finalizada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
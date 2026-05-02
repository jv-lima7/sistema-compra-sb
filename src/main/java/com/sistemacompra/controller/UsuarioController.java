package com.sistemacompra.controller;

import com.sistemacompra.model.Usuario;
import com.sistemacompra.service.AutenticacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final AutenticacaoService autenticacaoService;

    public UsuarioController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, String> body) {
        try {
            Usuario usuario = autenticacaoService.cadastrar(
                    body.get("email"),
                    body.get("cpf"),
                    body.get("senha")
            );
            return ResponseEntity.ok("Usuário cadastrado com sucesso! ID: " + usuario.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        Optional<Usuario> usuario = autenticacaoService.autenticar(
                body.get("email"),
                body.get("senha")
        );
        if (usuario.isPresent()) {
            return ResponseEntity.ok("Login realizado! Bem-vindo(a), " + usuario.get().getEmail());
        }
        return ResponseEntity.status(401).body("Email ou senha incorretos.");
    }

    @PutMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> body) {
        boolean sucesso = autenticacaoService.redefinirSenha(
                body.get("email"),
                body.get("cpf"),
                body.get("novaSenha")
        );
        if (sucesso) return ResponseEntity.ok("Senha redefinida com sucesso!");
        return ResponseEntity.badRequest().body("Email ou CPF incorretos.");
    }
}
package com.sistemacompra.service;

import com.sistemacompra.model.Usuario;
import com.sistemacompra.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrar(String email, String cpf, String senha) {
        if (!email.contains("@"))
            throw new IllegalArgumentException("Email inválido.");
        if (!cpf.matches("\\d{11}"))
            throw new IllegalArgumentException("CPF deve ter 11 dígitos.");
        if (senha.length() < 6)
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
        if (usuarioRepository.existsByEmail(email))
            throw new IllegalArgumentException("Email já cadastrado.");

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setCpf(cpf);
        usuario.setSenha(senha);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> autenticar(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> u.getSenha().equals(senha));
    }

    public boolean redefinirSenha(String email, String cpf, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getCpf().equals(cpf))
            return false;

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);
        return true;
    }
}
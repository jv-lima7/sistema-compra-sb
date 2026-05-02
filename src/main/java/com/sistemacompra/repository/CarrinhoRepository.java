package com.sistemacompra.repository;

import com.sistemacompra.model.ItemCarrinho;
import com.sistemacompra.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {
    List<ItemCarrinho> findByUsuario(Usuario usuario);
    Optional<ItemCarrinho> findByUsuarioAndProduto_NomeIgnoreCase(Usuario usuario, String nome);
    void deleteByUsuario(Usuario usuario);
}
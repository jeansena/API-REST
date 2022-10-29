package com.Api.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Api.Rest.model.produtoModel;

@Repository
public interface ProdutoRepository extends JpaRepository<produtoModel, Long>{

}

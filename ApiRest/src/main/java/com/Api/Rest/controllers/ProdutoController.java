package com.Api.Rest.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.RestController;

import com.Api.Rest.model.produtoModel;
import com.Api.Rest.repositories.ProdutoRepository;

@RestController
public class ProdutoController {

	@Autowired
	ProdutoRepository produtoRepository;
	
	//metodo lista
	@GetMapping("/produtos")
    public ResponseEntity<List<produtoModel>> getAllProdutos(){
	    //lista os produtos 
		List<produtoModel> produtoList = produtoRepository.findAll();
	  if(produtoList.isEmpty()){
		  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	  }
	  else {
		  for(produtoModel produto : produtoList) {
			  long id = produto.getIdProduto();
			  produto.add(linkTo(methodOn(ProdutoController.class).getOneProduto(id)).withSelfRel());
		  }
		  return new ResponseEntity<List<produtoModel>>(produtoList, HttpStatus.OK);
	  }
	}
	
	//lista produtos pelo-id 
	@GetMapping("/produtos/{id}")
    public ResponseEntity<produtoModel> getOneProduto(@PathVariable(value = "id") long id){
    	Optional<produtoModel> produtoO = produtoRepository.findById(id);    	
     
	
	  if(produtoO.isEmpty()){
		  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	  }
	  else
	  
	   { 
		  //usando o Hetaoas para links de imagens
	 	  produtoO.get().add(linkTo(methodOn(ProdutoController.class).getAllProdutos()).withRel("Lista de produtos"));
		  return new ResponseEntity<produtoModel>(produtoO.get(), HttpStatus.OK);
	  }
	}
	
    //metodo POST para Salvar no banco
    @PostMapping("/produtos")
     public ResponseEntity<produtoModel> saveProduto(@RequestBody @Validated produtoModel produto){
     
    	 return new ResponseEntity<produtoModel>(produtoRepository.save(produto),HttpStatus.CREATED);
    	 
     } 
     
     //metodo DELETE passando o id
    @DeleteMapping("/produtos/{id}")
     public ResponseEntity<?> deleteProduto(@PathVariable(value="id") long id){
    	Optional<produtoModel> produtoO = produtoRepository.findById(id);
    	
    	if(produtoO.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}else {
    		produtoRepository.delete(produtoO.get());
    		return new ResponseEntity<>(HttpStatus.OK);
    	}
    }    
    
    //metodo PUT atualizar o banco
    @PutMapping("/produtos/{id}")
    public ResponseEntity<produtoModel> updateProduto (@PathVariable(value="id") long id,
    		@RequestBody @Validated produtoModel produto){
    	
        Optional<produtoModel> produtoO = produtoRepository.findById(id);
    	
    	if(!produtoO.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}else {
    		produto.setIdProduto(produtoO.get().getIdProduto());
    		return new ResponseEntity<produtoModel>(produtoRepository.save(produto), HttpStatus.OK);
    	}    	
      }
    
	}










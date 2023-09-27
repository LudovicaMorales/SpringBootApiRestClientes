package com.cursoudemy.springboot.backend.apirest.models.service;

import java.util.List;

import com.cursoudemy.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {
	
	// Método para listar todos los clientes
	public List<Cliente> findAll();
	
	// Método para listar un cliente por su id
	public Cliente findById(Long id);
	
	// Método para guardar un nuevo cliente
	public Cliente save(Cliente cliente);
	
	// Método para eliminar un cliente
	public void delete(Long id);
	
}

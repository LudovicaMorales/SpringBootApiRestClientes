package com.cursoudemy.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cursoudemy.springboot.backend.apirest.models.entity.Cliente;
import com.cursoudemy.springboot.backend.apirest.models.service.IClienteService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> listarClientes(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> buscarCliente(@PathVariable Long id) {
		
		Cliente cliente = null;
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = clienteService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Ha sucedido un error al realizar la consulta.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mensaje", "El id ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
		
		Cliente nuevoCliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			nuevoCliente = clienteService.save(cliente);
		}catch(DataAccessException e) {
			response.put("mensaje", "Ha sucedido un error al realizar la consulta.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido creado con éxito.");
		response.put("cliente", nuevoCliente);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> actualizarCliente(@RequestBody Cliente cliente, @PathVariable Long id) {
		
		Cliente clienteEncontrado = clienteService.findById(id);
		
		Map<String, Object> response = new HashMap<>();
		
		if(clienteEncontrado == null) {
			response.put("mensaje", "El id ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Cliente clienteActualizado = null;
		
		try {
			clienteEncontrado.setNombre(cliente.getNombre());
			clienteEncontrado.setApellidos(cliente.getApellidos());
			clienteEncontrado.setEmail(cliente.getEmail());
			clienteEncontrado.setCreateAt(cliente.getCreateAt());
			
			clienteActualizado = clienteService.save(clienteEncontrado);
		} catch(DataAccessException e) {
			response.put("mensaje", "Ha sucedido un error al realizar la consulta.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con éxito.");
		response.put("cliente", clienteActualizado);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
		
		Cliente clienteEncontrado = clienteService.findById(id);
		
		Map<String, Object> response = new HashMap<>();
		
		if(clienteEncontrado == null) {
			response.put("mensaje", "El id ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Ha sucedido un error al realizar la consulta.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido eliminado con éxito.");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}

}

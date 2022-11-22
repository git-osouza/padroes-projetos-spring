package one.digitalinovation.padroesprojetosspring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinovation.padroesprojetosspring.model.Cliente;
import one.digitalinovation.padroesprojetosspring.model.ClienteRepositoy;
import one.digitalinovation.padroesprojetosspring.model.Endereco;
import one.digitalinovation.padroesprojetosspring.model.EnderecoRepository;
import one.digitalinovation.padroesprojetosspring.service.ClienteService;
import one.digitalinovation.padroesprojetosspring.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author egsouza
 */

@Service
public class ClienteServiceImpl implements ClienteService{

		// Singleton: Injetar os componentes do Spring com @Autowired.
		@Autowired
		private ClienteRepositoy clienteRepositoy;
		
		@Autowired
		private EnderecoRepository enderecoRepository;
		
		@Autowired
		private ViaCepService viaCepService;
		

		@Override
		public Iterable<Cliente> buscarTodos() {
			return clienteRepositoy.findAll();
		}

		@Override
		public Cliente buscarPorId(Long id) {
			Optional<Cliente> cliente = clienteRepositoy.findById(id);
			return cliente.get();
		}

		@Override
		public void inserir(Cliente cliente) {
			salvarClienteComCep(cliente);
		}

		@Override
		public void atualizar(Long id, Cliente cliente) {
			Optional<Cliente> clienteBd = clienteRepositoy.findById(id);
			if (clienteBd.isPresent()) {
				salvarClienteComCep(cliente);
			}
		}

		@Override
		public void deletar(Long id) {
			clienteRepositoy.deleteById(id);
		}

		private void salvarClienteComCep(Cliente cliente) {
			String cep = cliente.getEndereco().getCep();
			Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
				Endereco novoEndereco = viaCepService.consultarCep(cep);
				enderecoRepository.save(novoEndereco);
				return novoEndereco;
			});
			cliente.setEndereco(endereco);
			clienteRepositoy.save(cliente);
		}

}

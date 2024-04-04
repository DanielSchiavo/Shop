package br.com.danielschiavo.shop.services.cliente.admin;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;

@ExtendWith(MockitoExtension.class)
class ClienteAdminServiceTest {

	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@InjectMocks
	private ClienteAdminService clienteService;

}

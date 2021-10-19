package modelo.servicos;

import java.util.List;

import modelo.dao.DaoFactory;
import modelo.dao.VendedorDao;
import modelo.entidades.Vendedor;

public class VendedorServico {
	
	private VendedorDao dao = DaoFactory.createVendedorDao();
	
	public List<Vendedor> busqueTodos(){
		return dao.findAll();
	}
	
	public void salvarOuAtualizar(Vendedor obj) {
		if(obj.getId() == null) {
			dao.inserir(obj);
		}else {
			dao.atualizar(obj);
		}
	}
	
	public void remover(Vendedor obj) {
		dao.deleteById(obj.getId());
	}
}

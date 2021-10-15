package modelo.servicos;

import java.util.List;

import modelo.dao.DaoFactory;
import modelo.dao.DepartamentoDao;
import modelo.entidades.Departamento;

public class DepartamentoServico {
	
	private DepartamentoDao dao = DaoFactory.createDepartmentDao();
	
	public List<Departamento> busqueTodos(){
		return dao.findAll();
	}
	
	public void salvarOuAtualizar(Departamento obj) {
		if(obj.getId() == null) {
			dao.inserir(obj);
		}else {
			dao.atualizar(obj);
		}
	}
	
	public void remover(Departamento obj) {
		dao.deleteById(obj.getId());
	}
}

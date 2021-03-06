package modelo.dao;

import java.util.List;

import modelo.entidades.Departamento;
import modelo.entidades.Vendedor;

public interface VendedorDao {

	void inserir(Vendedor obj);
	void atualizar(Vendedor obj);
	void deleteById(Integer id);
	Vendedor findById(Integer id);
	List<Vendedor> findAll();
	List<Vendedor> findByDepartamento(Departamento departamento);

}

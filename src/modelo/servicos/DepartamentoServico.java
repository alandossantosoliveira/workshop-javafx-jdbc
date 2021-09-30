package modelo.servicos;

import java.util.ArrayList;
import java.util.List;

import modelo.entidades.Departamento;

public class DepartamentoServico {
	
	public List<Departamento> busqueTodos(){
		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Livros"));
		list.add(new Departamento(2, "Computadores"));
		list.add(new Departamento(3, "Eletronicos"));
		
		return list;
	}

}

package modelo.excessoes;

import java.util.HashMap;
import java.util.Map;

public class ValidacaoExcessao extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> erros = new HashMap<>();
	
	public ValidacaoExcessao(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros(){
		return erros;
	}
	
	public void addErro(String fieldNome, String msgErro) {
		erros.put(fieldNome, msgErro);
	}
}

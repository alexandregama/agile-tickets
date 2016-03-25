package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	public static BigDecimal calcula(Sessao sessao, Integer quantidadeDeIngressos) {
		BigDecimal preco;
		
		if(sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.CINEMA) || sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.SHOW)) {
			preco = new CalculadoraDePrecoPorShowECinema().calcula(sessao, quantidadeDeIngressos);
		} else if(sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.BALLET)) {
			preco = new CalculadoraDePrecosPorBallet().calcula(sessao, quantidadeDeIngressos);
		} else if(sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.ORQUESTRA)) {
			preco = new CalculadoraDePrecosPorOrquestra().calcula(sessao, quantidadeDeIngressos);
		}  else {
			preco = new CalculadoraDePrecosPorTeatro().calcula(sessao, quantidadeDeIngressos);
		} 

		return preco.multiply(BigDecimal.valueOf(quantidadeDeIngressos));
	}

}
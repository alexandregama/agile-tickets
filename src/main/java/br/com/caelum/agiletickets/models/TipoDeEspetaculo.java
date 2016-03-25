package br.com.caelum.agiletickets.models;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.domain.precos.CalculadoraDePrecoPorEspetaculo;
import br.com.caelum.agiletickets.domain.precos.CalculadoraDePrecoPorShowOuCinema;
import br.com.caelum.agiletickets.domain.precos.CalculadoraDePrecosPorBallet;
import br.com.caelum.agiletickets.domain.precos.CalculadoraDePrecosPorOrquestra;
import br.com.caelum.agiletickets.domain.precos.CalculadoraDePrecosPorTeatro;

public enum TipoDeEspetaculo {
	
	CINEMA(new CalculadoraDePrecoPorShowOuCinema()), 
	SHOW(new CalculadoraDePrecoPorShowOuCinema()), 
	TEATRO(new CalculadoraDePrecosPorTeatro()), 
	BALLET(new CalculadoraDePrecosPorBallet()), 
	ORQUESTRA(new CalculadoraDePrecosPorOrquestra());

	private CalculadoraDePrecoPorEspetaculo calculadora;
	
	TipoDeEspetaculo(CalculadoraDePrecoPorEspetaculo calculadora) {
		this.calculadora = calculadora;
	}
	
	public BigDecimal calcula(Sessao sessao, Integer quantidadeDeIngressos) {
		return calculadora.calcula(sessao, quantidadeDeIngressos);
	}
	
}

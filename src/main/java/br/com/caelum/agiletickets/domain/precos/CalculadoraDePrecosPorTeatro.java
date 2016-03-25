package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;

public class CalculadoraDePrecosPorTeatro implements CalculadoraDePrecoPorEspetaculo {

	@Override
	public BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco = BigDecimal.ZERO;
		preco = sessao.getPreco();
		
		return preco;
	}

}

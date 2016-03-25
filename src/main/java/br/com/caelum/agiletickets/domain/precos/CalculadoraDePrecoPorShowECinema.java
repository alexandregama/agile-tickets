package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;

public class CalculadoraDePrecoPorShowECinema implements CalculadoraDePrecoPorEspetaculo {

	@Override
	public BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco = BigDecimal.ZERO;
		int ingressosSobrando = sessao.getTotalIngressos() - sessao.getIngressosReservados();
		double totalDeIngressos = sessao.getTotalIngressos().doubleValue();

		if (ingressosSobrando / totalDeIngressos <= 0.05) {
			preco = sessao.acressentaEmPorcentagem(0.10);
		} else {
			preco = sessao.getPreco();
		}
		return preco;
	}

}

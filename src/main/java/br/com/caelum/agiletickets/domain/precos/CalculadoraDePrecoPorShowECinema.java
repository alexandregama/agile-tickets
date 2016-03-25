package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;

public class CalculadoraDePrecoPorShowECinema implements CalculadoraDePrecoPorEspetaculo {

	@Override
	public BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal novoPreco = BigDecimal.ZERO;

		if (existemMenosQueCincoPorCentoDosIngressosNa(sessao)) {
			novoPreco = sessao.acressentaNoPrecoAPorcentagemDe(0.10);
		} else {
			novoPreco = sessao.getPreco();
		}
		return novoPreco;
	}

	private boolean existemMenosQueCincoPorCentoDosIngressosNa(Sessao sessao) {
		int ingressosSobrando = sessao.getTotalIngressos() - sessao.getIngressosReservados();
		double totalDeIngressos = sessao.getTotalIngressos().doubleValue();
		boolean menosQueCincoPorCentoDosIngressos = ingressosSobrando / totalDeIngressos <= 0.05;
		return menosQueCincoPorCentoDosIngressos;
	}

}

package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;

public class CalculadoraDePrecosPorBallet implements CalculadoraDePrecoPorEspetaculo {

	@Override
	public BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco = BigDecimal.ZERO;
		
		if(existemMenosQueCincoPorCentoDeIngressosNa(sessao)) { 
			preco = sessao.acressentaNoPrecoAPorcentagemDe(0.20);
		} else {
			preco = sessao.getPreco();
		}
		
		if(sessao.getDuracaoEmMinutos() > 60){
			preco = sessao.acressentaNoPrecoAPorcentagemDe(0.10);
		}
		return preco;
	}

	private boolean existemMenosQueCincoPorCentoDeIngressosNa(Sessao sessao) {
		return (sessao.getTotalIngressos() - sessao.getIngressosReservados()) / sessao.getTotalIngressos().doubleValue() <= 0.50;
	}

}

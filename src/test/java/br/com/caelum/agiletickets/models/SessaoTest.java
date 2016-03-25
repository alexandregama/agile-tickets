package br.com.caelum.agiletickets.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class SessaoTest {

	@Test
	public void deveVender1ingressoSeHa2vagas() throws Exception {
		Sessao sessao = new Sessao();
        sessao.setTotalIngressos(2);

        assertTrue(sessao.podeReservar(1));
	}

	@Test
	public void naoDeveVender3ingressoSeHa2vagas() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(2);

		assertFalse(sessao.podeReservar(3));
	}

	@Test
	public void deveriaVender1IngressoSeHaSomente1Vaga() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(1);
		
		assertTrue(sessao.podeReservar(1));
	}
	
	@Test
	public void reservarIngressosDeveDiminuirONumeroDeIngressosDisponiveis() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(5);

		sessao.reserva(3);
		assertEquals(2, sessao.getIngressosDisponiveis().intValue());
	}

	@Test
	public void deveriaAcrescentarUmaPorcentagemAoValorOriginalDaSessao() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setPreco(BigDecimal.TEN);
		
		BigDecimal novoPreco = sessao.acressentaNoPrecoAPorcentagemDe(0.10);
		
		assertThat(novoPreco).isEqualTo(BigDecimal.valueOf((11.0)));
	}
	
}

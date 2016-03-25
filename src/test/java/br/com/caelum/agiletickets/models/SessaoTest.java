package br.com.caelum.agiletickets.models;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.caelum.agiletickets.domain.precos.SessaoTestDataBuilder;

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
		
		BigDecimal novoPreco = sessao.acressentaNoPrecoFinalAPorcentagemDe(0.10);
		
		assertThat(novoPreco).isEqualTo(BigDecimal.valueOf((11.0)));
	}
	
	@Test
	public void deveConsiderarQuantidadeAoCalcularPrecoTotalParaSessaoEmTeatro(){
		Sessao sessao =	SessaoTestDataBuilder
			.umaSessao()
			.deUmEspetaculoDoTipo(TipoDeEspetaculo.TEATRO)
			.custando(10.0)
			.build();
		
		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(9);
		
		assertThat(precoTotal).isEqualTo(valueOf(90.0));
	}
	
	@Test
	public void deveAplicar10PorCentoAMaisNosUltimosIngressosQuandoForCinema(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.CINEMA)
				.comTotalIngressos(100)
				.comIngressoReservados(95)
				.custando(20.0)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(22.0).compareTo(precoTotal));
	}

	@Test
	public void naoDeveAplicarAcrescimoNosPrimeirosIngressosQuandoForCinema(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.CINEMA)
				.comTotalIngressos(100)
				.comIngressoReservados(10)
				.custando(20.0)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(20.0).compareTo(precoTotal));
	}
	
	@Test
	public void deveAplicar10PorCentoAMaisNosUltimosIngressosQuandoForShow(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.SHOW)
				.comTotalIngressos(200)
				.comIngressoReservados(195)
				.custando(100.0)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(110.0).compareTo(precoTotal));
	}

	@Test
	public void naoDeveAplicarAcrescimoNosPrimeirosIngressosQuandoForShow(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.SHOW)
				.comTotalIngressos(200)
				.comIngressoReservados(15)
				.custando(100.0)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(100.0).compareTo(precoTotal));
	}
	
	@Test
	public void deveAplicar20PorCentoAMaisNosUltimosIngressosQuandoForBallet(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.BALLET)
				.comTotalIngressos(50)
				.comIngressoReservados(25)
				.custando(500.0)
				.comDuracaoEmMinutos(50)
				.build();
		
		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(600.0).compareTo(precoTotal));
	}

	@Test
	public void naoDeveAplicarAcrescimoNosPrimeirosIngressosQuandoForBallet(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.BALLET)
				.comTotalIngressos(50)
				.comIngressoReservados(5)
				.custando(500.0)
				.comDuracaoEmMinutos(50)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(500.0).compareTo(precoTotal));
	}

	@Test
	public void deveAplicar10AMaisSeDurarMaisDeUmaHoraQuandoForBallet(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.BALLET)
				.comTotalIngressos(50)
				.comIngressoReservados(5)
				.custando(500.0)
				.comDuracaoEmMinutos(100)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(550.0).compareTo(precoTotal));
	}
	
	@Test
	public void deveAplicar20PorCentoAMaisNosUltimosIngressosQuandoForOrquestra(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.ORQUESTRA)
				.comTotalIngressos(70)
				.comIngressoReservados(40)
				.custando(1000.0)
				.comDuracaoEmMinutos(60)
				.build();
		
		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(1200.0).compareTo(precoTotal));
	}

	@Test
	public void naoDeveAplicarAcrescimoNosPrimeirosIngressosQuandoForOrquestra(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.ORQUESTRA)
				.comTotalIngressos(70)
				.comIngressoReservados(10)
				.custando(1000.0)
				.comDuracaoEmMinutos(60)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(1000.0).compareTo(precoTotal));
	}

	@Test
	public void deveAplicar10AMaisSeDurarMaisDeUmaHoraQuandoForOrquestra(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.ORQUESTRA)
				.comTotalIngressos(70)
				.comIngressoReservados(65)
				.custando(1000.0)
				.comDuracaoEmMinutos(120)
				.build();

		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertThat(precoTotal).isEqualByComparingTo(BigDecimal.valueOf(1300.0));
	}
	
	@Test
	public void naoDeveAplicarAcrescimoQuandoForTeatro(){
		Sessao sessao =	SessaoTestDataBuilder
				.umaSessao()
				.deUmEspetaculoDoTipo(TipoDeEspetaculo.TEATRO)
				.custando(10.0)
				.build();
		
		BigDecimal precoTotal = sessao.calculaPrecoFinalPara(1);
		
		assertEquals(0, BigDecimal.valueOf(10.0).compareTo(precoTotal));
	}

	
}

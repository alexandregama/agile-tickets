package br.com.caelum.agiletickets.controllers;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.agiletickets.domain.Agenda;
import br.com.caelum.agiletickets.domain.DiretorioDeEstabelecimentos;
import br.com.caelum.agiletickets.models.Espetaculo;
import br.com.caelum.agiletickets.models.Periodicidade;
import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.ValidationException;
import br.com.caelum.vraptor.validator.Validator;

@RunWith(MockitoJUnitRunner.class)
public class EspetaculosControllerTest {

	@Mock
	private Agenda agenda;
	@Mock
	private DiretorioDeEstabelecimentos estabelecimentos;
	@Spy 
	private Validator validator = new MockValidator();
	@Spy
	private Result result = new MockResult();
	
	private EspetaculosController controller;

	@Before
	public void setup() {
		controller = new EspetaculosController(result, validator, agenda, estabelecimentos);
	}

	@Test(expected=ValidationException.class)
	public void naoDeveCadastrarEspetaculosSemNome() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setDescricao("uma descricao");

		controller.adiciona(espetaculo);

		verifyZeroInteractions(agenda);
	}

	@Test(expected=ValidationException.class)
	public void naoDeveCadastrarEspetaculosSemDescricao() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setNome("um nome");

		controller.adiciona(espetaculo);

		verifyZeroInteractions(agenda);
	}

	@Test
	public void deveCadastrarEspetaculosComNomeEDescricao() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setNome("um nome");
		espetaculo.setDescricao("uma descricao");

		controller.adiciona(espetaculo);

		verify(agenda).cadastra(espetaculo);
	}
	
	@Test
	public void deveRetornarNotFoundSeASessaoNaoExiste() throws Exception {
		when(agenda.sessao(1234l)).thenReturn(null);

		controller.sessao(1234l);

		verify(result).notFound();
	}

	@Test(expected=ValidationException.class)
	public void naoDeveReservarZeroIngressos() throws Exception {
		when(agenda.sessao(1234l)).thenReturn(new Sessao());

		controller.reserva(1234l, 0);

		verifyZeroInteractions(result);
	}

	@Test(expected=ValidationException.class)
	public void naoDeveReservarMaisIngressosQueASessaoPermite() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(3);

		when(agenda.sessao(1234l)).thenReturn(sessao);

		controller.reserva(1234l, 5);

		verifyZeroInteractions(result);
	}
	
	@Test
	public void deveriaIncluirASessaoNoResultQuandoUsuarioBuscarUmaSessaoEElaExistir() throws Exception {
		Sessao sessao = new Sessao();
		
		when(agenda.sessao(1L)).thenReturn(sessao);
		
		controller.sessao(1L);
		
		verify(result).include("sessao", sessao);
	}

	@Test
	public void deveReservarSeASessaoTemIngressosSuficientes() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setTipo(TipoDeEspetaculo.TEATRO);

		Sessao sessao = new Sessao();
		sessao.setPreco(new BigDecimal("10.00"));
		sessao.setTotalIngressos(5);
		sessao.setEspetaculo(espetaculo);

		when(agenda.sessao(1234l)).thenReturn(sessao);

		controller.reserva(1234l, 3);

		assertThat(sessao.getIngressosDisponiveis(), is(2));
	}

	@Test(expected = ValidationException.class)
	public void deveriaLancarExcecaoDeValidacaoQuandoUsuarioCadastrarUmaSessaoDeEspetaculoQueNaoExiste() throws Exception {
		controller.cadastraSessoes(12L, LocalDate.now(), LocalDate.now(), LocalTime.now(), Periodicidade.DIARIA);
		
		when(agenda.espetaculo(12L)).thenReturn(null);
	}
	
	@Test
	public void deveriaCriarUmaSessaoQuandoUsuarioCadastrarUmaSessaoValida() throws Exception {
		LocalDate inicio = LocalDate.now();
		LocalDate fim = LocalDate.now();
		LocalTime horario = LocalTime.now();
		
		Espetaculo espetaculo = mock(Espetaculo.class);
		when(agenda.espetaculo(12L)).thenReturn(espetaculo);
		
		controller.cadastraSessoes(12L, inicio, inicio, horario, Periodicidade.DIARIA);
		
		verify(espetaculo).criaSessoes(inicio, fim, horario, Periodicidade.DIARIA);
	}
	
	@Test
	public void deveriaAgendarUmaNovaSessaoQuandoOUsuarioCadastrarUmaNovaSessaoValida() throws Exception {
		LocalDate inicio = LocalDate.now();
		LocalDate fim = LocalDate.now();
		LocalTime horario = LocalTime.now();
		
		Espetaculo espetaculo = mock(Espetaculo.class);
		when(agenda.espetaculo(12L)).thenReturn(espetaculo);
		
		List<Sessao> sessoes = asList(new Sessao());
		when(espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.SEMANAL)).thenReturn(sessoes);
		
		controller.cadastraSessoes(12L, inicio, fim, horario, Periodicidade.SEMANAL);
		
		verify(agenda).agende(sessoes);
	}
	
	@Test
	public void deveriaAdicionaUmaMensagemDeSucessoQuandoUsuarioCadastrarUmaNovaSessaoValida() throws Exception {
		LocalDate inicio = LocalDate.now();
		LocalDate fim = LocalDate.now();
		LocalTime horario = LocalTime.now();
		
		Espetaculo espetaculo = mock(Espetaculo.class);
		when(agenda.espetaculo(12L)).thenReturn(espetaculo);
		
		List<Sessao> sessoes = asList(new Sessao());
		when(espetaculo.criaSessoes(inicio, fim, horario, Periodicidade.SEMANAL)).thenReturn(sessoes);
		
		controller.cadastraSessoes(12L, inicio, fim, horario, Periodicidade.SEMANAL);
		
		verify(result).include("message", sessoes.size() + " sessões criadas com sucesso");
	}
	
}

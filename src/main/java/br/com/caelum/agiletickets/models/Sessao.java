package br.com.caelum.agiletickets.models;

import java.math.BigDecimal;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

@Entity
public class Sessao {

	private static final Locale LOCALE_BRASIL = new Locale("pt", "BR");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Espetaculo espetaculo;

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime inicio;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime dataCriacao = new DateTime();

	private Integer duracaoEmMinutos;

	private Integer totalIngressos = 0;

	private Integer ingressosReservados = 0;

	private BigDecimal preco = BigDecimal.ZERO;

	@Transient
	private BigDecimal precoFinal = BigDecimal.ZERO;

	public Long getId() {
		return id;
	}

	public void setEspetaculo(Espetaculo espetaculo) {
		this.espetaculo = espetaculo;
	}

	public DateTime getInicio() {
		return inicio;
	}

	public void setInicio(DateTime inicio) {
		this.inicio = inicio;
	}

	public Espetaculo getEspetaculo() {
		return espetaculo;
	}

	public Integer getDuracaoEmMinutos() {
		return duracaoEmMinutos;
	}

	public void setDuracaoEmMinutos(Integer duracaoEmMinutos) {
		this.duracaoEmMinutos = duracaoEmMinutos;
	}

	public String getDia() {
		return inicio.toString(DateTimeFormat.shortDate().withLocale(LOCALE_BRASIL));
	}

	public String getHora() {
		return inicio.toString(DateTimeFormat.shortTime().withLocale(LOCALE_BRASIL));
	}

	public Integer getTotalIngressos() {
		return totalIngressos;
	}

	public void setTotalIngressos(Integer totalIngressos) {
		this.totalIngressos = totalIngressos;
	}

	public Integer getIngressosReservados() {
		return ingressosReservados;
	}

	public void setIngressosReservados(Integer ingressosReservados) {
		this.ingressosReservados = ingressosReservados;
	}

	public Integer getIngressosDisponiveis() {
		return totalIngressos - ingressosReservados;
	}
	
	public void reserva(Integer numeroDeIngressos) {
		this.ingressosReservados += numeroDeIngressos;
	}

	public boolean podeReservar(Integer numeroDeIngressosDesejados) {
		int sobraram = getIngressosDisponiveis() - numeroDeIngressosDesejados;
        boolean temEspaco = sobraram >= 0;

        return temEspaco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public DateTime getDataCriacao() {
		return dataCriacao;
	}

	public BigDecimal acressentaNoPrecoFinalAPorcentagemDe(Double porcentagem) {
		if (precoFinal.equals(BigDecimal.ZERO)) {
			precoFinal = precoFinal.add(preco);
		}
		precoFinal = precoFinal.add(preco.multiply(BigDecimal.valueOf(porcentagem)));
		
		return precoFinal;
	}

	public BigDecimal calculaPrecoFinalPara(Integer quantidade) {
		BigDecimal precoParaOEspetaculo = getEspetaculo().getTipo().calcula(this, quantidade);
		
		return precoParaOEspetaculo.multiply(BigDecimal.valueOf(quantidade));
	}

	public BigDecimal getPrecoFinal() {
		return precoFinal;
	}
}

package br.com.caelum.agiletickets.persistencia;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.agiletickets.domain.DiretorioDeEstabelecimentos;
import br.com.caelum.agiletickets.models.Estabelecimento;

public class JpaEstabelecimentoDao implements DiretorioDeEstabelecimentos {

	private EntityManager manager;
	
	/** @deprecated CDI eyes only*/
	JpaEstabelecimentoDao() {
	}
	
	@Inject
	public JpaEstabelecimentoDao(EntityManager manager) {
		this.manager = manager;
	}

	@Override
	public List<Estabelecimento> todos() {
		return manager.createQuery("select e from Estabelecimento e", Estabelecimento.class).getResultList();
	}

	@Override
	public void adiciona(Estabelecimento estabelecimento) {
		manager.persist(estabelecimento);
	}

}

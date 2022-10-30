package it.prova.gestioneordiniarticolicategorie.dao.articolo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public class ArticoloDAOImpl implements ArticoloDAO{
	
	private EntityManager entityManager;

	@Override
	public List<Articolo> list() throws Exception {
		return entityManager.createQuery("from Articolo", Articolo.class).getResultList();
	}

	@Override
	public Articolo get(Long id) throws Exception {
		return entityManager.find(Articolo.class, id);
	}

	@Override
	public void update(Articolo input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		input = entityManager.merge(input);
		
	}

	@Override
	public void insert(Articolo input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(input);
	}

	@Override
	public void delete(Articolo input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.remove(entityManager.merge(input));
		
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Articolo findByIdFetchingOrdine(Long id) throws Exception {
		TypedQuery<Articolo> query = entityManager
				.createQuery("select a FROM Articolo a left join fetch a.ordine o where a.id = :idArticolo", Articolo.class);
		query.setParameter("idArticolo", id);
		return query.getResultList().stream().findFirst().orElse(null);
	}
	
	@Override
	public Articolo findByIdFetchingCategoria(Long id) throws Exception {
		TypedQuery<Articolo> query = entityManager
				.createQuery("select a FROM Articolo a left join fetch a.categorie c where a.id = :idArticolo", Articolo.class);
		query.setParameter("idArticolo", id);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void rimuoviArticoloEDisassociaCategorie(Long id) throws Exception {
		entityManager.createNativeQuery("delete from articolo_categoria where articolo_id=?1").setParameter(1, id).executeUpdate();
		this.delete(this.get(id));
	}

	@Override
	public long sommaPrezziDegliArticoliDiUnaCategoria(Categoria categoria) throws Exception {
		TypedQuery<Long> query = entityManager
				.createQuery("select sum(a.prezzoSingolo) FROM Articolo a join a.categorie c where c=?1", Long.class);
		query.setParameter(1, categoria);
		return query.getSingleResult().longValue();
	}

	@Override
	public long sommaPrezziArticoliIndrizzatiAdUnDestinatario(String nomeDestinatario) throws Exception {
		TypedQuery<Long> query = entityManager
				.createQuery("select sum(a.prezzoSingolo) FROM Articolo a join a.categorie c join a.ordine o where o.nomeDestinatario=?1", Long.class);
		query.setParameter(1, nomeDestinatario);
		return query.getSingleResult().longValue();
	}

}

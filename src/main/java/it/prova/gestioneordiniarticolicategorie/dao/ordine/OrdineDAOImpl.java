package it.prova.gestioneordiniarticolicategorie.dao.ordine;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public class OrdineDAOImpl implements OrdineDAO{
	
	private EntityManager entityManager;

	@Override
	public List<Ordine> list() throws Exception {
		return entityManager.createQuery("from Ordine", Ordine.class).getResultList();
	}

	@Override
	public Ordine get(Long id) throws Exception {
		return entityManager.find(Ordine.class, id);
	}

	@Override
	public void update(Ordine input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		input = entityManager.merge(input);
	}

	@Override
	public void insert(Ordine input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(input);
	}

	@Override
	public void delete(Ordine input) throws Exception {
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
	public Ordine caricaSingoloElementoEagerArticolo(Long id) throws Exception {
		TypedQuery<Ordine> query = entityManager
				.createQuery("select o FROM Ordine o left join fetch o.articoli a where o.id = :idOrdine", Ordine.class);
		query.setParameter("idOrdine", id);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public List<Ordine> ordiniEffettuatiPerUnaDeterminataCategoria(Categoria categoria) throws Exception {
		TypedQuery<Ordine> query = entityManager.createQuery("select o from Ordine o join o.articoli a join a.categorie c where c.descrizione = ?1", Ordine.class);
		return query.setParameter(1, categoria.getDescrizione()).getResultList();
	}

	@Override
	public List<String> indirizziCheContengonoUnaStringaNelNumSerialeArticoli(String stringaDaConfrontare)
			throws Exception {
		stringaDaConfrontare="%"+stringaDaConfrontare+"%";
		TypedQuery<String> query = entityManager.createQuery("select distinct(o.indirizzoSpedizione) from Ordine o join o.articoli a where a.numeroSeriale like ?1", String.class);
		query.setParameter(1, stringaDaConfrontare);
		return query.getResultList();
	}

	@Override
	public Ordine ordineConSpedizionePiuRecente(Categoria categoria) throws Exception {
		TypedQuery<Ordine> query = entityManager
				.createQuery("select o FROM Ordine o join o.articoli a join a.categorie c where c =?1 group by (o.dataSpedizione)", Ordine.class);
		query.setParameter(1, categoria);
		return query.getResultList().get(0);
	}

}

package it.prova.gestioneordiniarticolicategorie.dao.categoria;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public class CategoriaDAOImpl implements CategoriaDAO {

	private EntityManager entityManager;

	@Override
	public List<Categoria> list() throws Exception {
		return entityManager.createQuery("from Categoria", Categoria.class).getResultList();
	}

	@Override
	public Categoria get(Long id) throws Exception {
		return entityManager.find(Categoria.class, id);
	}

	@Override
	public void update(Categoria input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		input = entityManager.merge(input);
	}

	@Override
	public void insert(Categoria input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(input);
	}

	@Override
	public void delete(Categoria input) throws Exception {
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
	public void rimuoviCategoriaEDisassociaArticoli(Long id) throws Exception {
		entityManager.createNativeQuery("delete from articolo_categoria where categoria_id=?1").setParameter(1, id).executeUpdate();
		this.delete(this.get(id));
	}

	@Override
	public Categoria caricaSingoloElementoEagerArticolo(Long id) throws Exception {
		TypedQuery<Categoria> query = entityManager
				.createQuery("select c FROM Categoria c left join fetch c.articoli a where c.id = :idCategoria", Categoria.class);
		query.setParameter("idCategoria", id);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public List<String> categorieDegliarticoliDiUnDeterminatoOrdine(Ordine ordine) throws Exception {
		TypedQuery<String> query = entityManager.createQuery("select distinct c.descrizione from Categoria c join c.articoli a join a.ordine o where o.id = ?1", String.class);
		return query.setParameter(1, ordine.getId()).getResultList();
	}

	@Override
	public List<String> codiciDiCategorieOrdiniEffettuatiDuranteUnMese(Date data) throws Exception {
		TypedQuery<String> query = entityManager.createQuery("select distinct c.codice from Categoria c join c.articoli a join a.ordine o where YEAR(o.dataSpedizione) = YEAR(:data) and MONTH(o.dataSpedizione) = MONTH(:data)", String.class);
		query.setParameter("data", new java.sql.Date(data.getTime()));
		return query.getResultList();
	}

}

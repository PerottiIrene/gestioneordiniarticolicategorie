package it.prova.gestioneordiniarticolicategorie.dao.categoria;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface CategoriaDAO extends IBaseDAO<Categoria>{
	
	public void rimuoviCategoriaEDisassociaArticoli(Long id) throws Exception;
	
	public Categoria caricaSingoloElementoEagerArticolo(Long id) throws Exception;

}

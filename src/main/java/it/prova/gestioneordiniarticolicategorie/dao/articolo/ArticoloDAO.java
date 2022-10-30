package it.prova.gestioneordiniarticolicategorie.dao.articolo;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;

public interface ArticoloDAO extends IBaseDAO<Articolo>{
	
	public Articolo findByIdFetchingOrdine(Long id) throws Exception;
	
	public Articolo findByIdFetchingCategoria(Long id) throws Exception;
	
	public void rimuoviArticoloEDisassociaCategorie (Long id) throws Exception;

}

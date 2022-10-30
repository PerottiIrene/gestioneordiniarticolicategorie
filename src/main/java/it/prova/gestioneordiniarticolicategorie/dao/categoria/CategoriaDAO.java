package it.prova.gestioneordiniarticolicategorie.dao.categoria;

import java.util.Date;
import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface CategoriaDAO extends IBaseDAO<Categoria>{
	
	public void rimuoviCategoriaEDisassociaArticoli(Long id) throws Exception;
	
	public Categoria caricaSingoloElementoEagerArticolo(Long id) throws Exception;
	
	public List<String> categorieDegliarticoliDiUnDeterminatoOrdine (Ordine ordine) throws Exception;
	
	public List<String> codiciDiCategorieOrdiniEffettuatiDuranteUnMese (Date data) throws Exception;

}

package it.prova.gestioneordiniarticolicategorie.dao.articolo;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface ArticoloDAO extends IBaseDAO<Articolo>{
	
	public Articolo findByIdFetchingOrdine(Long id) throws Exception;
	
	public Articolo findByIdFetchingCategoria(Long id) throws Exception;
	
	public void rimuoviArticoloEDisassociaCategorie (Long id) throws Exception;
	
	public long sommaPrezziDegliArticoliDiUnaCategoria (Categoria categoria) throws Exception;
	
	public long sommaPrezziArticoliIndrizzatiAdUnDestinatario (String nomeDestinatario) throws Exception;

}

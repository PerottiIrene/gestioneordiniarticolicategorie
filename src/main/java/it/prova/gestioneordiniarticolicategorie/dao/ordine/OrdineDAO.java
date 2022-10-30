package it.prova.gestioneordiniarticolicategorie.dao.ordine;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface OrdineDAO extends IBaseDAO<Ordine>{
	
	public Ordine caricaSingoloElementoEagerArticolo(Long id) throws Exception;
	
	public List<Ordine> ordiniEffettuatiPerUnaDeterminataCategoria(Categoria categoria) throws Exception;

}

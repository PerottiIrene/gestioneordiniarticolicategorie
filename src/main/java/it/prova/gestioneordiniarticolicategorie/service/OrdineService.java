package it.prova.gestioneordiniarticolicategorie.service;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.articolo.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.dao.ordine.OrdineDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface OrdineService {
	
	public List<Ordine> listAll() throws Exception;

	public Ordine caricaSingoloElemento(Long id) throws Exception;

	public void aggiorna(Ordine ordineInstance) throws Exception;

	public void inserisciNuovo(Ordine ordineInstance) throws Exception;

	public void rimuovi(Long idOrdine) throws Exception;

	public void aggiungiArticolo(Ordine ordineInstance, Articolo articoloInstance) throws Exception;
	
	public void creaECollegaOrdineEArticolo(Ordine ordineTransientInstance, Articolo articoloTransientInstance) throws Exception;
	
	public Ordine caricaSingoloElementoEagerArticolo(Long id) throws Exception;
	
	public List<Ordine> ordiniEffettuatiPerUnaDeterminataCategoria(Categoria categoria) throws Exception;
	
	public List<String> indirizziCheContengonoUnaStringaNelNumSerialeArticoli (String stringaDaConfrontare) throws Exception;
	
	public Ordine ordineConSpedizionePiuRecente (Categoria categoria) throws Exception;
	
	// per injection
    public void setOrdineDAO(OrdineDAO ordineDAO);

}

package it.prova.gestioneordiniarticolicategorie.service;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.articolo.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface ArticoloService {
	
	// per injection
    public void setArticoloDAO(ArticoloDAO articoloDAO);
    
    public List<Articolo> listAll() throws Exception;

	public Articolo caricaSingoloElemento(Long id) throws Exception;
	
	public Articolo caricaSingoloElementoEagerCategoria(Long id) throws Exception;

	public void aggiorna(Articolo articoloInstance) throws Exception;

	public void inserisciNuovo(Articolo articoloInstance) throws Exception;

	public void rimuovi(Long idArticolo) throws Exception;

	public void aggiungiCategoria(Articolo articoloInstance, Categoria categoriaInstance) throws Exception;
	
	public Articolo findByIdFetchingOrdine(Long id) throws Exception;
	
	public void aggiungiOrdine(Articolo articoloInstance, Ordine ordineInstance) throws Exception;
	
	public void rimuoviArticoloEDisassociaCategorie (Long id) throws Exception;
	
	public long sommaPrezziDegliArticoliDiUnaCategoria (Categoria categoria) throws Exception;
	
	public long sommaPrezziArticoliIndrizzatiAdUnDestinatario (String nomeDestinatario) throws Exception;
}

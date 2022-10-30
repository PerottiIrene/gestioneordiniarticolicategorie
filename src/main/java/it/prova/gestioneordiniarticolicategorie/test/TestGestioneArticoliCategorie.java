package it.prova.gestioneordiniarticolicategorie.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import it.prova.gestioneordiniarticolicategorie.dao.EntityManagerUtil;
import it.prova.gestioneordiniarticolicategorie.exception.OrdineConArticoliAssociatiException;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;
import it.prova.gestioneordiniarticolicategorie.service.ArticoloService;
import it.prova.gestioneordiniarticolicategorie.service.CategoriaService;
import it.prova.gestioneordiniarticolicategorie.service.MyServiceFactory;
import it.prova.gestioneordiniarticolicategorie.service.OrdineService;

public class TestGestioneArticoliCategorie {

	public static void main(String[] args) {

		ArticoloService articoloServiceInstance = MyServiceFactory.getArticoloServiceInstance();
		OrdineService ordineServiceInstance = MyServiceFactory.getOrdineServiceInstance();
		CategoriaService categoriaServiceInstance = MyServiceFactory.getCategoriaServiceInstance();

		try {

			testInserimentoNuovoOrdine(ordineServiceInstance);
			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");

			testAggiornamentoOrdineEsistente(ordineServiceInstance);

			testInserimentoNuovoArticolo(articoloServiceInstance, ordineServiceInstance);
			System.out
					.println("In tabella Articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi.");

			testAggiornamentoArticoloEsistente(articoloServiceInstance);

			testInserimentoNuovaCategoria(categoriaServiceInstance);
			System.out.println(
					"In tabella Categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi.");

			testAggiornamentoCategoriaEsistente(categoriaServiceInstance);

			testAggiungiArticoloACategoria(categoriaServiceInstance, articoloServiceInstance);

			testAggiungiCategoriaAdArticolo(categoriaServiceInstance, articoloServiceInstance, ordineServiceInstance);

			testRimuovArticoloEDisassociaCategorie(articoloServiceInstance, categoriaServiceInstance,
					ordineServiceInstance);
			
			testRimuoviCategoriaEDisassociaArticoli(categoriaServiceInstance, articoloServiceInstance, ordineServiceInstance);
			
			testRimozioneOrdine(ordineServiceInstance);

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}

	}

	private static void testInserimentoNuovoOrdine(OrdineService ordineServiceInstance) throws Exception {

		System.out.println(".......testInserimentoNuovoOrdine inizio.............");

		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoOrdine fallito ");

		System.out.println(".......testInserimentoNuovoOrdine fine: PASSED.............");
	}

	private static void testAggiornamentoOrdineEsistente(OrdineService ordineServiceInstance) throws Exception {

		System.out.println(".......testAggiornamentoOrdine inizio.............");

		Ordine ordineEsistenteSulDb = ordineServiceInstance.caricaSingoloElemento(1L);

		String nuovoIndirizzo = "via roma";
		ordineEsistenteSulDb.setIndirizzoSpedizione(nuovoIndirizzo);
		ordineServiceInstance.aggiorna(ordineEsistenteSulDb);

		Ordine ordineReloaded = ordineServiceInstance.caricaSingoloElemento(ordineEsistenteSulDb.getId());
		if (!ordineEsistenteSulDb.getIndirizzoSpedizione().equals(ordineReloaded.getIndirizzoSpedizione()))
			throw new RuntimeException("testAggiornamentoOrdine fallito");

		System.out.println("testAggiornamentoOrdine passed");
	}

	private static void testInserimentoNuovoArticolo(ArticoloService articoloServiceInstance,
			OrdineService ordineServiceInstance) throws Exception {

		System.out.println(".......testInserimentoNuovoArticolo inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);

		if (articoloInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoArticolo fallito ");

		System.out.println(".......testInserimentoNuovoArticolo fine: PASSED.............");
	}

	private static void testAggiornamentoArticoloEsistente(ArticoloService articoloServiceInstance) throws Exception {

		System.out.println(".......testAggiornamentoArticolo inizio.............");

		Articolo articoloEsistenteSulDb = articoloServiceInstance.caricaSingoloElemento(1L);

		String nuovaDescrizione = "cuffie bluetooth";
		articoloEsistenteSulDb.setDescrizione(nuovaDescrizione);
		articoloServiceInstance.aggiorna(articoloEsistenteSulDb);

		Articolo articoloReloaded = articoloServiceInstance.caricaSingoloElemento(articoloEsistenteSulDb.getId());
		if (!articoloEsistenteSulDb.getDescrizione().equals(articoloReloaded.getDescrizione()))
			throw new RuntimeException("testAggiornamentoArticolo fallito");

		System.out.println("testAggiornamentoArticolo passed");
	}

	private static void testInserimentoNuovaCategoria(CategoriaService categoriaServiceInstance) throws Exception {

		System.out.println(".......testInserimentoNuovaCategoria inizio.............");

		Categoria categoriaInstance = new Categoria("cuffie", "6390");
		categoriaServiceInstance.inserisciNuovo(categoriaInstance);

		if (categoriaInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovaCategoria fallito ");

		System.out.println(".......testInserimentoNuovaCategoria fine: PASSED.............");
	}

	private static void testAggiornamentoCategoriaEsistente(CategoriaService categoriaServiceInstance)
			throws Exception {

		System.out.println(".......testAggiornamentoCategoria inizio.............");

		Categoria categoriaPresenteSuDb = categoriaServiceInstance.caricaSingoloElemento(1L);

		String nuovaDescrizione = "cuffie wireless";
		categoriaPresenteSuDb.setDescrizione(nuovaDescrizione);
		categoriaServiceInstance.aggiorna(categoriaPresenteSuDb);

		Categoria categoriaReloaded = categoriaServiceInstance.caricaSingoloElemento(categoriaPresenteSuDb.getId());
		if (!categoriaPresenteSuDb.getDescrizione().equals(categoriaReloaded.getDescrizione()))
			throw new RuntimeException("testAggiornamentoCategoria fallito");

		System.out.println(".......testAggiornamentoCategoria passed.............");
	}

	private static void testAggiungiArticoloACategoria(CategoriaService categoriaServiceInstance,
			ArticoloService articoloServiceInstance) throws Exception {

		System.out.println(".......testAggiungiArticoloACategoria inizio.............");

		Categoria categoriaPresenteSulDb = categoriaServiceInstance.caricaSingoloElemento(1L);

		Articolo articoloPresenteSulDb = articoloServiceInstance.caricaSingoloElemento(1L);
		Set<Articolo> listaArticoli = new HashSet<>();
		listaArticoli.add(articoloPresenteSulDb);

		categoriaPresenteSulDb.setArticoli(listaArticoli);
		categoriaServiceInstance.aggiorna(categoriaPresenteSulDb);

		if (categoriaPresenteSulDb.getArticoli().size() != 1)
			throw new RuntimeException("testAggiungiArticoloACategoria fallito");

		System.out.println(".......testAggiungiArticoloACategoria passed.............");
	}

	private static void testAggiungiCategoriaAdArticolo(CategoriaService categoriaServiceInstance,
			ArticoloService articoloServiceInstance, OrdineService ordineServiceInstance) throws Exception {

		System.out.println(".......testAggiungiCategoriaAdArticolo inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);

		if (articoloInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoArticolo fallito ");

		Categoria nuovaCategoria = new Categoria("maglie", "30");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);

		if (nuovaCategoria.getId() == null)
			throw new RuntimeException("testInserimentoNuovaCategoria fallito ");

		articoloServiceInstance.aggiungiCategoria(articoloInstance, nuovaCategoria);

		Articolo articoloReloaded = articoloServiceInstance
				.caricaSingoloElementoEagerCategoria(articoloInstance.getId());

		if (articoloReloaded.getCategorie().isEmpty())
			throw new RuntimeException("testAggiungiCategoriaAdArticolo fallito");

		System.out.println(".......testAggiungiCategoriaAdArticolo passed.............");
	}

	private static void testRimuovArticoloEDisassociaCategorie(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception {

		System.out.println(".......testRimuovArticoloEDisassociaCategorie inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);

		if (articoloInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoArticolo fallito ");

		Categoria nuovaCategoria = new Categoria("maglie", "30");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);

		if (nuovaCategoria.getId() == null)
			throw new RuntimeException("testInserimentoNuovaCategoria fallito ");

		articoloServiceInstance.aggiungiCategoria(articoloInstance, nuovaCategoria);

		Articolo articoloReloaded = articoloServiceInstance.caricaSingoloElementoEagerCategoria(articoloInstance.getId());
		if (articoloReloaded.getCategorie().size() != 1)
			throw new RuntimeException("testRimuovArticoloEDisassociaCategorie fallito: 1 categoria e articolo non collegati ");

		articoloServiceInstance.rimuoviArticoloEDisassociaCategorie(articoloReloaded.getId());

		Articolo articoloSupposedToBeRemoved = articoloServiceInstance
				.caricaSingoloElementoEagerCategoria(articoloInstance.getId());
		if (articoloSupposedToBeRemoved != null)
			throw new RuntimeException("testRimuovArticoloEDisassociaCategorie fallito: rimozione non avvenuta ");

		System.out.println(".......testRimuovArticoloEDisassociaCategorie passed.............");
	}
	
	private static void testRimuoviCategoriaEDisassociaArticoli (CategoriaService categoriaServiceInstance, ArticoloService articoloServiceInstance, OrdineService ordineServiceInstance) throws Exception {
		
		System.out.println(".......testRimuoviCategoriaEDisassociaArticoli inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);

		if (articoloInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoArticolo fallito ");

		Categoria nuovaCategoria = new Categoria("maglie", "30");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);

		if (nuovaCategoria.getId() == null)
			throw new RuntimeException("testInserimentoNuovaCategoria fallito ");

		Set<Articolo> listaArticoli = new HashSet<>();
		listaArticoli.add(articoloInstance);
		nuovaCategoria.setArticoli(listaArticoli);
		
		categoriaServiceInstance.aggiorna(nuovaCategoria);

		if(nuovaCategoria.getArticoli().size() != 1)
			throw new RuntimeException("testRimuoviCategoriaEDisassociaArticoli fallito: 1 categoria e articolo non collegati ");
		
		categoriaServiceInstance.rimuoviCategoriaEDisassociaArticoli(nuovaCategoria.getId());
		
		Categoria categoriaSupposedToBeRemoved = categoriaServiceInstance
				.caricaSingoloElementoEagerArticolo(nuovaCategoria.getId());
		if (categoriaSupposedToBeRemoved != null)
			throw new RuntimeException("testRimuoviCategoriaEDisassociaArticoli fallito: rimozione non avvenuta ");

		System.out.println(".......testRimuoviCategoriaEDisassociaArticoli passed.............");
	}
	
	private static void testRimozioneOrdine(OrdineService ordineServiceInstance) throws Exception {
		
		System.out.println(".......testRimuoviOrdne inizio.............");
		
		Ordine ordineDaRimuovere= new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineDaRimuovere);
		
		Ordine ordineReloaded= ordineServiceInstance.caricaSingoloElementoEagerArticolo(ordineDaRimuovere.getId());
		if(!ordineReloaded.getArticoli().isEmpty())
			throw new OrdineConArticoliAssociatiException("l'ordine non puo essere rimosso, ha degli articoli associati ");

		ordineServiceInstance.rimuovi(ordineDaRimuovere.getId());	
		
		Ordine ordineSupposedToBeRemoved = ordineServiceInstance.caricaSingoloElemento(ordineDaRimuovere.getId());
		if(ordineSupposedToBeRemoved != null)
			throw new RuntimeException("testRimuoviOrdne fallito: rimozione non avvenuta ");
		
		System.out.println(".......testRimuoviOrdne passed.............");
	}

}

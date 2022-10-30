package it.prova.gestioneordiniarticolicategorie.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
			
			testOrdiniEffettuatiPerUnaDeterminataCategoria(ordineServiceInstance, articoloServiceInstance, categoriaServiceInstance);
			
			testCategorieArticoliDiUnDeterminatoOrdine(ordineServiceInstance, articoloServiceInstance, categoriaServiceInstance);
			
//			testSommaPrezzoArticoliDiUnaDetCategoria(ordineServiceInstance, articoloServiceInstance, categoriaServiceInstance);
			
			testCodiciDiCategorieOrdiniEffettuatiDuranteUnMese(ordineServiceInstance, articoloServiceInstance, categoriaServiceInstance);
			
			testSommaPrezziArticoliIndrizzatiAdUnDestinatario(ordineServiceInstance, articoloServiceInstance, categoriaServiceInstance);

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
	
	private static void testOrdiniEffettuatiPerUnaDeterminataCategoria(OrdineService ordineServiceInstance, ArticoloService articoloServiceInstance, 
			CategoriaService categoriaServiceInstance) throws Exception {
		
		System.out.println(".......testOrdiniEffettuatiPerUnaDeterminataCategoria inizio.............");

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
		
		Categoria categoriaConfronto = new Categoria("scarpe");
		categoriaServiceInstance.inserisciNuovo(categoriaConfronto);

		if (nuovaCategoria.getId() == null)
			throw new RuntimeException("testInserimentoNuovaCategoria fallito ");

		articoloServiceInstance.aggiungiCategoria(articoloInstance, nuovaCategoria);
		
		List<Ordine> listaOrdiniEffettuatiPerUnaDetCategoria= ordineServiceInstance.ordiniEffettuatiPerUnaDeterminataCategoria(nuovaCategoria);
		if(listaOrdiniEffettuatiPerUnaDetCategoria.isEmpty())
			throw new RuntimeException("testOrdiniEffettuatiPerUnaDeterminataCategoria fallito ");
		
		System.out.println(".......testOrdiniEffettuatiPerUnaDeterminataCategoria passed.............");
	}
	
	private static void testCategorieArticoliDiUnDeterminatoOrdine(OrdineService ordineServiceInstance, ArticoloService articoloServiceInstance, 
			CategoriaService categoriaServiceInstance) throws Exception {
		
		System.out.println(".......testCategorieArticoliDiUnDeterminatoOrdine inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Articolo articoloInstance2 = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloInstance2.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance2);

		Categoria nuovaCategoria = new Categoria("maglie", "30");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);
		Categoria nuovaCategoria2 = new Categoria("pc", "30");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria2);
		articoloServiceInstance.aggiungiCategoria(articoloInstance2, nuovaCategoria2);
		articoloServiceInstance.aggiungiCategoria(articoloInstance, nuovaCategoria);
		articoloServiceInstance.aggiorna(articoloInstance2);
		articoloServiceInstance.aggiorna(articoloInstance);
		
		List<String> listaCategorieDiUnDetOrdine= categoriaServiceInstance.categorieDegliarticoliDiUnDeterminatoOrdine(ordineInstance);
		if(listaCategorieDiUnDetOrdine.size() != 2)
			throw new RuntimeException("testCategorieArticoliDiUnDeterminatoOrdine fallito ");
		
		System.out.println(".......testCategorieArticoliDiUnDeterminatoOrdine passed.............");
	}
	
	private static void testSommaPrezzoArticoliDiUnaDetCategoria(OrdineService ordineServiceInstance, ArticoloService articoloServiceInstance, 
			CategoriaService categoriaServiceInstance) throws Exception {
		
		System.out.println(".......testSommaPrezzoArticoliDiUnaDetCategoria inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Articolo articoloInstance2 = new Articolo("cuffie", "0567uy", 100,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloInstance2.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance2);

		Categoria nuovaCategoria = new Categoria("maglie", "30");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);

		Set<Articolo> listaArticoli = new HashSet<>();
		listaArticoli.add(articoloInstance);
		listaArticoli.add(articoloInstance2);
		nuovaCategoria.setArticoli(listaArticoli);
		
		categoriaServiceInstance.aggiorna(nuovaCategoria);
		
		if(nuovaCategoria.getArticoli().isEmpty())
			throw new RuntimeException("la categoria e gli articoli non sono collegati ");
		
		if(articoloServiceInstance.sommaPrezziDegliArticoliDiUnaCategoria(nuovaCategoria) < 0)
			throw new RuntimeException("testSommaPrezzoArticoliDiUnaDetCategoria fallito ");
		
		System.out.println(".......testSommaPrezzoArticoliDiUnaDetCategoria passed.............");
	}
	
	private static void testCodiciDiCategorieOrdiniEffettuatiDuranteUnMese(OrdineService ordineServiceInstance, ArticoloService articoloServiceInstance, 
			CategoriaService categoriaServiceInstance) throws Exception {
		
		System.out.println(".......testCodiciDiCategorieOrdiniEffettuatiDuranteUnMese inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		
		Categoria nuovacategoria=new Categoria("maglie", "30");
		categoriaServiceInstance.aggiungiArticolo(nuovacategoria, articoloInstance);
		categoriaServiceInstance.inserisciNuovo(nuovacategoria);
		
		Date dataConfronto = new SimpleDateFormat("dd-MM-yyyy").parse("03-09-2022");
		
		List<String> listaCodiciOrdiniEffetuatiDuranteUnMese=categoriaServiceInstance.codiciDiCategorieOrdiniEffettuatiDuranteUnMese(dataConfronto);
		if(listaCodiciOrdiniEffetuatiDuranteUnMese.size() != 1)
            throw new RuntimeException("testCodiciDiCategorieOrdiniEffettuatiDuranteUnMese fallito ");
		
		System.out.println(".......testCodiciDiCategorieOrdiniEffettuatiDuranteUnMese passed.............");
	}
	
	private static void testSommaPrezziArticoliIndrizzatiAdUnDestinatario(OrdineService ordineServiceInstance, ArticoloService articoloServiceInstance, 
	CategoriaService categoriaServiceInstance) throws Exception {
		
		System.out.println(".......testSommaPrezziArticoliIndrizzatiAdUnDestinatario inizio.............");

		Articolo articoloInstance = new Articolo("cuffie", "0567uy", 50,
				new SimpleDateFormat("dd/MM/yyyy").parse("24/07/2022"));
		Ordine ordineInstance = new Ordine("marco", "via mosca", new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2022"),
				new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2022"));
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		articoloInstance.setOrdine(ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		
		Categoria nuovacategoria=new Categoria("maglie", "30");
		categoriaServiceInstance.aggiungiArticolo(nuovacategoria, articoloInstance);
		categoriaServiceInstance.inserisciNuovo(nuovacategoria);
		
		String nomeDestinatario="marco";
		if(articoloServiceInstance.sommaPrezziArticoliIndrizzatiAdUnDestinatario(nomeDestinatario) < 50)
           throw new RuntimeException("testSommaPrezziArticoliIndrizzatiAdUnDestinatario fallito ");
		
		System.out.println(".......testSommaPrezziArticoliIndrizzatiAdUnDestinatario passed.............");
	}

}

package ma.projet;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ProduitServicesMinimalTest {

    // on va leur injecter un SessionFactory proxy
    private ProduitService produitService;
    private CategorieService categorieService;
    private CommandeService commandeService;
    private LigneCommandeService ligneCommandeService;

    // in-memory store pour simuler les entités en base
    private InMemoryStore store;
    private SessionFactory sessionFactoryProxy;

    @BeforeEach
    public void setup() throws Exception {
        // instancie services
        produitService = new ProduitService();
        categorieService = new CategorieService();
        commandeService = new CommandeService();
        ligneCommandeService = new LigneCommandeService();

        // in-memory store
        store = new InMemoryStore();

        // crée le proxy SessionFactory qui renvoie un Session proxy
        sessionFactoryProxy = createSessionFactoryProxy(store);

        // injecte le proxy dans chaque service (reflection sur champ private sessionFactory)
        injectSessionFactory(produitService, sessionFactoryProxy);
        injectSessionFactory(categorieService, sessionFactoryProxy);
        injectSessionFactory(commandeService, sessionFactoryProxy);
        injectSessionFactory(ligneCommandeService, sessionFactoryProxy);
    }

    // ---------- TESTS ----------

    @Test
    public void testCreateFindUpdateDeleteProduitAndCategorie() {
        // create catégorie
        Categorie c = new Categorie();
        c.setCode("C1");
        c.setLibelle("Catégorie 1");
        categorieService.create(c);
        assertNotNull(c.getId());
        // create produit
        Produit p = new Produit();
        p.setReference("P-1");
        p.setPrix(120f);
        p.setCategorie(c);
        p.setLignes(new HashSet<>());
        produitService.create(p);
        assertNotNull(p.getId());

        // findById
        Produit loaded = produitService.findById(p.getId());
        assertEquals("P-1", loaded.getReference());
        assertEquals(120f, loaded.getPrix(), 0.001);

        // update
        loaded.setPrix(130f);
        produitService.update(loaded);
        Produit updated = produitService.findById(loaded.getId());
        assertEquals(130f, updated.getPrix(), 0.001);

        // findAll contains the product
        List<Produit> all = produitService.findAll();
        assertTrue(all.stream().anyMatch(x -> "P-1".equals(x.getReference())));

        // delete
        produitService.delete(updated);
        Produit afterDelete = produitService.findById(updated.getId());
        assertNull(afterDelete);
    }

    @Test
    public void testFindByCategorie() {
        // create categories
        Categorie ca = new Categorie(); ca.setCode("A"); ca.setLibelle("A"); categorieService.create(ca);
        Categorie cb = new Categorie(); cb.setCode("B"); cb.setLibelle("B"); categorieService.create(cb);

        // products
        Produit p1 = new Produit(); p1.setReference("X"); p1.setPrix(80f); p1.setCategorie(ca); p1.setLignes(new HashSet<>()); produitService.create(p1);
        Produit p2 = new Produit(); p2.setReference("Y"); p2.setPrix(200f); p2.setCategorie(ca); p2.setLignes(new HashSet<>()); produitService.create(p2);
        Produit p3 = new Produit(); p3.setReference("Z"); p3.setPrix(50f); p3.setCategorie(cb); p3.setLignes(new HashSet<>()); produitService.create(p3);

        List<Produit> catA = produitService.findByCategorie(ca.getId());
        assertTrue(catA.stream().anyMatch(p -> "X".equals(p.getReference())));
        assertTrue(catA.stream().anyMatch(p -> "Y".equals(p.getReference())));
        assertFalse(catA.stream().anyMatch(p -> "Z".equals(p.getReference())));
    }

    @Test
    public void testProduitsCommandeAndBetweenDatesAndDistinct() {
        // create product and commande + lignes
        Produit pa = new Produit(); pa.setReference("PA"); pa.setPrix(150f); pa.setLignes(new HashSet<>()); produitService.create(pa);
        Produit pb = new Produit(); pb.setReference("PB"); pb.setPrix(60f); pb.setLignes(new HashSet<>()); produitService.create(pb);

        // commandes with dates
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JANUARY, 1,0,0,0);
        Date d1 = cal.getTime();
        cal.set(2025, Calendar.FEBRUARY, 15,0,0,0);
        Date d2 = cal.getTime();

        Commande cmd1 = new Commande(); cmd1.setDate(d1); commandeService.create(cmd1);
        Commande cmd2 = new Commande(); cmd2.setDate(d2); commandeService.create(cmd2);

        // lignes: pa in cmd1, pb in cmd2, pa also in cmd2 (to test distinct)
        LigneCommandeProduit l1 = new LigneCommandeProduit(); l1.setProduit(pa); l1.setCommande(cmd1); l1.setQuantite(2); ligneCommandeService.create(l1);
        LigneCommandeProduit l2 = new LigneCommandeProduit(); l2.setProduit(pb); l2.setCommande(cmd2); l2.setQuantite(1); ligneCommandeService.create(l2);
        LigneCommandeProduit l3 = new LigneCommandeProduit(); l3.setProduit(pa); l3.setCommande(cmd2); l3.setQuantite(3); ligneCommandeService.create(l3);

        // produits d'une commande (cmd1)
        List<Produit> produitsCmd1 = produitService.findByProduitsCommande(cmd1.getId());
        assertEquals(1, produitsCmd1.size());
        assertEquals("PA", produitsCmd1.get(0).getReference());

        // produits entre dates: range covering only cmd1
        Calendar a = Calendar.getInstance(); a.set(2024, Calendar.DECEMBER, 31); Date from = a.getTime();
        Calendar b = Calendar.getInstance(); b.set(2025, Calendar.JANUARY, 2); Date to = b.getTime();
        List<Produit> between = produitService.findByProduitsBetweenTwoDates(from, to);
        assertEquals(1, between.size());
        assertEquals("PA", between.get(0).getReference());

        // range covering both commands
        a.set(2024, Calendar.DECEMBER, 1); from = a.getTime();
        b.set(2025, Calendar.MARCH, 1); to = b.getTime();
        List<Produit> betweenAll = produitService.findByProduitsBetweenTwoDates(from, to);
        assertTrue(betweenAll.stream().anyMatch(p -> "PA".equals(p.getReference())));
        assertTrue(betweenAll.stream().anyMatch(p -> "PB".equals(p.getReference())));
    }

    @Test
    public void testNamedQueryPriceGreaterThan100() {
        Produit pSmall = new Produit(); pSmall.setReference("S"); pSmall.setPrix(90f); pSmall.setLignes(new HashSet<>()); produitService.create(pSmall);
        Produit pLarge = new Produit(); pLarge.setReference("L"); pLarge.setPrix(200f); pLarge.setLignes(new HashSet<>()); produitService.create(pLarge);

        List<Produit> result = produitService.findByPriceGreaterThan100();
        assertTrue(result.stream().anyMatch(p -> "L".equals(p.getReference())));
        assertFalse(result.stream().anyMatch(p -> "S".equals(p.getReference())));
    }

    // ---------- helpers & in-memory store & proxy factories ----------

    private void injectSessionFactory(Object service, SessionFactory sf) throws Exception {
        Field f = null;
        Class<?> c = service.getClass();
        while (c != null) {
            try {
                f = c.getDeclaredField("sessionFactory");
                break;
            } catch (NoSuchFieldException ex) {
                c = c.getSuperclass();
            }
        }
        if (f == null) throw new RuntimeException("sessionFactory field not found in " + service.getClass());
        f.setAccessible(true);
        f.set(service, sf);
    }

    // crée proxy SessionFactory simple : getCurrentSession() -> session proxy
    private SessionFactory createSessionFactoryProxy(InMemoryStore store) {
        InvocationHandler sfHandler = (proxy, method, args) -> {
            if ("getCurrentSession".equals(method.getName())) {
                return createSessionProxy(store);
            }
            // other methods: unsupported in this test context
            throw new UnsupportedOperationException("Method not supported in proxy: " + method.getName());
        };
        return (SessionFactory) Proxy.newProxyInstance(
                SessionFactory.class.getClassLoader(),
                new Class[]{SessionFactory.class},
                sfHandler);
    }

    // crée proxy Session qui expose un petit sous-ensemble de méthodes utilisés
    private Session createSessionProxy(InMemoryStore store) {
        InvocationHandler sessHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if ("save".equals(name) && args.length == 1) {
                    return store.save(args[0]);
                }
                if ("update".equals(name) && args.length == 1) {
                    store.update(args[0]);
                    return null;
                }
                if ("delete".equals(name) && args.length == 1) {
                    store.delete(args[0]);
                    return null;
                }
                if ("get".equals(name) && args.length == 2) {
                    Class<?> cls = (Class<?>) args[0];
                    Serializable id = (Serializable) args[1];
                    return store.get(cls, id);
                }
                if ("createQuery".equals(name) && args.length == 2) {
                    String q = (String) args[0];
                    Class<?> cls = (Class<?>) args[1];
                    return createQueryProxy(store, q, cls);
                }
                if ("createNamedQuery".equals(name) && args.length == 2) {
                    String nameQuery = (String) args[0];
                    Class<?> cls = (Class<?>) args[1];
                    // map named queries to JPQL (we only support the one used)
                    if ("Produit.findByPriceGreaterThan100".equals(nameQuery)) {
                        return createQueryProxy(store, "select p from Produit p where p.prix > :prix", cls);
                    }
                    throw new UnsupportedOperationException("NamedQuery not handled: " + nameQuery);
                }
                throw new UnsupportedOperationException("Session method not supported in proxy: " + name);
            }
        };

        return (Session) Proxy.newProxyInstance(
                Session.class.getClassLoader(),
                new Class[]{Session.class},
                sessHandler);
    }

    // crée proxy Query minimal (setParameter + list/getResultList)
    private Query createQueryProxy(InMemoryStore store, String jpql, Class<?> resultClass) {
        Map<String, Object> params = new HashMap<>();
        InvocationHandler qHandler = (proxy, method, args) -> {
            String name = method.getName();
            if ("setParameter".equals(name) && args.length >= 2 && args[0] instanceof String) {
                params.put((String) args[0], args[1]);
                return proxy; // for chaining
            }
            if (("list".equals(name) || "getResultList".equals(name)) && (args == null || args.length == 0)) {
                // evaluate JPQL for the small set of supported patterns
                List<?> res = evaluateJPQL(store, jpql, params, resultClass);
                return res;
            }
            // some code calls .setParameter(name, value) with three args (TemporalType) etc. We don't support that here.
            throw new UnsupportedOperationException("Query method not supported in proxy: " + name);
        };

        return (Query) Proxy.newProxyInstance(
                Query.class.getClassLoader(),
                new Class[]{Query.class},
                qHandler);
    }

    // évalue uniquement les patterns JPQL requis par les services
    @SuppressWarnings("unchecked")
    private <T> List<T> evaluateJPQL(InMemoryStore store, String jpql, Map<String, Object> params, Class<T> resultClass) {
        jpql = jpql.trim();
        if ("from Produit".equalsIgnoreCase(jpql)) {
            return (List<T>) new ArrayList<>(store.getAll(Produit.class));
        }
        if (jpql.toLowerCase().contains("from produit p where p.categorie.id = :categorieid")) {
            Object catId = params.get("categorieId");
            if (catId == null) catId = params.get("categorieid");
            Integer idVal = (Integer) catId;
            List<T> res = new ArrayList<>();
            for (Produit p : store.getAll(Produit.class)) {
                if (p.getCategorie() != null && p.getCategorie().getId() != null && p.getCategorie().getId().equals(idVal)) {
                    res.add((T) p);
                }
            }
            return res;
        }
        // select distinct l.produit from LigneCommandeProduit l where l.commande.date between :d1 and :d2
        if (jpql.toLowerCase().contains("from lignecommandeproduit l") && jpql.toLowerCase().contains("l.commande.date between")) {
            Date d1 = (Date) params.get("d1");
            Date d2 = (Date) params.get("d2");
            Set<Produit> set = new LinkedHashSet<>();
            for (LigneCommandeProduit l : store.getAll(LigneCommandeProduit.class)) {
                Commande cmd = l.getCommande();
                if (cmd != null && cmd.getDate() != null && !cmd.getDate().before(d1) && !cmd.getDate().after(d2)) {
                    set.add(l.getProduit());
                }
            }
            return (List<T>) new ArrayList<>(set);
        }
        // select distinct l.produit from LigneCommandeProduit l where l.commande.id = :commandeId
        if (jpql.toLowerCase().contains("from lignecommandeproduit l") && jpql.toLowerCase().contains("l.commande.id = :commandeid")) {
            Integer comId = (Integer) params.get("commandeId");
            if (comId == null) comId = (Integer) params.get("commandeid");
            Set<Produit> set = new LinkedHashSet<>();
            for (LigneCommandeProduit l : store.getAll(LigneCommandeProduit.class)) {
                Commande cmd = l.getCommande();
                if (cmd != null && cmd.getId() != null && cmd.getId().equals(comId)) {
                    set.add(l.getProduit());
                }
            }
            return (List<T>) new ArrayList<>(set);
        }

        // named query for price >
        if (jpql.toLowerCase().startsWith("select p from produit p where p.prix >")) {
            Number prix = (Number) params.get("prix");
            double threshold = prix.doubleValue();
            List<T> res = new ArrayList<>();
            for (Produit p : store.getAll(Produit.class)) {
                if (p.getPrix() > threshold) res.add((T) p);
            }
            return res;
        }

        // fallback empty
        return Collections.emptyList();
    }

    // ---------- InMemoryStore : minimal CRUD ----------

    private static class InMemoryStore {
        private final Map<Class<?>, Map<Integer, Object>> data = new HashMap<>();
        private final Map<Class<?>, AtomicInteger> seq = new HashMap<>();

        public synchronized Serializable save(Object obj) {
            Class<?> cls = obj.getClass();
            seq.putIfAbsent(cls, new AtomicInteger(0));
            int id = seq.get(cls).incrementAndGet();
            // try to set 'id' property via reflection if exists
            try {
                Field f = findField(cls, "id");
                if (f != null) {
                    f.setAccessible(true);
                    // set Integer id
                    f.set(obj, id);
                }
            } catch (IllegalAccessException ignored) {}
            data.computeIfAbsent(cls, k -> new LinkedHashMap<>()).put(id, deepCopy(obj));
            return id;
        }

        public synchronized void update(Object obj) {
            Class<?> cls = obj.getClass();
            Field f = findField(cls, "id");
            if (f == null) throw new RuntimeException("no id field on " + cls);
            try {
                f.setAccessible(true);
                Object idObj = f.get(obj);
                if (idObj == null) throw new RuntimeException("id is null on update for " + cls);
                Integer id = (Integer) idObj;
                Map<Integer, Object> map = data.computeIfAbsent(cls, k -> new LinkedHashMap<>());
                map.put(id, deepCopy(obj));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized void delete(Object obj) {
            Class<?> cls = obj.getClass();
            Field f = findField(cls, "id");
            if (f == null) return;
            try {
                f.setAccessible(true);
                Object idObj = f.get(obj);
                if (idObj == null) return;
                Integer id = (Integer) idObj;
                Map<Integer, Object> map = data.get(cls);
                if (map != null) map.remove(id);
            } catch (IllegalAccessException ignored) {}
        }

        @SuppressWarnings("unchecked")
        public synchronized <T> T get(Class<T> cls, Serializable id) {
            Map<Integer, Object> map = data.get(cls);
            if (map == null) return null;
            Object found = map.get((Integer) id);
            return (T) deepCopy(found);
        }

        @SuppressWarnings("unchecked")
        public synchronized <T> List<T> getAll(Class<T> cls) {
            Map<Integer, Object> map = data.get(cls);
            if (map == null) return Collections.emptyList();
            List<T> list = new ArrayList<>();
            for (Object o : map.values()) {
                list.add((T) deepCopy(o));
            }
            return list;
        }

        // find declared field name (walk superclasses)
        private static Field findField(Class<?> cls, String name) {
            Class<?> c = cls;
            while (c != null) {
                try {
                    return c.getDeclaredField(name);
                } catch (NoSuchFieldException ex) {
                    c = c.getSuperclass();
                }
            }
            return null;
        }

        // very naive shallow "copy": we just return the same object in this test context,
        // but to avoid accidental modification we could create clones. For simplicity we return the object itself.
        private Object deepCopy(Object o) {
            return o;
        }
    }
}


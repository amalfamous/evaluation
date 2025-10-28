package org.example.util;


import java.util.Properties;

import org.example.classes.Categorie;
import org.example.classes.Commande;
import org.example.classes.LigneCommandeProduit;
import org.example.classes.Produit;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            // load properties file
            Properties props = new Properties();
            props.load(HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties"));

            configuration.setProperties(props);

            // register annotated classes
            configuration.addAnnotatedClass(Categorie.class);
            configuration.addAnnotatedClass(Produit.class);
            configuration.addAnnotatedClass(Commande.class);
            configuration.addAnnotatedClass(LigneCommandeProduit.class);

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

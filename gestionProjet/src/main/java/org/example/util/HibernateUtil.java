package org.example.util;

import org.example.classes.Employe;
import org.example.classes.EmployeTache;
import org.example.classes.Projet;
import org.example.classes.Tache;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Properties;
import java.io.InputStream;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            InputStream in = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties");
            Properties props = new Properties();
            if (in != null) props.load(in);
            configuration.setProperties(props);

            configuration.addAnnotatedClass(Employe.class);
            configuration.addAnnotatedClass(Projet.class);
            configuration.addAnnotatedClass(Tache.class);
            configuration.addAnnotatedClass(EmployeTache.class);

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


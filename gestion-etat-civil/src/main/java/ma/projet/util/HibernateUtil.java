package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.FileInputStream;
import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Properties properties = new Properties();
            properties.load(HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties"));

            Configuration configuration = new Configuration();

            // Configuration de la base de données
            configuration.setProperty("hibernate.connection.driver_class", properties.getProperty("db.driver"));
            configuration.setProperty("hibernate.connection.url", properties.getProperty("db.url"));
            configuration.setProperty("hibernate.connection.username", properties.getProperty("db.user"));
            configuration.setProperty("hibernate.connection.password", properties.getProperty("db.password"));

            // Configuration Hibernate
            configuration.setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"));
            configuration.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"));
            configuration.setProperty("hibernate.show_sql", properties.getProperty("hibernate.show_sql"));
            configuration.setProperty("hibernate.format_sql", properties.getProperty("hibernate.format_sql"));

            // Enregistrer les entités
            configuration.addAnnotatedClass(ma.projet.beans.Personne.class);
            configuration.addAnnotatedClass(ma.projet.beans.Homme.class);
            configuration.addAnnotatedClass(ma.projet.beans.Femme.class);
            configuration.addAnnotatedClass(ma.projet.beans.Mariage.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Échec de l'initialisation de SessionFactory : " + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
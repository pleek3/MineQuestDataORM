package com.minequest.dataorm;


import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class HibernateListenerIntegrator implements Integrator {

  private final DataCompound compound;

  public HibernateListenerIntegrator(DataCompound compound) {
    this.compound = compound;
    //todo:
  }

  @Override
  public void integrate(Metadata metadata, BootstrapContext bootstrapContext, SessionFactoryImplementor sessionFactory) {
    Integrator.super.integrate(metadata, bootstrapContext, sessionFactory);
  }

  @Override
  public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

  }
}

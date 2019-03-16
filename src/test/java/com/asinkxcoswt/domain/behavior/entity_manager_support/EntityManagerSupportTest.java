package com.asinkxcoswt.domain.behavior.entity_manager_support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EntityManagerSupportTestConfiguration.class})
public class EntityManagerSupportTest {

    @Autowired
    private FooRepository fooRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void withoutCallingDetach_shouldSaveChanges() {
        // prepare
        FooEntity fooEntity = fooRepository.create();
        fooEntity = fooRepository.saveAndFlush(fooEntity);

        // get entity from repo and change a column
        FooEntity entityFromRepo = fooRepository.getOne(fooEntity.getUuid());
        entityFromRepo.setFoo("hello");
        fooRepository.flush();

        // later, verify that the previous change was saved
        FooEntity entityFromRepoLater = fooRepository.getOne(fooEntity.getUuid());
        Assertions.assertEquals("hello", entityFromRepoLater.getFoo());
    }

    @Test
    @Transactional
    public void withCallingDetach_shouldNotSaveChanges() {
        // prepare
        FooEntity fooEntity = fooRepository.create();
        fooEntity = fooRepository.saveAndFlush(fooEntity);

        // get entity from repo and change a column after calling detach()
        FooEntity entityFromRepo = fooRepository.getOne(fooEntity.getUuid());
        entityFromRepo.detach();
        entityFromRepo.setFoo("hello");
        fooRepository.flush();

        // later, verify that the previous change was not saved
        FooEntity entityFromRepoLater = fooRepository.getOne(fooEntity.getUuid());
        Assertions.assertNotEquals("hello", entityFromRepoLater.getFoo());
    }

    @Test
    @Transactional
    public void beforeAndAfterCallingDetach_changeBeforeShouldBeSaveButChangeAfterShouldNot() {
        // prepare
        FooEntity fooEntity = fooRepository.create();
        fooEntity = fooRepository.saveAndFlush(fooEntity);

        // get entity from repo and change columns before and after calling detach()
        FooEntity entityFromRepo = fooRepository.getOne(fooEntity.getUuid());
        entityFromRepo.setFoo("hello");
        fooRepository.flush();
        entityFromRepo.detach();
        entityFromRepo.setBar("hello");
        fooRepository.flush();

        // later, verify that the previous change was not saved only if it was done after detach()
        FooEntity entityFromRepoLater = fooRepository.getOne(fooEntity.getUuid());
        Assertions.assertEquals("hello", entityFromRepoLater.getFoo());
        Assertions.assertNotEquals("hello", entityFromRepoLater.getBar());
    }

    @Test
    @Transactional
    public void shouldNotSaveAnythingIfRollback() {
        // prepare
        FooEntity fooEntity = fooRepository.create();
        fooEntity = fooRepository.saveAndFlush(fooEntity);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // get entity from repo and change columns before and after calling detach()
        TestTransaction.start();
        TestTransaction.flagForRollback();
        FooEntity entityFromRepo = fooRepository.getOne(fooEntity.getUuid());
        entityFromRepo.setFoo("hello");
        fooRepository.flush();
        entityFromRepo.detach();
        entityFromRepo.setBar("hello");
        fooRepository.flush();
        TestTransaction.end();

        // later, verify that the previous change was not saved only if it was done after detach()
        TestTransaction.start();
        FooEntity entityFromRepoLater = fooRepository.getOne(fooEntity.getUuid());
        Assertions.assertNotEquals("hello", entityFromRepoLater.getFoo());
        Assertions.assertNotEquals("hello", entityFromRepoLater.getBar());
        TestTransaction.end();
    }
}

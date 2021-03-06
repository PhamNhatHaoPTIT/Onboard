package vn.com.vng.zalopay.promotion.onboarding.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import vn.com.vng.zalopay.promotion.onboarding.transaction.repository.PersonRepository;
import vn.com.vng.zalopay.promotion.onboarding.transaction.service.InnerRequiredServiceImpl;
import vn.com.vng.zalopay.promotion.onboarding.transaction.service.InnerRequiresNewServiceImpl;
import vn.com.vng.zalopay.promotion.onboarding.transaction.service.OuterTransactionalService;
import vn.com.vng.zalopay.promotion.onboarding.transaction.service.OuterTransactionalServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;
import static org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 * @author huyvha
 */
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@AutoConfigureJdbc
@AutoConfigureTestDatabase
@ComponentScan
@TestPropertySource(properties = {
        "logging.level.ROOT=INFO",
        "logging.level.org.springframework.jdbc.core=DEBUG",
        "logging.level.org.springframework.transaction=TRACE"
})
public class TransactionalTest {

    @Autowired
    private OuterTransactionalService outerTransactionalService; // SUT

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void test1() {
        // GIVEN transaction propagation setup with "REQUIRES_NEW" inside "REQUIRED"

        // WHEN
        try {
            outerTransactionalService.executeRequiresNewInsideRequired();
            fail("Exception must be thrown out");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Error occurs"));
        }

        // THEN both transactions are rolled back
        assertThat(personRepository.countByName(OuterTransactionalServiceImpl.class.getSimpleName()), is(0));
        assertThat(personRepository.countByName(InnerRequiresNewServiceImpl.class.getSimpleName()), is(0));
    }

    @Test
    public void test2() {
        // GIVEN mixed transaction propagation setup

        // WHEN
        try {
            outerTransactionalService.executeMixedPropagation();
            fail("Exception must be thrown out");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Error occurs"));
        }

        // THEN all transactions are rolled back
        assertThat(personRepository.countByName(InnerRequiredServiceImpl.class.getSimpleName()), is(0));
        assertThat(personRepository.countByName(InnerRequiresNewServiceImpl.class.getSimpleName()), is(0));
    }

    @Test
    public void test3() {
        // GIVEN mixed transaction propagation setup

        // WHEN
        try {
            outerTransactionalService.executeSuccessInInnerTransaction();
            fail("Exception must be thrown out");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Error occurs"));
        }

        // THEN "REQUIRES_NEW" transaction committed successfully
        assertThat(personRepository.countByName(InnerRequiredServiceImpl.class.getSimpleName()), is(0));
        assertThat(personRepository.countByName(InnerRequiresNewServiceImpl.class.getSimpleName()), is(1));
    }

    @Test
    public void testBean() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TransactionalTest.class);
        String[] beans = applicationContext.getBeanDefinitionNames();
        for(String x : beans) {
            System.out.println(x);
        }
    }

}

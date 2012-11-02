package es.icarto.gvsig.viasobras.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.icarto.gvsig.viasobras.domain.tests.AllCatalogTests;
import es.icarto.gvsig.viasobras.forms.tests.AllFormTests;
import es.icarto.gvsig.viasobras.queries.tests.QueriesTests;

@RunWith(Suite.class)
@SuiteClasses({ AllCatalogTests.class, AllFormTests.class, QueriesTests.class })
public class AllTests {

}

package es.icarto.gvsig.viasobras.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.icarto.gvsig.viasobras.tests.catalog.AllTestsCatalog;
import es.icarto.gvsig.viasobras.tests.forms.AllTestsForm;
import es.icarto.gvsig.viasobras.tests.queries.TestsQueries;

@RunWith(Suite.class)
@SuiteClasses({ AllTestsCatalog.class, AllTestsForm.class, TestsQueries.class })
public class AllTests {

}

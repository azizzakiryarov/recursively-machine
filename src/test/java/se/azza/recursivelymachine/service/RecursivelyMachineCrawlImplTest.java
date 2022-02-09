package se.azza.recursivelymachine.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.azza.recursivelymachine.model.RecursivelyMachine;
import java.io.IOException;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RecursivelyMachineCrawlImplTest {

    RecursivelyMachineCrawlImpl recursivelyMachineCrawl = new RecursivelyMachineCrawlImpl();

    RecursivelyMachine recursivelyMachine = new RecursivelyMachine();

    public final static String VALID_URL = "https://tretton37.com/";
    public final static String INVALID_URL = "htp://tretton37.com/";
    public final static String EMPTY_URL = "";

    @Test
    public void shouldRunWhileUrlIsNotEmptyOrPagesToVisitSizeNotEqualOne() throws IOException {
        recursivelyMachineCrawl.searchValidLinks(VALID_URL);
        assertNotEquals(0, recursivelyMachineCrawl.getPagesToVisit().size());
        assertNotEquals(0, recursivelyMachineCrawl.getLinks().size());
        assertEquals(445, recursivelyMachineCrawl.getLinks().size());
    }

    @Test
    public void shouldntRunWhileUrlIsEmptyOrPagesToVisitSizeNotEqualOne() throws IOException {
        recursivelyMachineCrawl.searchValidLinks(EMPTY_URL);
        assertEquals(0, recursivelyMachineCrawl.getLinks().size());

        recursivelyMachine.getPagesToVisit().add(VALID_URL);
        recursivelyMachineCrawl.searchValidLinks(EMPTY_URL);
        assertEquals(0, recursivelyMachineCrawl.getLinks().size());
    }

    @Test
    public void throwIllegalArgumentExceptionWhenJsoupGettingInvalidUrl() {
        Assert.assertThrows(IllegalArgumentException.class, ()-> recursivelyMachineCrawl.crawling(INVALID_URL));
    }
}

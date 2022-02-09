package se.azza.recursivelymachine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
@Setter
public class RecursivelyMachine {

    static Logger logger = Logger.getLogger(String.valueOf(RecursivelyMachine.class));
    private final Set<String> pagesVisited = new HashSet<>();
    private final Set<String> pagesToVisit = new HashSet<>();
}
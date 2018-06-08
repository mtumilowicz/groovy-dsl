package dsl

import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input
import org.xmlunit.diff.Diff
import spock.lang.Specification
/**
 * Created by mtumilowicz on 2018-06-07.
 */
class DslTest extends Specification {
    
    def "xml"() {
        given:
        String deadlineMemo = DeadlineMemo.make {
            title Constants.titleText
            deadline Constants.deadlineText
            idea Constants.ideaText
            plan Constants.planText
            xml
        }
        
        when:
        Diff d = DiffBuilder.compare(Input.fromFile(new File("src/test/resources/DslTest.xml")))
                .withTest(Input.fromString(deadlineMemo))
                .ignoreWhitespace()
                .ignoreComments()
                .normalizeWhitespace()
                .build()
        
        then:
        !d.hasDifferences()
    }

    def "text"() {
        given:
        String deadlineMemo = DeadlineMemo.make {
            title Constants.titleText
            deadline Constants.deadlineText
            idea Constants.ideaText
            plan Constants.planText
            text
        }
        
        and:
        String control =
                "Memo\n" +
                        "Title: IMPORTANT\n" +
                        "Deadline: 2020-01-01\n" +
                        "idea: Be a better programmer!\n" +
                        "plan: Commit to github everyday!\n"
        
        expect:
        control == deadlineMemo
    }

    def "json"() {
        given:
        String deadlineMemo = DeadlineMemo.make {
            title Constants.titleText
            deadline Constants.deadlineText
            idea Constants.ideaText
            plan Constants.planText
            json
        }
        
        and:
        String control = '{"memo":{"title":"IMPORTANT","deadline":"2020-01-01","idea":"Be a better programmer!","plan":"Commit to github everyday!"}}'

        expect:
        deadlineMemo == control
    }
}
package dsl.xml

import dsl.DeadlineMemo
import dsl.ToDo
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input
import org.xmlunit.diff.Diff
import spock.lang.Specification

import java.time.LocalDate
/**
 * Created by mtumilowicz on 2018-06-09.
 */
class DeadlineMemoXmlConverterTest extends Specification {
    def "test xml"() {
        given:
        def toDo = [new ToDo(title: 'idea', body: 'Be a better programmer!'),
                    new ToDo(title: 'plan', body: 'Commit to github everyday!')]
        def memo = new DeadlineMemo(title: 'IMPORTANT',
                deadline: LocalDate.parse('2020-01-01'),
                toDo: toDo)
        when:
        Diff d = DiffBuilder.compare(Input.fromFile(new File('src/test/resources/DslTest.xml')))
                .withTest(Input.fromString(DeadlineMemoXmlConverter.xml(memo)))
                .ignoreWhitespace()
                .ignoreComments()
                .normalizeWhitespace()
                .build()

        then:
        !d.hasDifferences()
    }
}

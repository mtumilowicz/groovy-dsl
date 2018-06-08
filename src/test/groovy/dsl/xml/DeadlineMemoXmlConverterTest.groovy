package dsl.xml

import dsl.DeadlineMemo
import dsl.ToDo
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input
import org.xmlunit.diff.Diff
import spock.lang.Specification

import java.time.LocalDate

import static dsl.Constants.getDeadlineText
import static dsl.Constants.getIdeaText
import static dsl.Constants.getPlanText
import static dsl.Constants.getTitleText

/**
 * Created by mtumilowicz on 2018-06-09.
 */
class DeadlineMemoXmlConverterTest extends Specification {
    def "test xml"() {
        given:
        def toDo = [new ToDo(title: "idea", body: ideaText), new ToDo(title: "plan", body: planText)]
        def memo = new DeadlineMemo(title: titleText,
                deadline: LocalDate.parse(deadlineText),
                toDo: toDo)
        when:
        Diff d = DiffBuilder.compare(Input.fromFile(new File("src/test/resources/DslTest.xml")))
                .withTest(Input.fromString(DeadlineMemoXmlConverter.xml(memo)))
                .ignoreWhitespace()
                .ignoreComments()
                .normalizeWhitespace()
                .build()

        then:
        !d.hasDifferences()
    }
}

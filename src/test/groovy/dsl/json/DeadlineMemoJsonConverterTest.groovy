package dsl.json

import dsl.DeadlineMemo
import dsl.ToDo
import spock.lang.Specification

import java.time.LocalDate

import static dsl.Constants.*
/**
 * Created by mtumilowicz on 2018-06-08.
 */
class DeadlineMemoJsonConverterTest extends Specification {
    def "test json"() {
        given:
        def toDo = [new ToDo(title: "idea", body: ideaText), new ToDo(title: "plan", body: planText)]
        def memo = new DeadlineMemo(title: titleText,
                deadline: LocalDate.parse(deadlineText),
                toDo: toDo)

        when:
        def json = DeadlineMemoJsonConverter.json(memo)

        then:
        json == '{"memo":{"title":"IMPORTANT","deadline":"2020-01-01","idea":"Be a better programmer!","plan":"Commit to github everyday!"}}'

    }
}

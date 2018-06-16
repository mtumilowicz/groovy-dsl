package dsl.json

import dsl.DeadlineMemo
import dsl.ToDo
import spock.lang.Specification

import java.time.LocalDate
/**
 * Created by mtumilowicz on 2018-06-08.
 */
class DeadlineMemoJsonConverterTest extends Specification {
    def "test json"() {
        given:
        def toDo = [new ToDo(title: 'idea', body: 'Be a better programmer!'), 
                    new ToDo(title: 'plan', body: 'Commit to github everyday!')]
        def memo = new DeadlineMemo(title: 'IMPORTANT',
                deadline: LocalDate.parse('2020-01-01'),
                toDo: toDo)

        when:
        def json = DeadlineMemoJsonConverter.json(memo)

        then:
        json == '{"memo":{"title":"IMPORTANT","deadline":"2020-01-01","idea":"Be a better programmer!","plan":"Commit to github everyday!"}}'

    }
}

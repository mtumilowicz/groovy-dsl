package dsl.text

import dsl.DeadlineMemo
import dsl.ToDo
import spock.lang.Specification

import java.time.LocalDate
/**
 * Created by mtumilowicz on 2018-06-09.
 */
class DeadlineMemoTextConverterTest extends Specification {
    def "test text"() {
        given:
        def toDo = [new ToDo(title: 'idea', body: 'Be a better programmer!'),
                    new ToDo(title: 'plan', body: 'Commit to github everyday!')]
        def memo = new DeadlineMemo(title: 'IMPORTANT',
                deadline: LocalDate.parse('2020-01-01'),
                toDo: toDo)

        and:
        String control =
                "Memo\n" +
                        "Title: IMPORTANT\n" +
                        "Deadline: 2020-01-01\n" +
                        "idea: Be a better programmer!\n" +
                        "plan: Commit to github everyday!\n"

        expect:
        control == DeadlineMemoTextConverter.text(memo)
    }
}

package dsl.xml

import dsl.DeadlineMemo
import groovy.xml.MarkupBuilder

/**
 * Created by mtumilowicz on 2018-06-08.
 */
class DeadlineMemoXmlConverter {
     static def xml(DeadlineMemo memo) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.memo() {
            title(memo.title)
            deadline(memo.deadline)
            memo.toDo.forEach { "$it.title"(it.body) }
        }
        writer
    }
}

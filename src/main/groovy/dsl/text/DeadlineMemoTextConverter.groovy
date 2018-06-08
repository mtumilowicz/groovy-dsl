package dsl.text

import dsl.DeadlineMemo

/**
 * Created by mtumilowicz on 2018-06-08.
 */
class DeadlineMemoTextConverter {
    static String text(DeadlineMemo memo) {
        String template = "Memo\nTitle: $memo.title\nDeadline: $memo.deadline\n"
        def sectionStrings = ""
        memo.toDo.forEach { sectionStrings += "$it.title: $it.body\n" }
        template += sectionStrings
        template
    }
}

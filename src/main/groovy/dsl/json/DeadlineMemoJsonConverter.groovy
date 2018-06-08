package dsl.json

import dsl.DeadlineMemo
import groovy.json.JsonBuilder

/**
 * Created by mtumilowicz on 2018-06-08.
 */
class DeadlineMemoJsonConverter {
    static String json(DeadlineMemo memo) {
        JsonBuilder builder = new JsonBuilder()
        builder.memo() {
            title memo.title
            deadline "$memo.deadline"
            memo.toDo.forEach { "$it.title" "$it.body" }
        }

        builder
    }
}

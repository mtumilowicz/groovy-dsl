import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by mtumilowicz on 2018-06-07.
 */
class DeadlineMemo {

    String title
    LocalDate deadline
    def toDo = []

    def static make(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DeadlineMemo) Closure closure) {
        def code = closure.rehydrate(new DeadlineMemo(), this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

    def title(String toText) {
        this.title = toText
    }

    def deadline(String text) {
        this.deadline = LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
    }

    def methodMissing(String methodName, args) {
        def section = new Section(title: methodName, body: args[0])
        toDo << section
    }

    String getXml() {
        doXml(this)
    }

    String getText() {
        doText(this)
    }

    String getJson() {
        doJson(this)
    }

    private static String doJson(DeadlineMemo memo) {
        JsonBuilder builder = new JsonBuilder()
        builder.memo() {
            title memo.title
            deadline "$memo.deadline"
            memo.toDo.forEach { "$it.title" "$it.body" }
        }

        builder.toString()
    }

     private static String doXml(DeadlineMemo memo) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.memo() {
            title(memo.title)
            deadline(memo.deadline)
            memo.toDo.forEach { "$it.title"(it.body) }
        } 
         writer.toString()
    }

    private static String doText(DeadlineMemo memo) {
        String template = "Memo\nTitle: $memo.title\nDeadline: $memo.deadline\n"
        def sectionStrings = ""
        memo.toDo.forEach { sectionStrings += "$it.title: $it.body\n" }
        template += sectionStrings
        template
    }
}
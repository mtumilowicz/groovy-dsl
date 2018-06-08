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

    def getXml() {
        doXml(this)
    }

    def getHtml() {
        doHtml(this)
    }

    def getText() {
        doText(this)
    }

    def getJson() {
        doJson(this)
    }

    private static doJson(DeadlineMemo memo) {
        JsonBuilder builder = new JsonBuilder()
        builder.memo() {
            title memo.title
            deadline "$memo.deadline"
            memo.toDo.forEach { "$it.title" "$it.body" }
        }

        println builder
    }

    private static doXml(DeadlineMemo memo) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.memo() {
            title(memo.title)
            deadline(memo.deadline)
            memo.toDo.forEach { "$it.title"(it.body) }
        }
        println writer
    }

    private static doHtml(DeadlineMemo memo) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.html() {
            head {
                title("Memo")
            }
            body {
                h1("Memo")
                h3("To: $memo.title")
                h3("Deadline: $memo.deadline")
                memo.toDo.forEach {
                    section ->
                        p {
                            b(section.title)
                            p(section.body)
                        }
                }
            }
        }
        println writer
    }

    private static doText(DeadlineMemo memo) {
        String template = "Memo\nTo: $memo.title\nDeadline: $memo.deadline \n"
        def sectionStrings = ""
        memo.toDo.forEach { sectionStrings += "$it.title: $it.body \n" }
        template += sectionStrings
        println template
    }
}
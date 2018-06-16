package dsl

import dsl.json.DeadlineMemoJsonConverter
import dsl.text.DeadlineMemoTextConverter
import dsl.xml.DeadlineMemoXmlConverter
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * Created by mtumilowicz on 2018-06-07.
 */
@EqualsAndHashCode
@ToString
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
        def section = new ToDo(title: methodName, body: args[0])
        toDo << section
    }

    String getXml() {
        DeadlineMemoXmlConverter.xml(this)
    }

    String getText() {
        DeadlineMemoTextConverter.text(this)
    }

    String getJson() {
        DeadlineMemoJsonConverter.json(this)
    }
}
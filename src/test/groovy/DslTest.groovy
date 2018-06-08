import spock.lang.Specification


/**
 * Created by mtumilowicz on 2018-06-07.
 */
class DslTest extends Specification {
    
    static final String titleText = "IMPORTANT"
    static final String deadlineText = "2020-01-01"
    static final String ideaText = "Be a better programmer!"
    static final String planText = "Commit to github everyday!"
    
    def "testDslUsage_outputXml"() {
        given:
        DeadlineMemo.make {
            title titleText
            deadline deadlineText
            idea ideaText
            plan planText
            xml
        }
    }

    def "testDslUsage_outputHtml"() {
        given:
        DeadlineMemo.make {
            title titleText
            deadline deadlineText
            idea ideaText
            plan planText
            html
        }
    }

    def "testDslUsage_outputText"() {
        given:
        DeadlineMemo.make {
            title titleText
            deadline deadlineText
            idea ideaText
            plan planText
            text
        }
    }

    def "testDslUsage_outputJson"() {
        given:
        DeadlineMemo.make {
            title titleText
            deadline deadlineText
            idea ideaText
            plan planText
            json
        }
    }
}
import spock.lang.Specification


/**
 * Created by mtumilowicz on 2018-06-07.
 */
class DslTest extends Specification {
    
    static final String titleText = "IMPORTANT"
    static final String deadlineText = "2020-01-01"
    static final String ideaText = "Be a better programmer!"
    static final String planText = "Commit to github everyday!"
    
    def "xml"() {
        given:
        DeadlineMemo.make {
            title titleText
            deadline deadlineText
            idea ideaText
            plan planText
            xml
        }
    }

    def "html"() {
        given:
        DeadlineMemo.make {
            title titleText
            deadline deadlineText
            idea ideaText
            plan planText
            html
        }
    }

    def "text"() {
        given:
        DeadlineMemo.make {
            title titleText
            deadline deadlineText
            idea ideaText
            plan planText
            text
        }
    }

    def "json"() {
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
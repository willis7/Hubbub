import com.grailsinaction.*
import spock.lang.Specification

/**
 * Created by willis7 on 04/01/15.
 */
@TestFor(UrlMappings)
@Mock(PostController)
class UrlMappingsSpec extends Specification {

    def "Ensure basic mapping operations for user permalink"() {
        expect:
        assertForwardUrlMapping(url, controller: expectCtrl, action: expectAction) {
            id = expectId
        }

        where:
        url                      | expectCtrl | expectAction | expectId
        '/users/glen'            | 'post'     | 'timeline'   | 'glen'
        '/timeline/chuck_norris' | 'post'     | 'timeline'   | 'chuck_norris'
    }
}

package pl.allegro.community.lucene

import spock.lang.Shared
import spock.lang.Specification

class IndexerTest extends Specification {
    @Shared
    def indexLocation = new File("/tmp/lucene")
    @Shared
    Indexer indexer

    def setupSpec() {
        indexer = new Indexer(indexLocation)
    }

    def "should digest document"() {
        when:
        def count = indexer.index(["title": "twitter"])
        then:
        1 == count
    }

    def cleanupSpec() {
        indexer?.close()
        indexLocation?.deleteDir()
    }
}

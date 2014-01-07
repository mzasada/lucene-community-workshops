package pl.allegro.community.lucene

import spock.lang.Shared
import spock.lang.Specification

class LuceneBasicsTest extends Specification {

    @Shared
    def indexLocation = new File("/tmp/lucene")

    def "should digest documents and search for them"() {
        given:
        def indexer = new Indexer(indexLocation)

        indexer.index([
                (SchemaConstants.FIELD_NAME_CONTENT): "The quick brown fox jumps over the lazy dog",
                (SchemaConstants.FIELD_NAME_ID): "DOC_1"])
        indexer.index([
                (SchemaConstants.FIELD_NAME_CONTENT): "Lucene in action!",
                (SchemaConstants.FIELD_NAME_ID): "DOC_2"])

        when:
        def searcher = new Searcher(indexLocation)
        def hits = searcher.search("+quick")

        then:
        indexer.countIndexedDocuments() == 2
        hits.size() == 1
        hits[0].get(SchemaConstants.FIELD_NAME_ID) == "DOC_1"
    }

    def cleanupSpec() {
        indexLocation?.deleteDir()
    }
}

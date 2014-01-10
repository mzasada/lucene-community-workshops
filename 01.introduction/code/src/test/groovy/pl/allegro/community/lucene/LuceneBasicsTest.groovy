package pl.allegro.community.lucene

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.util.Version
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
        def hits = searcher.search("quick dog")

        then:
        indexer.countIndexedDocuments() == 2
        hits.size() == 1
        hits[0].get(SchemaConstants.FIELD_NAME_ID) == "DOC_1"
    }

    def "should analyze text with StandardAnalyzer"() {
        given:
        def input = "The Quick brown fox jumps over the lazy dog"
        def analyzer = new StandardAnalyzer(Version.LUCENE_46)

        when:
        def terms = listOfTerms analyzer.tokenStream(SchemaConstants.FIELD_NAME_CONTENT, input)

        then:
        println "$input -> $terms"
    }

    def "should parse same input using different query parser"() {
        given:
        def input = "+quick -fox"

        expect:
        def query = queryParser.parse(input)
        println query

        where:
        queryParser << [
                new QueryParser(
                        Version.LUCENE_46,
                        SchemaConstants.FIELD_NAME_CONTENT,
                        new StandardAnalyzer(Version.LUCENE_46)),
                new MultiFieldQueryParser(Version.LUCENE_46,
                        [SchemaConstants.FIELD_NAME_CONTENT, SchemaConstants.FIELD_NAME_ID].toArray() as String[],
                        new StandardAnalyzer(Version.LUCENE_46))
        ]
    }

    def listOfTerms(def tokenStream) {
        def result = []
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            result << charTermAttribute.toString();
        }
        result
    }

    def cleanupSpec() {
        indexLocation?.deleteDir()
    }
}

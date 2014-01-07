package pl.allegro.community.lucene

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version

import static java.lang.System.currentTimeMillis

class Searcher {

    IndexSearcher indexSearcher

    QueryParser queryParser

    Searcher(File fileLocation) {
        def directory = FSDirectory.open(fileLocation)
        indexSearcher = new IndexSearcher(DirectoryReader.open(directory))
        queryParser = new QueryParser(
                Version.LUCENE_46,
                SchemaConstants.FIELD_NAME_CONTENT,
                new StandardAnalyzer(Version.LUCENE_46))
    }

    List<Document> search(String phrase) {
        Query query = queryParser.parse(phrase)
        def start = currentTimeMillis()
        TopDocs hits = indexSearcher.search(query, 10)
        def duration = currentTimeMillis() - start
        println("Found $hits.totalHits document(s) in $duration ms for query $phrase")

        hits.scoreDocs.collect { indexSearcher.doc(it.doc) }
    }
}

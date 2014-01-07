package pl.allegro.community.lucene

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version

class Indexer implements Closeable {
    IndexWriter indexWriter

    Indexer(File indexLocation) {
        def directory = FSDirectory.open(indexLocation)
        def indexWriterConfig = new IndexWriterConfig(Version.LUCENE_46, new StandardAnalyzer(Version.LUCENE_46))
        this.indexWriter = new IndexWriter(directory, indexWriterConfig)
    }

    void index(Map<String, Object> fields) {
        def document = prepareDocument(fields)
        indexWriter.addDocument(document)
        indexWriter.commit()
    }

    int countIndexedDocuments() {
        indexWriter.numDocs()
    }

    private Document prepareDocument(Map<String, Object> fields) {
        new Document().with {
            fields.each {
                key, value -> add(new Field(key, value, TextField.TYPE_STORED))
            }
            it
        }
    }

    @Override
    void close() throws IOException {
        indexWriter?.close()
    }
}

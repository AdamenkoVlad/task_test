import org.example.DocumentManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

public class DocumentManagerTest {

    @Test
    public void testSave() {
        DocumentManager testDocument = new DocumentManager();

        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Document Title")
                .content("Document Content")
                .author(DocumentManager.Author.builder().id("1").name("Tom").build())
                .build();

        DocumentManager.Document savedDocument = testDocument.save(document);


        assertNotNull(savedDocument.getId());
        assertNotNull(savedDocument.getCreated());
    }

    @Test
    public void testFindById() {
        DocumentManager testDocument = new DocumentManager();

        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Document Title")
                .content("Document Content")
                .author(DocumentManager.Author.builder().id("1").name("Tom").build())
                .build();

        DocumentManager.Document savedDocument = testDocument.save(document);
        assertTrue(testDocument.findById(savedDocument.getId()).isPresent());
    }

    @Test
    public void testSearch() {
        DocumentManager testDocument = new DocumentManager();

        // Створюємо документи
        DocumentManager.Document document1 = DocumentManager.Document.builder()
                .title("Document Title 1")
                .content("Some content about company")
                .author(DocumentManager.Author.builder().id("1").name("Anna 1").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document document2 = DocumentManager.Document.builder()
                .title("Document Title 2")
                .content("Other content about factory")
                .author(DocumentManager.Author.builder().id("2").name("Fred 2").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document document3 = DocumentManager.Document.builder()
                .title("Document Title 3")
                .content("More content about enterprise")
                .author(DocumentManager.Author.builder().id("3").name("Alice 3").build())
                .created(Instant.now())
                .build();

        testDocument.save(document1);
        testDocument.save(document2);
        testDocument.save(document3);

        // Тест пошуку за префіксами заголовка
        DocumentManager.SearchRequest requestByTitle = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Document Title"))
                .build();
        List<DocumentManager.Document> resultsByTitle = testDocument.search(requestByTitle);
        assertEquals(3, resultsByTitle.size());

        // Тест пошуку за вмістом
        DocumentManager.SearchRequest requestByContent = DocumentManager.SearchRequest.builder()
                .containsContents(List.of("company"))
                .build();
        List<DocumentManager.Document> resultsByContent = testDocument.search(requestByContent);
        assertEquals(1, resultsByContent.size());

        // Тест пошуку за автором
        DocumentManager.SearchRequest requestByAuthor = DocumentManager.SearchRequest.builder()
                .authorIds(List.of("2"))
                .build();
        List<DocumentManager.Document> resultsByAuthor = testDocument.search(requestByAuthor);
        assertEquals(1, resultsByAuthor.size());

        // Тест пошуку за кількома умовами (заголовок + автор)
        DocumentManager.SearchRequest requestByTitleAndAuthor = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Document Title"))
                .authorIds(List.of("2"))
                .build();
        List<DocumentManager.Document> resultsByTitleAndAuthor = testDocument.search(requestByTitleAndAuthor);
        assertEquals(1, resultsByTitleAndAuthor.size());


    }
}

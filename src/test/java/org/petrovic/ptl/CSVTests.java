package org.petrovic.ptl;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.carpediem.CSVLexer;
import org.carpediem.CSVParser;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Test-Driven Development with ANTLR.
 *
 *A simplified version https://theantlrguy.atlassian.net/wiki/plugins/servlet/mobile?contentId=2687102#content/view/2687378
 * The grammar come from https://github.com/antlr/grammars-v4/blob/master/csv/CSV.g4
 * It has been a little modified to support the space remove test.
 */
public class CSVTests {

    private CSVParser createParser(CSVLexer lexer) throws IOException {
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSVParser parser = new CSVParser(tokens);
        return parser;
    }

    private CSVLexer createLexer(String testString) throws IOException {
        CharStream stream = new ANTLRInputStream(testString);
        CSVLexer lexer = new CSVLexer(stream);
        return lexer;
    }

    private CSVParser createParser(String testString) throws IOException {
        CSVLexer lexer = createLexer(testString);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSVParser parser = new CSVParser(tokens);
        return parser;
    }

    private String parseField(String testString) throws IOException, RecognitionException {
        CSVParser parser = createParser(testString + "\n");

        String result = "";
        CSVParser.RowContext context = parser.row();
        for (CSVParser.FieldContext fieldContext : context.field()) {
            result += fieldContext.getText();
        }
        return result;
    }

    @Test
    public void testNewline() throws IOException, RecognitionException {
        CSVParser parser = createParser("\n");
        parser.row();
    }

    @Test
    public void testCRLF() throws IOException, RecognitionException {
        CSVParser parser = createParser("\r\n");
        parser.row();
    }

    @Test
    public void testSingleWord() throws IOException, RecognitionException {
        String result = parseField("Red");
        assertEquals("Expected Red", "Red", result);
    }

    @Test
    public void testMultipleWords() throws IOException, RecognitionException {
        CSVParser parser = createParser("Red,Green,,Blue\n");
        CSVParser.RowContext context = parser.row();
        List<CSVParser.FieldContext> fieldContexts = context.field();
        assertEquals("Expected 4 items", 4, context.field().size());
        assertEquals("Expected Red", "Red", fieldContexts.get(0).getText());
        assertEquals("Expected Green", "Green", fieldContexts.get(1).getText());
        assertEquals("Expected \"\"", "", fieldContexts.get(2).getText());
        assertEquals("Expected Blue", "Blue", fieldContexts.get(3).getText());
    }

    @Test
    public void testSpaceRemoval() throws IOException, RecognitionException {
        CSVLexer lexer = createLexer("Red  ,   Green, ,Blue\n");
        CSVParser parser = createParser(lexer);
        CSVParser.RowContext context = parser.row();
        List<CSVParser.FieldContext> fieldContexts = context.field();
        assertEquals("Expected 4 items", 4, context.field().size());
        assertEquals("Expected 'Red'", "Red", fieldContexts.get(0).getText());
        assertEquals("Expected 'Green'", "Green", fieldContexts.get(1).getText());
        assertEquals("Expected \"\"", "", fieldContexts.get(2).getText());
        assertEquals("Expected Blue", "Blue", fieldContexts.get(3).getText());
    }
}

package dp.module2.lab1.parsing;

import dp.module2.lab1.Main;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParseErrorHandler implements ErrorHandler {
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        Main.log("Line " + exception.getLineNumber() + " : " + exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        Main.log("Line " + exception.getLineNumber() + " : " + exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        Main.log("Line " + exception.getLineNumber() + " : " + exception.getMessage());
        exception.printStackTrace();
    }
}

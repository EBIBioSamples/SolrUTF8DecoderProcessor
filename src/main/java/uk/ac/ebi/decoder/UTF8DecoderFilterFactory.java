package uk.ac.ebi.decoder;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

public class UTF8DecoderFilterFactory extends TokenFilterFactory {


    public UTF8DecoderFilterFactory(Map<String, String> args) {
        super(args);
    }

    public UTF8DecoderFilter create(TokenStream input) {
        return new UTF8DecoderFilter(input);
    }
}

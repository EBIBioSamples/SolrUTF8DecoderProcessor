package uk.ac.ebi.decoder;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

final class UTF8DecoderFilter extends TokenFilter {
    private final CharTermAttribute charTermAttribute;

    UTF8DecoderFilter(TokenStream in) {
        super(in);
        this.charTermAttribute = this.addAttribute(CharTermAttribute.class);
    }

    public boolean incrementToken() throws IOException {
        if (this.input.incrementToken()) {
            CharSequence out = UTF8Utils.detectDecodeUTF8Sequences(this.charTermAttribute.toString());
            this.charTermAttribute.setEmpty();
            this.charTermAttribute.append(out);
            return true;
        }
        else {
            return false;
        }

    }

}

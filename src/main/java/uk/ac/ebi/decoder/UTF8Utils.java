package uk.ac.ebi.decoder;

class UTF8Utils {

    private static char ILLEGAL_CHAR_REPRESENATION = 0xfffd;

    static String detectDecodeUTF8Sequences(String in) {
        if (null == in) {
            return null;
        }

        StringBuilder sb = new StringBuilder(in.length() * 2);

        char ch, decoded;
        for (int ix = 0; ix < in.length(); ++ix) {
            ch = in.charAt(ix);

            if (ch <= 0x7f) {
                // there are no problems with ascii
                sb.append(ch);
            }
            else if (ch >= 0xc2 && ch <= 0xdf && (ix + 1) < in.length()) {
                // this is possibly a start of two-byte utf-8 sequence
                // let's try to decode it and if it's any good, use it
                // or just copy the original sequence
                decoded = decodeUTF8((byte) ch, (byte) in.charAt(ix + 1));
                if (ILLEGAL_CHAR_REPRESENATION == decoded) {
                    sb.append(ch);
                }
                else {
                    sb.append(decoded);
                    ix++;
                }
            }
            else if (ch >= 0xe0 && ch <= 0xef && (ix + 2) < in.length()) {
                // this is possibly a start of three-byte utf-8 sequence
                // let's try to decode it and if it's any good, use it
                // or just copy the original sequence
                decoded = decodeUTF8((byte) ch, (byte) in.charAt(ix + 1),
                        (byte) in.charAt(ix + 2));
                if (ILLEGAL_CHAR_REPRESENATION == decoded) {
                    sb.append(ch);
                }
                else {
                    sb.append(decoded);
                    ix += 2;
                }
            }
            else {
                // the rest is interpreted as pure unicode (we don't decode four-byte utf-8's)
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static char decodeUTF8(byte b0, byte b1) {
        if ((0xc0 == (b0 & 0xe0)) && (0x80 == (b1 & 0xc0))) {
            return (char) (((b0 & 0x1f) << 6) + (b1 & 0x3f));
        }
        else {
            return ILLEGAL_CHAR_REPRESENATION;
        }
    }

    private static char decodeUTF8(byte b0, byte b1, byte b2) {
        if ((0xe0 == (b0 & 0xf0)) && (0x80 == (b1 & 0xc0)) &&
                (0x80 == (b2 & 0xc0))) {
            return (char) (((b0 & 0x0f) << 12) + ((b1 & 0x3f) << 6) + (b2 & 0x3f));
        }
        else {
            return ILLEGAL_CHAR_REPRESENATION;
        }
    }
}

package com.imooc.utils;

//import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.zip.InflaterInputStream;

/**
 * Created by yang.gao on 2017/1/18.
 */
public class HttpGzipEntityUtils {

    private HttpGzipEntityUtils() {
    }

    public static void consumeQuietly(HttpEntity entity) {
        try {
            consume(entity);
        } catch (IOException var2) {
            ;
        }

    }

    public static void consume(HttpEntity entity) throws IOException {
        if(entity != null) {
            if(entity.isStreaming()) {
                InputStream instream = entity.getContent();
                if(instream != null) {
                    instream.close();
                }
            }

        }
    }

    public static void updateEntity(HttpResponse response, HttpEntity entity) throws IOException {
        Args.notNull(response, "Response");
        consume(response.getEntity());
        response.setEntity(entity);
    }

    public static byte[] toByteArray(HttpEntity entity) throws IOException {
        Args.notNull(entity, "Entity");
        InputStream instream = entity.getContent();
        if(instream == null) {
            return null;
        } else {
            try {
                Args.check(entity.getContentLength() <= 2147483647L, "HTTP entity too large to be buffered in memory");
                int i = (int)entity.getContentLength();
                if(i < 0) {
                    i = 4096;
                }

                ByteArrayBuffer buffer = new ByteArrayBuffer(i);
                byte[] tmp = new byte[4096];

                int l;
                while((l = instream.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }

                byte[] var6 = buffer.toByteArray();
                return var6;
            } finally {
                instream.close();
            }
        }
    }

    /** @deprecated */
    @Deprecated
    public static String getContentCharSet(HttpEntity entity) throws ParseException {
        Args.notNull(entity, "Entity");
        String charset = null;
        if(entity.getContentType() != null) {
            HeaderElement[] values = entity.getContentType().getElements();
            if(values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if(param != null) {
                    charset = param.getValue();
                }
            }
        }

        return charset;
    }

    /** @deprecated */
    @Deprecated
    public static String getContentMimeType(HttpEntity entity) throws ParseException {
        Args.notNull(entity, "Entity");
        String mimeType = null;
        if(entity.getContentType() != null) {
            HeaderElement[] values = entity.getContentType().getElements();
            if(values.length > 0) {
                mimeType = values[0].getName();
            }
        }

        return mimeType;
    }

    public static String toString(HttpEntity entity, Charset defaultCharset) throws IOException, ParseException {
        Args.notNull(entity, "Entity");
        InputStream instream = entity.getContent();
        if(instream == null) {
            return null;
        } else {
            try {
                Args.check(entity.getContentLength() <= 2147483647L, "HTTP entity too large to be buffered in memory");
                int i = (int)entity.getContentLength();
                if(i < 0) {
                    i = 4096;
                }

                Charset charset = null;

                try {
                    ContentType reader = ContentType.get(entity);
                    if(reader != null) {
                        charset = reader.getCharset();
                    }
                } catch (UnsupportedCharsetException var13) {
                    if(defaultCharset == null) {
                        throw new UnsupportedEncodingException(var13.getMessage());
                    }
                }

                if(charset == null) {
                    charset = defaultCharset;
                }

                if(charset == null) {
                    charset = HTTP.DEF_CONTENT_CHARSET;
                }

//                InputStreamReader reader1 = new InputStreamReader(instream, charset);
                BufferedInputStream bis = new BufferedInputStream(instream);
//                GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bis);
                InflaterInputStream gzIn = new InflaterInputStream(bis);

//                CharArrayBuffer buffer = new CharArrayBuffer(i);
                ByteArrayOutputStream buffer=new ByteArrayOutputStream();
                byte[] tmp = new byte[1024*1024*20];

                int l = 0;
                while((l = gzIn.read(tmp)) != -1) {
                    buffer.write(tmp, 0, l);
                }

                byte[] bytes = buffer.toByteArray();
                String s = new String(bytes);

                String var9 = buffer.toString();
                return var9;


                /* InputStreamReader reader1 = new InputStreamReader(instream, charset);
                CharArrayBuffer buffer = new CharArrayBuffer(i);
                char[] tmp = new char[1024];

                int l;
                while((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }

                String var9 = buffer.toString();
                return var9;*/
            } finally {
                instream.close();
            }
        }
    }

    public static String toString(HttpEntity entity, String defaultCharset) throws IOException, ParseException {
        return toString(entity, defaultCharset != null?Charset.forName(defaultCharset):null);
    }

    public static String toString(HttpEntity entity) throws IOException, ParseException {
        return toString(entity, (Charset)null);
    }
}

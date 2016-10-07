package uk.ac.ebi.decoder;

import org.apache.commons.lang.CharUtils;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessor.FieldNameSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UTF8DecodeUpdateProcessorFactory extends FieldMutatingUpdateProcessorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(UTF8DecodeUpdateProcessorFactory.class);

    public void init(NamedList args) { super.init(args); }

    public FieldNameSelector getDefaultSelector(SolrCore core) {
        return FieldMutatingUpdateProcessor.SELECT_NO_FIELDS;
    }

    public UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) {
        return new UTF8DecodeUpdateProcessor(this.getSelector(), next);
    }

    private class UTF8DecodeUpdateProcessor extends FieldValueMutatingUpdateProcessor {
        UTF8DecodeUpdateProcessor(FieldNameSelector selector, UpdateRequestProcessor next) {
            super(selector, next);
        }

        protected Object mutateValue(Object src) {
            if (src instanceof CharSequence) {
                CharSequence value = UTF8Utils.detectDecodeUTF8Sequences(((String) src).trim());
                LOGGER.debug(String.format("Applying UTF8 decode to %s -> %s",src.toString(),value.toString()));
                return value;
            } else {
                return src;
            }
        }
    }
}

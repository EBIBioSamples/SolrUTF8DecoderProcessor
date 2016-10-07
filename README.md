# Readme

## Description
This processor try to fix character encoding problems within specific fields in your solr index.
It's a *processor*, that means is able to change the stored values of documents you're trying to index, so be carefull that if you don't pay attention 
you will not be able to retrieve original content from your fields.

## How to build
Just run:
```
mvn clean package
```
In the `target` folder you will be able to find the `.jar` package that you can then copy wherever you want. 

## How to use the processor

**1:** First of all you need SolR to be aware of the plugin. An easy way to achive this is to create a `lib` folder inside the core that will use such plugin. SolR automatically scan that folder and you dont' need to do much more. Another solution could be to put the processor in a another folder and update the `solrconfig.xml` file with a new instruction similar to this  
```xml
	 <lib dir="path/to/processor/folder" regex=".*\.jar" />
	 <!-- or even defining a specific path
 		<lib path="path/to/processor/the-jar-package-with-processor.jar" />
 	-->
 ```
**2:** You need to update your `solrconfig.xml` file with the instructions for the processor. Here an example
```xml
<!-- You need to define an updateRequestProcessorChain in order to make this work -->
 <updateRequestProcessorChain name="UTF8">
    <processor class="uk.ac.ebi.decoder.UTF8DecodeUpdateProcessorFactory">
      <str name="fieldName">test_utf8</str>
    </processor>
    <processor class="solr.LogUpdateProcessorFactory" />
    <processor class="solr.RunUpdateProcessorFactory" />
  </updateRequestProcessorChain>
```
Remember that you can decide which fields the processor will work on using the `<str name="type-of-field">value</str>`. You can use
- fieldName - selecting specific fields by field name lookup
- fieldRegex - selecting specific fields by field name regex match (regexes are checked in the order specified)
- typeName - selecting specific fields by fieldType name lookup
- typeClass - selecting specific fields by fieldType class lookup, including inheritence and interfaces

**3:** You need than to attach the processor chain to the `/update` request handler, i.e:
```xml
<requestHandler name="/update" class="solr.UpdateRequestHandler">
    <lst name="defaults">
      <str name="update.chain">UTF8</str>
    </lst>
  </requestHandler>
```

## Start indexing
Everything is in place, when you index a document with a field `test_utf8` such field will be processed by the UTF8DecoderProcessor and the result stored in the field.

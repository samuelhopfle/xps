# Library
## What am I
XPS is a library to convert XML to HTML or PDF documents via XSLT 2.0 processing. It uses Templates on the server or on a network drive 
to convert XML content delivered as a String value and takes into account the locale given.

XPS uses the following 3rd party libraries. For license information and other stuff see their website.
- Apache Fop Library (https://xmlgraphics.apache.org/fop/)
- Saxon HE XSLT Processor (https://www.saxonica.com/products/products.xml)

## Usage
To convert files use the following classes:
 
- XML to HTML: <code>Xml2HtmlConverter</code>
- XML to PDF: <code>Xml2PdfConverter</code>

To convert the file, use the following method. It returns the document as a byte array.

```
public interface IXsltConverter {   
    /**   
     * creates a document of any type (ByteArray can be a PDF, HTML or whatever) based on XML, an XSLT stylesheet and a locale   
     *
     * @param xml              XML content
     * @param templateFileName file name of the XSLT stylesheet
     * @param locale           locale for presentation and translation of the document
     * @return ByteArray with the document created
     */
    byte[] convertXml(final String xml, final String templateFileName, final String locale);
    }
```

 # Added Functionality
 ## XSLT Expansions
 The XSLT functionality was slightly enhanced by the following options:
 
 - the collection finder can handle UNC network paths
 - the library can localize dates not only by XSLT but also by java
    - for an example call ```doc(concat('date/long`, current-date())``` to get a Java long style formatted current date
    - formats available:
        - ```/date/short/``` leads to Java ```DateFormat.SHORT```
        - ```/date/long/``` leads to Java ```DateFormat.LONG```
        - ```/date/medium/``` leads to Java ```DateFormat.MEDIUM```
        - ```/date/month/``` leads to Java Simple Date with Format "MMMM"
        - ```/date/medium_month/``` leads to Java Simple Date with Format "MMM"
 - language (i18n) used can be queried
    - call ```doc('language)``` 
    
## Date Format Correction
This library has some date format corrections that were issued by a client. It's in fact the case, that some french 
and italian terms in certain cases are not correct in certain Java versions.

# License
Copyright © 2020 LESS Informatik AG

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
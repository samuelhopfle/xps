# XPS #
## What am I? ##
XPS is a library with a set of restfull http microservices to convert XML to HTML or PDF documents via XSLT 2.0 processing. It uses Templates on the server or on a network drive 
to convert XML content delivered as a String value and takes into account the locale given. It can be used as followed:

- directly in the code (see ```./xps-lib/README.md```)
- as a set of restful microservices

XPS uses the following 3rd party libraries. For license information and other stuff see their website.
- Apache Fop Library (https://xmlgraphics.apache.org/fop/)
- Saxon HE XSLT Processor (https://www.saxonica.com/products/products.xml)

*Java Version used is 11 (or higher). This container runs on Apache Tomcat Servlet Container 9.0 (or higher).*

## Examples
Example templates can be found in the path:
```./src/main/webapp/templates``` (```.xslt``` for HTML, ```.fo``` for PDF)

The related curl calls can be found in the file:
```./example_calls.txt```

# Services
|funcionality|restful interface|data|
|--------------|---------|---|
|XML to PDF|http://servername:port/pdf/{locale}?template=*[absolute_or_relative_unc_path]*|xml content in POST|
|XML to HTML|http://servername:port/html/{locale}?template=*[absolute_or_relative_unc_path]*|xml content in POST|
|XML to XML|http://servername:port/xml/{locale}|xml content in POST|
|information page|http://servername:port/||

## Run & deploy
* run with gradle ```bootRun```   
* deploy with gradle ```build```

# Functionality
## Basics
- print service for template based converting of
	- XML to PDF
	- XML to HTML
	- XML to XML
- document templates with XSLT 2.0 standard
- webservice with a restful HTTP protocol
    - XML-data via POST
    - file access via UNC network access

## Features supported
- include graphics or other documents
- i18n (internationalization) and translation
    - i18n formatting via XSLT
    - translations via XSLT
    - date can be formatted with java 18n
- SVG is supported

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
    - call ```doc('language)
    
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
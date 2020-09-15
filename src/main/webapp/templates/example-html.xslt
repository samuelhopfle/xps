<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <!-- language, needed for the catalog  -->
    <xsl:variable name="language" select="doc('language')"/>

    <!-- include translations -->
    <xsl:variable name="includes" select="'./i18n?select=global_*.xml'"/>
    <xsl:variable name="translations" select="collection($includes)/catalogue[@xml:lang=$language]"/>
    <xsl:key name="i18n" match="message" use="@key"/>

    <!-- match example -->
    <xsl:template match="example">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
                <title>
                    <xsl:value-of select="key('i18n', 'Title', $translations)"/>
                </title>
                <link rel="stylesheet" type="text/css" href="../css/xps.css"/>
                <style>
                    table { margin-bottom: 2em; }
                    td { padding-left: 1em; padding-right: 1em; }
                </style>
            </head>
            <body>
                <h1>
                    <xsl:value-of select="key('i18n', 'Title', $translations)"/>
                </h1>
                <table>
                    <tr>
                        <td>
                            <xsl:value-of select="key('i18n','GetLanguage',$translations)"/>
                        </td>
                        <td>
                            <xsl:value-of select="$language"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <xsl:value-of select="key('i18n','FormatADateWithJava',$translations)"/>
                        </td>
                        <td>
                            <xsl:value-of select="doc(concat('date/long/', myDate))"/>
                        </td>
                    </tr>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
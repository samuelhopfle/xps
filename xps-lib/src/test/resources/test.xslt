<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="doc">
        <html>
            <head/>
            <body>
                <h1>
                    <xsl:value-of select="field"/>
                </h1>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

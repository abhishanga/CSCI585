<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
    <font face="Arial">
    <div id="empInfo">
      <div style="font-size: 24px; background-color: yellow; font-weight: bold; color: red">
        Employee Information
      </div>
      <div style="font-size: 12px">

        <xsl:for-each select="Company/Employee">
          <xsl:variable name="this" select="." />
          <p>
            Employee <span style="font-weight:bold"><xsl:value-of select="$this/empName" /></span>
            works from <span style="font-weight:bold"><xsl:value-of select="$this/empOffice" /></span> office.

            <span style="font-weight:bold"><xsl:value-of select="$this/empName"/></span> works for
            <xsl:variable name="numDivisions" select="count(//WorksFor[empId=$this/empId])" />
            <span style="font-weight:bold"><xsl:value-of select="$numDivisions"/></span> division(s), which are

            <xsl:for-each select="//WorksFor[empId=$this/empId]/divId">
              <xsl:variable name="divId" select="." />
              <xsl:variable name="divName" select="//Division[divId=$divId]/divName" />
              <xsl:if test="$numDivisions = 2">
                 <xsl:if test="position() = 1">
                  <span style="font-weight:bold"><xsl:copy-of select="$divName" /></span>
                </xsl:if>
                 <xsl:if test="position() = 2">
                  <span style="font-weight:bold"> and <xsl:copy-of select="$divName" /></span>.
                </xsl:if>
              </xsl:if>
              <xsl:if test="$numDivisions != 2">
                <xsl:if test="position() != last()">
                  <span style="font-weight:bold"><xsl:copy-of select="$divName" />, </span>
                </xsl:if>
                <xsl:if test="position() = last()">
                  <span style="font-weight:bold">and <xsl:copy-of select="$divName" /></span>.
                </xsl:if>
              </xsl:if>
            </xsl:for-each>

            <span style="font-weight:bold"><xsl:value-of select="$this/empName"/></span> manages 
            <xsl:variable name="numManagedDivisions" select="count(//Division[managerEmpId=$this/empId])" />

            <xsl:if test="$numManagedDivisions = 0">
              <span style="font-weight:bold"><xsl:copy-of select="$numManagedDivisions" /></span> division(s).
            </xsl:if>

            <xsl:if test="$numManagedDivisions = 1">
              <span style="font-weight:bold"><xsl:copy-of select="$numManagedDivisions" /></span> division, which is <span style="font-weight:bold"><xsl:value-of select="//Division[managerEmpId=$this/empId]/divName" /></span>.
            </xsl:if>

            <xsl:if test="$numManagedDivisions &gt; 1">
              <span style="font-weight:bold"><xsl:copy-of select="$numManagedDivisions" /></span> divisions, which are

              <xsl:variable name="managedDivisionNames" select="//Division[managerEmpId=$this/empId]/divName" />
              <xsl:for-each select="$managedDivisionNames">
                <xsl:variable name="mdn" select="." />
                <xsl:if test="$numManagedDivisions = 2">
                   <xsl:if test="position() = 1">
                    <span style="font-weight:bold"><xsl:copy-of select="$mdn" /> </span>
                  </xsl:if>
                   <xsl:if test="position() = 2">
                    <span style="font-weight:bold"> and <xsl:copy-of select="$mdn" /></span>.
                  </xsl:if>
                </xsl:if>
                <xsl:if test="$numManagedDivisions != 2">
                  <xsl:if test="position() != last()">
                    <span style="font-weight:bold"><xsl:copy-of select="$mdn" />, </span>
                  </xsl:if>
                  <xsl:if test="position() = last()">
                    <span style="font-weight:bold">and <xsl:copy-of select="$mdn" /></span>.
                  </xsl:if>  
                </xsl:if>
              </xsl:for-each>
            </xsl:if>
            
            <span style="font-weight:bold"><xsl:value-of select="$this/empName"/></span> works for the most time with the 

            <xsl:variable name="WorksFor" select="//WorksFor[empId=$this/empId]" />
            <xsl:for-each select="$WorksFor">
              <xsl:sort select="./percentTime" order="descending" />
              <xsl:if test="position() = 1">
                <xsl:variable name="maxTimeDivId" select="./divId" />
                <span style="font-weight:bold"><xsl:value-of select="//Division[divId=$maxTimeDivId]/divName" /></span>
              </xsl:if>
            </xsl:for-each>
             division.
          </p>
        </xsl:for-each>
      </div>
    </div>

    <div id="deptInfo">
      <div style="font-size: 24px; background-color: LightGreen; font-weight: bold; color: red">
        Department Information
      </div>
      <div style="font-size: 12px">
        <xsl:for-each select="Company/Department">
          <xsl:variable name="this" select="." />
          <p>
            <span style="font-weight:bold"><xsl:value-of select="$this/deptName" /></span> houses 
            <xsl:variable name="numHousedDept" select="count(//Division[housedDeptId=$this/deptId])" />
            <span style="font-weight:bold"><xsl:value-of select="$numHousedDept" /></span> division(s):

            <xsl:for-each select="//Division[housedDeptId=$this/deptId]/divName">
              <xsl:variable name="hdn" select="." />

              <xsl:if test="$numHousedDept = 2">
                <xsl:if test="position() = 1">
                  <span style="font-weight:bold"><xsl:copy-of select="$hdn" /> </span>
                </xsl:if>
                <xsl:if test="position() = 2">
                  <span style="font-weight:bold"> and <xsl:copy-of select="$hdn" /></span>.
                </xsl:if>
              </xsl:if>

              <xsl:if test="$numHousedDept != 2">
                <xsl:if test="position() != last()">
                    <span style="font-weight:bold"><xsl:copy-of select="$hdn" />, </span>
                  </xsl:if>
                  <xsl:if test="position() = last()">
                    <span style="font-weight:bold"> and <xsl:copy-of select="$hdn" /></span>.
                  </xsl:if>  
              </xsl:if>
            </xsl:for-each>
          </p>
        </xsl:for-each>
      </div>
    </div>

    </font>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>

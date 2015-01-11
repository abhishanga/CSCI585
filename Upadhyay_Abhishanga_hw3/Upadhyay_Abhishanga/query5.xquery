xquery version "1.0";

declare namespace ext="http://www.altova.com/xslt-extensions";

<query5>
{
	let $xml := doc("company.xml")
	
	let $numDivPerEmp :=
		for $empId in $xml/Company/Employee/empId
		return count($xml/Company/WorksFor[empId=$empId])
	
	return 
		<Company>
			<avgNumDivPerEmp> { avg($numDivPerEmp) } </avgNumDivPerEmp>
		</Company>
}
</query5>

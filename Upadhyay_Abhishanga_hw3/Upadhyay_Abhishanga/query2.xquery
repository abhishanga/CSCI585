<query2>
{
	let $xml := doc("company.xml")
	let $numEmployees := count($xml/Company/Employee)
	
	let $divIdForAllEmployees :=
		for $divIds in $xml/Company/Division/divId
		for $row in $xml/Company/WorksFor[divId=$divIds]
		where count($xml/Company/WorksFor[divId=$divIds]) = $numEmployees
		return $divIds
		
	let $distinctDivId := distinct-values($divIdForAllEmployees)
	for $divName in $xml/Company/Division[divId=$distinctDivId]/divName
	return
		<Division>
			<divName> { $divName/text() } </divName>
		</Division>

}
</query2>
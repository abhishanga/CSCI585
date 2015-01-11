xquery version "1.0";

declare namespace ext="http://www.altova.com/xslt-extensions";
<query3>
{
	let $xml := doc("company.xml")
	
	let $PSmithEmpId := $xml/Company/Employee[empName="PSmith"]/empId
	let $PSmithDivs := $xml/Company/Division[managerEmpId=$PSmithEmpId]
	let $PSmithDeptNames :=
		for $PSmithDeptName in $xml/Company/Department[deptId=$PSmithDivs/housedDeptId]/deptName
		return $PSmithDeptName
	
	let $WongEmpId := $xml/Company/Employee[empName="Wong"]/empId
	let $WongDivs := $xml/Company/Division[managerEmpId=$WongEmpId]
	let $WongDeptNames :=
		for $WongDeptName in $xml/Company/Department[deptId=$WongDivs/housedDeptId]/deptName
		return $WongDeptName
	
	for $deptName in $PSmithDeptNames [. != $WongDeptNames]
	return 
		<Department>
			<deptName> { $deptName/text() }  </deptName>
		</Department>	
}
</query3>

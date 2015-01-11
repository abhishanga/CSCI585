xquery version "1.0";
declare namespace ns = "http://www.w3.org/2001/XMLSchema-instance";

<query1>
{
	let $xml := doc("company.xml")
	for $empIds in $xml/Company/Employee[empName="PSmith" or empName="Jack"]/empId
	for $divIds in $xml/Company/WorksFor[empId=$empIds and percentTime >= 50]/divId
	return
		<Department>
		<deptName> { 
			for $housedDeptId in $xml/Company/Division[divId=$divIds]/housedDeptId
			for $deptName in $xml/Company/Department[deptId=$housedDeptId]/deptName
			return $deptName/text()
			} </deptName>
		</Department>
}
</query1>
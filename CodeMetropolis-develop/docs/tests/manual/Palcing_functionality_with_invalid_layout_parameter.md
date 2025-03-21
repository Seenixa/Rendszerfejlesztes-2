# Test placing with invalid layout parameter
## Tags
#component_placing #testFile-format_IXML #category_input #category_error_handling

### Purpose
This test assesses the placing tool's handling of an invalid layout parameter, confirming that it correctly identifies the invalid parameter value and exits without creating an output file.

### Pre-requisites
* Java runtime 22_
* mappingToPlacing.xml exists in the placing-1.5.0.jar's folder and is a valid inputFile (produced by mapping-1.5.0.jar with the cdfFile generated by converter-1.5.0.jar for the input codemetropolis-toolchain-commons.graph)
* command line opened in the folder of placing-1.5.0.jar

### Steps
1. Run:
	```cmd
	java -jar placing-1.5.0.jar -i mappingToPlacing.xml -l NOLAYOUT
	``` 

### Expected result
The tool warns the user that the layout parameter value is invalid and exits normally without creating the output file.
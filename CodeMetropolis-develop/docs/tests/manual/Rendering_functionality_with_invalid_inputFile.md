# Test rendering with invalid inputFile

## Tags
#component_rendering #testFile-format_IXML #category_input #category_error_handling

### Purpose
This test evaluates how the rendering tool handles an invalid input file, ensuring it alerts the user about the file's invalid content and exits without processing.

### Pre-requisites
* Java runtime 22
* placingToRendering.xml exists in the rendering-1.5.0.jar's folder and is a valid inputFile (produced by placing-1.5.0.jar with the mappingToPlacing.xml as input generated by mapping-1.5.0.jar with the cdfFile generated by converter-1.5.0.jar for the input codemetropolis-toolchain-commons.graph)
* command line opened in the folder of rendering-1.5.0.jar

### Steps
1. Open placingToRendering.xml with a text editor
2. Delete content from line 10 to 20 and save the file
3. Run:
	```cmd
	java -jar rendering-1.5.0.jar -i placingToRendering.xml -w cm
	```

### Expected result:
The tool warns the user that the input file content is invalid and exits normally without creating the output file.
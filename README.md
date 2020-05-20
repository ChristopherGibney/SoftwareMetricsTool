# SoftwareMetricsTool
This tool takes a local project, or a local or remote GitHub repository as input and calculates several Coupling and Cohesion Metrics for all Java files present as well as an Application Level metric. In the case of a GitHub repository entered as input, the application creates a subfolder for each branch in a results folder which contains graphs showing the evolution of all metrics at an application level as well as graphs showing the evolution for each class and package in the system. 

Instructions on how to run this tool, as well as a list of the metrtics currently implemented can be found in this readme file.

# How to run SoftwareMetricsTool
This tool can be run in two ways as outlined below. Firstly download the zip file for the repository and unzip it. 

**1)** Navigate to the directory containing the unzipped files on the command line and enter the command: "java -jar SoftwareMetricsTool.jar" (prerequisite: java must be installed on your machine). The tool must be run from command line as it requires user input. Finally, follow the instructions printed out to the console.

**2)** Alternatively, the source code can be opened in eclipse and run from there. Ensure the Maven plugin is installed in Eclipse (Eclipse -> Help -> Marketplace -> Search "Maven" -> Install Maven Integration for Eclipse -> follow instructions)

Once Maven Integration is installed open Eclipse -> File -> Import -> Maven -> Existing Maven Projects -> Next -> Browse and select folder where zipped file was unzipped to -> OK -> Select project pom file -> Finish. 

The tool can now be run in Eclipse by running the AnalysisRunner class and entering input in the console, and source code can also be viewed.

# Metrics Implemented
This tool implements 4 Coupling Metrics and 4 Cohesion Metrics. It also implements an Application Level Metric.

**Application Level Metric**: This is calculated for each of the 8 metrics. It is calculated by storing weighted values for each class and package, normalizing this data once the analysis has completed, and computing the average normalized value. It gives a user a general idea of the level of cohesion and coupling across their entire application for each off the eight metrics.

**Afferent Coupling** *(Martin, R.C. Object Oriented Design Quality Metrics: An Analysis of Dependencies.C++Report(1994))*: A package level Coupling Metric which measures the number of classes outside a package that use classes inside the package.

**Efferent Coupling** *(Martin, R.C. Object Oriented Design Quality Metrics: An Analysis of Dependencies.C++Report(1994))*: A package level Coupling Metric which measures the number of classes inside a package which use classes from a different package.

**Coupling Between Object Classes** *(Chidamber, S.R., Kemerer, C.F. A Metrics Suite for Object Oriented Design.IEEE Transac-tions on Software Engineering20,476–493 (1994))*: A class level Coupling Metric calculated as a count of the size of the set of classes to which a given class is coupled. A class is coupled if it uses a method or instance variable from another class and coupling to a class is counted once (as the metric is the size of the set of classes to which a class is coupled).

**Data Abstraction Coupling** *(Briand, L.C., Daly, W., Wust, K. A unified framework for coupling measurement in object-oriented systems.Software Engineering, IEEE Transactions25,91–121 (1999))*: A class level Coupling metric which is calculated by this tool as a count of the number of class attributes of a class which are of the type Astract Data Type (classes from the same system as the class being analysed)

**Lack of Cohesion in Methods Five** *(Briand, L.C., Daly, W., Wust, K. A Unified Framework for Cohesion Measurement in Object-Oriented Systems.International Journal "Information Theories and Applications"13,82–91(2006))*: a class level lack of Cohesion Metric (lower value is positive higher value negative inverse of normal Cohesion Metric results) calculated as the number of methods in the class, minus the sum of similarities between methods divided by the total number of class attributes. This value is then divided by the number of methods minus one. The number of similarities between methods is defined as the sum of, for each pair of methods, the number of class attributes used by both of these methods. Algorithm used for this metric can be found here: *O’Cinnéide, M., Moghadam, I.H., Harman, M., Counsell, S., Tratt, L. An experimental search-based approach to cohesion metric evaluation.Empir Software Eng22,292–329 (2017)*.

**Sensitive Class Cohesion** *(Fernández, L., Peña, R. A Sensitive Metric of Class Cohesion.Empirical Software Engineering3,65–117 (1998))*: A class level Cohesion Metric calculated by first summing, for each pair of methods in a class, the intersection of class attributes used by the methods (i.e. the number of class attributes used in both methods), multiplied by the union of class attributes used in these methods (i.e. the total number of unique class attributes used across both methods), and divided by the minimum count of class attributes in either method multiplied by the total number of attributes in the class. Once this summation across all pairs of methods has been made, the resultant value is multiplied by two, and divided by the total number of methods in the class, multiplied by the total number of methods in the class minus one. The algorithm used for this metric can be found here: *O’Cinnéide, M., Moghadam, I.H., Harman, M., Counsell, S., Tratt, L. An experimental search-based approach to cohesion metric evaluation.Empir Software Eng22,292–329 (2017)*.

**Class Cohesion** *(Bonja, C., Kidanmariam, E. Metrics for Class Cohesion and Similarity Between Methods.Proceedings of the 44th annual Southeast regional conference. ACM, Florida.44,91–95(2006))*: A class level Cohesion Metric calculated by; for each pair of metrics, summing the intersection of class attributes used by the methods (i.e. the number of class attributes used in both methods), divided by the union of class attributes used in these methods. The result from this summation across all pairs of methods in the class is then multiplied by two and divided by the number of methods in the class multiplied by the number of methods in the class minus one. The algorithm used for this metric can be found here: *O’Cinnéide, M., Moghadam, I.H., Harman, M., Counsell, S., Tratt, L. An experimental search-based approach to cohesion metric evaluation.Empir Software Eng22,292–329 (2017)*.

**Package Cohesion** *(Gupta, V., Chhabra, J.K. Package level cohesion measurement in object-oriented software.Journal of the Brazilian Computer Society18,251–266 (2012))*: A package level Cohesion Metric which measures the cohesiveness of a package based on the relationships and dependencies between the modules within the package. This metric is calculated as a ratio.The number of dependencies between modules of the package is divided by the total number of possible dependencies.

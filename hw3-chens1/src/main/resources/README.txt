FileSystemCollectionReader.xml 
is the reader used in all kinds of CPE to read local files.

hw2-chens1-aae-client.xml
is to call the service that I deploy. The service endpoint is "Score".

hw2-chens1-aae-deploy.xml
is used to deploy the service to the server

hw2-chens1-aae.xml
is modified now only NGram is used here.

hw3-chens1-aae-nlp.xml
is used to intergrate the nlp scoring method. 
This aae is integrated into hw3-chens1-CPE-test.xml 
to run the nlp intergrated scoring method

hw3-chens1-CPE.xml
is used to run the homework 2 pipeline

hw3-chens1-nameEntity.xml
is a annotator descriptor to use the name Entity locally.

hw3-chens1-casconsumer.xml
puts the evaluation part into the consumer part

hw3-chens1-aae-as-CPE.xml
this was to test my own service

hw2-chens1-scnlp-local.xml
local stanford nlp to comare the speed of different service(local and remote).

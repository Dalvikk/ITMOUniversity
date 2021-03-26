@javac -d __out RunJS.java && java -ea --module-path=graal -cp __out RunJS %*

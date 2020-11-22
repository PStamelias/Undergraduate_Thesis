
run: 
	java -cp .:jsoup-1.13.1.jar:postgresql-42.2.18.jar:selenium-server-standalone-3.141.59.jar TikTokCrawling 

compile:
	javac -cp .:jsoup-1.13.1.jar:postgresql-42.2.18.jar:selenium-server-standalone-3.141.59.jar TikTokCrawling.java Fun.java Fun2.java

clean:
	rm -r TikTokCrawling.class Video.class Fun2.class Fun.class

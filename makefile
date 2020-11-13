
run: 
	java -cp jsoup-1.13.1.jar:. TikTokCrawling 

compile:
	javac -cp jsoup-1.13.1.jar TikTokCrawling.java 

clean:
	rm -r TikTokCrawling.class

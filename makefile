
all:compile


compile:
	javac TikTokCrawling.java

clean:
	rm -r *.class *~
	rm **/*.class

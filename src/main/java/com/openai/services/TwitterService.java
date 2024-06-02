package com.openai.services;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterService {

	@Value("${twitter.api.key}")
	private String apiKey;

	@Value("${twitter.api.secret}")
	private String apiSecret;

	@Value("${twitter.api.bearerToken}")
	private String bearerToken;

	@Value("${twitter.api.accesstoken}")
	private String accessToken;

	@Value("${twitter.api.token.secret}")
	private String accessTokenSecret;

	private Twitter twitter;

	@PostConstruct
	public void init() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(apiKey).setOAuthConsumerSecret(apiSecret)
				.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		this.twitter = tf.getInstance();
	}

	public void postTweet(String tweetContent) {

		try {
			twitter.updateStatus(tweetContent);
			System.out.println("Successfully posted the tweet: " + tweetContent);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.out.println("Failed to post the tweet.");
		}
	}
}

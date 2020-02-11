package com.prueba.acciona.controller;

import java.io.IOException;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.acciona.exception.RecordNotFoundException;
import com.prueba.acciona.model.TweetEntity;
import com.prueba.acciona.service.TwiterService;
import com.prueba.acciona.service.TwiterService.HashtagCount;

import twitter4j.TwitterException;

@RestController
@RequestMapping("/twitter")
public class TwitterController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TwiterService service;

	@GetMapping
	public ResponseEntity<List<TweetEntity>> getAllTwitter() {
		List<TweetEntity> list = service.getAllTweet();
		logger.info("getAllTwitter -- " + list);
		return new ResponseEntity<List<TweetEntity>>(list, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/valido")
	public ResponseEntity<List<TweetEntity>> getAllTweetValido() {
		List<TweetEntity> list = service.getAllTweetValido();
		return new ResponseEntity<List<TweetEntity>>(list, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TweetEntity> getTwittById(@PathVariable("id") Long id) throws RecordNotFoundException {
		TweetEntity entity = service.getTwittById(id);

		return new ResponseEntity<TweetEntity>(entity, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<TweetEntity> createOrUpdateTwitt(TweetEntity TweetEntity)
			throws RecordNotFoundException {
		TweetEntity updated = service.createOrUpdateTwitt(TweetEntity);
		return new ResponseEntity<TweetEntity>(updated, new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public HttpStatus deleteTwittById(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.deleteTwittById(id);
		return HttpStatus.FORBIDDEN;
	}
	
	@GetMapping("/clasificacion")
	public ResponseEntity<List<HashtagCount>> clasificacion()
			throws RecordNotFoundException, TwitterException, IOException {
		List<HashtagCount> clasificacion = service.clasificacion();

		return new ResponseEntity<List<HashtagCount>>(clasificacion, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/usertimeline")
	public ResponseEntity<List<TweetEntity>> getusertimeline() throws TwitterException, IOException {
		List<TweetEntity> list = service.getUserTimeline();
		return new ResponseEntity<List<TweetEntity>>(list, new HttpHeaders(), HttpStatus.OK);
	}
}
package com.prueba.acciona.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.acciona.config.AppProperties;
import com.prueba.acciona.exception.RecordNotFoundException;
import com.prueba.acciona.model.TweetEntity;
import com.prueba.acciona.repository.TwitterRepository;
import com.prueba.acciona.util.Util;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

@Service
public class TwiterService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TwitterRepository repository;
	@Autowired
	private AppProperties appProperties;

	public List<TweetEntity> getAllTweet() {
		List<TweetEntity> findAll = repository.findAll();
		logger.info("twittsList: " + findAll);
		if (findAll.size() > 0) {
			return findAll;
		} else {
			return new ArrayList<TweetEntity>();
		}
	}

	public List<TweetEntity> getAllTweetValido() {
		List<TweetEntity> twittsList = repository.findAll();

		List<TweetEntity> collect = twittsList.stream().filter(twit -> twit.isValido() == true)
				.collect(Collectors.toList());

		logger.info("twittsList: " + collect);

		return (collect.size() > 0) ? collect : new ArrayList<TweetEntity>();
	}

	public TweetEntity getTwittById(Long id) throws RecordNotFoundException {
		Optional<TweetEntity> twitt = repository.findById(id);

		if (twitt.isPresent()) {
			return twitt.get();
		} else {
			throw new RecordNotFoundException("No twitt record exist for given id");
		}
	}

	public TweetEntity createOrUpdateTwitt(TweetEntity entity) throws RecordNotFoundException {
		Optional<TweetEntity> twitt = repository.findById(entity.getId());

		if (twitt.isPresent()) {
			TweetEntity newEntity = twitt.get();

			newEntity.setLocalizacion(
					entity.getLocalizacion() == null ? newEntity.getLocalizacion() : entity.getLocalizacion());
			newEntity.setTexto(entity.getTexto() == null ? newEntity.getTexto() : entity.getTexto());
			newEntity.setUsuario(entity.getUsuario() == null ? newEntity.getUsuario() : entity.getUsuario());
			newEntity.setValido(entity.isValido());

			newEntity = repository.save(newEntity);

			return newEntity;
		} else {
			entity = repository.save(entity);

			return entity;
		}
	}

	public List<HashtagCount> clasificacion() throws TwitterException, IOException {

		Integer hashtagsUsed = appProperties.getHashtagsUsed();

		List<TweetEntity> findAll = repository.findAll();
		HashMap<String, Integer> hashMap = new HashMap<>();

		List<HashtagCount> hashtagCounts = new ArrayList<>();

		for (TweetEntity TweetEntity : findAll) {
			String[] split = TweetEntity.getTexto().replace("\n", " ").split(" ");
			for (int i = 0; i < split.length; i++) {
				String string = split[i];
				if (string.startsWith("#")) {
					String key = string;
					Integer integer = hashMap.get(key);

					if (integer == null) {
						integer = 1;
					} else {
						integer++;
					}
					hashMap.put(key, integer);
				}
			}
		}

		Set<String> keySet = hashMap.keySet();
		for (String key : keySet) {
			Integer integer = hashMap.get(key);
			hashtagCounts.add(new HashtagCount(key, integer));
		}

		Collections.sort(hashtagCounts, Collections.reverseOrder());
		List<HashtagCount> subList = null;
		if (hashtagCounts.size() >= hashtagsUsed - 1) {
			subList = hashtagCounts.subList(0, hashtagsUsed);
		}

		return subList;
	}

	public List<TweetEntity> getUserTimeline() throws TwitterException, IOException {
		logger.info("findAll 1: " + repository.findAll());

		Integer favourites = Integer.parseInt(appProperties.getFavourites());
		logger.info("favourites: " + favourites);
		List<String> defaultLanguages = appProperties.getDefaultLanguages();
		logger.info("defaultLanguages " + defaultLanguages);

		List<TweetEntity> findAll = repository.findAll();
		logger.info("findAll 2: " + findAll);

		ResponseList<Status> autorizar = Util.autorizar();
		for (Status status : autorizar) {
			int favouritesCount = status.getUser().getFavouritesCount();
			String lang = status.getLang();
			if (favouritesCount > favourites && (defaultLanguages.contains(lang))) {
				logger.info("Status: " + status);
				String texto = status.getText();
				String usuario = status.getUser().getName();
				String localizacion = lang;

				TweetEntity twiterEntity = new TweetEntity();
				twiterEntity.setUsuario(usuario);
				twiterEntity.setTexto(texto);

				twiterEntity.setLocalizacion(localizacion);

				twiterEntity.setValido(false);
				repository.save(twiterEntity);
			}
		}
		return findAll;
	}

	public void deleteTwittById(Long id) throws RecordNotFoundException {
		Optional<TweetEntity> twitt = repository.findById(id);

		if (twitt.isPresent()) {
			repository.deleteById(id);
		} else {
			throw new RecordNotFoundException("No twitt record exist for given id");
		}
	}

	public class HashtagCount implements Comparable<HashtagCount> {
		private String hashtag;
		private Integer count;

		public HashtagCount(String hashtag, Integer count) {
			super();
			this.hashtag = hashtag;
			this.count = count;
		}

		@Override
		public String toString() {
			return "HashtagCount [hashtag=" + hashtag + ", count=" + count + "]";
		}

		public String getHashtag() {
			return hashtag;
		}

		public void setHashtag(String hashtag) {
			this.hashtag = hashtag;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		@Override
		public int compareTo(HashtagCount o) {
			return this.getCount().compareTo(o.getCount());
		}

	}
}

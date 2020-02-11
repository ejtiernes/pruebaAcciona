package com.prueba.acciona.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
	private String favourites;
	private Integer hashtagsUsed;
	private List<String> defaultLanguages;

	public String getFavourites() {
		return favourites;
	}

	public void setFavourites(String favourites) {
		this.favourites = favourites;
	}

	public List<String> getDefaultLanguages() {
		return defaultLanguages;
	}

	public void setDefaultLanguages(List<String> defaultLanguages) {
		this.defaultLanguages = defaultLanguages;
	}

	public Integer getHashtagsUsed() {
		return hashtagsUsed;
	}

	public void setHashtagsUsed(Integer hashtagsUsed) {
		this.hashtagsUsed = hashtagsUsed;
	}

}

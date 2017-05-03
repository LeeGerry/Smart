package edu.auburn.smart.model;

import java.io.Serializable;

public class Article implements Serializable{
	private int articleId;
	private long articleDate;
	private String articleTitle;
	private String articleImg;
	private String articleDescription;
	private String articleUrl;
	private int category_id;
	private int source_id;

	public Article(int articleId, long articleDate, String articleTitle, String articleImg, String articleDescription, String articleUrl, int category_id, int source_id) {
		this.articleId = articleId;
		this.articleDate = articleDate;
		this.articleTitle = articleTitle;
		this.articleImg = articleImg;
		this.articleDescription = articleDescription;
		this.articleUrl = articleUrl;
		this.category_id = category_id;
		this.source_id = source_id;
	}

	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public long getArticleDate() {
		return articleDate;
	}
	public void setArticleDate(long articleDate) {
		this.articleDate = articleDate;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticleImg() {
		return articleImg;
	}
	public void setArticleImg(String articleImg) {
		this.articleImg = articleImg;
	}
	public String getArticleDescription() {
		return articleDescription;
	}
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}
	public String getArticleUrl() {
		return articleUrl;
	}
	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getSource_id() {
		return source_id;
	}
	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}
	@Override
	public String toString() {
		return "Article [articleId=" + articleId + ", articleDate=" + articleDate + ", articleTitle=" + articleTitle
				+ ", articleImg=" + articleImg + ", articleDescription=" + articleDescription + ", articleUrl="
				+ articleUrl + ", category_id=" + category_id + ", source_id=" + source_id + "]";
	}
}

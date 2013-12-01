package com.glassthetic.hackernews;

import org.jsoup.nodes.Element;

/**
 * Represents a single Hacker News post.
 */
public class Post {
  private final String title;
  private final String author;
  private final String timeDiff;
  private final String postUrl;
  private final String commentsUrl;
  private final int points;
  private final int numComments;

  private Post(Builder builder) {
    this.title = builder.title;
    this.author = builder.author;
    this.timeDiff = builder.timeDiff;
    this.postUrl = builder.postUrl;
    this.commentsUrl = builder.commentsUrl;
    this.points = builder.points;
    this.numComments = builder.numComments;
  }

  public static Post withPostTitleAndSubTextElements(Element postTitle, Element subText) {
    String title = postTitle.text();
    String author = subText.select("a[href^=user]").text();
    String postUrl = postTitle.attr("href");

    return new Builder()
        .withTitle(title)
        .withAuthor(author)
        .withPostUrl(postUrl)
        .build();
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public String getTimeDiff() {
    return timeDiff;
  }

  public String getPostUrl() {
    return postUrl;
  }

  public String getCommentsUrl() {
    return commentsUrl;
  }

  public int getPoints() {
    return points;
  }

  public int getNumComments() {
    return numComments;
  }

  /**
   * Builds a {@link com.glassthetic.hackernews.Post} object.
   */
  public static class Builder {
    private String title;
    private String author;
    private String timeDiff;
    private String postUrl;
    private String commentsUrl;
    private int points;
    private int numComments;

    public Builder() {
    }

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withAuthor(String author) {
      this.author = author;
      return this;
    }

    public Builder withTimeDiff(String timeDiff) {
      this.timeDiff = timeDiff;
      return this;
    }

    public Builder withPostUrl(String postUrl) {
      this.postUrl = postUrl;
      return this;
    }

    public Builder withCommentsUrl(String commentsUrl) {
      this.commentsUrl = commentsUrl;
      return this;
    }

    public Builder withPoints(int points) {
      this.points = points;
      return this;
    }

    public Builder withNumComments(int numComments) {
      this.numComments = numComments;
      return this;
    }

    public Post build() {
      return new Post(this);
    }
  }
}

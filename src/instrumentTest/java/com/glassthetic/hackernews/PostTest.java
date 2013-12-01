package com.glassthetic.hackernews;

import junit.framework.TestCase;


public class PostTest extends TestCase {
  private Post post;

  public void setUp() throws Exception {
    super.setUp();

    post = new Post.Builder()
        .withTitle("TITLE")
        .withAuthor("AUTHOR")
        .withTimeDiff("7 minutes ago")
        .withPostUrl("http://someposturl.com")
        .withCommentsUrl("https://news.ycombinator.com/item?id=6823668")
        .withPoints(135)
        .withNumComments(16)
        .build();
  }

  public void testGetTitle() throws Exception {
    assertEquals(post.getTitle(), "TITLE");
  }

  public void testGetAuthor() throws Exception {
    assertEquals(post.getAuthor(), "AUTHOR");
  }

  public void testGetTimeDiff() throws Exception {
    assertEquals(post.getTimeDiff(), "7 minutes ago");
  }

  public void testGetPostUrl() throws Exception {
    assertEquals(post.getPostUrl(), "http://someposturl.com");
  }

  public void testGetCommentsUrl() throws Exception {
    assertEquals(post.getCommentsUrl(), "https://news.ycombinator.com/item?id=6823668");
  }

  public void testGetPoints() throws Exception {
    assertEquals(post.getPoints(), 135);
  }

  public void testGetNumComments() throws Exception {
    assertEquals(post.getNumComments(), 16);
  }
}

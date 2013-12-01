package com.glassthetic.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
  private static final String HOMEPAGE_URL = "https://news.ycombinator.com/";
  private static final String POST_TITLE_SELECTOR = "table:not([width]) tbody tr td[class=title]:not([valign]) a:not([href=news2])";
  private static final String POST_SUBTEXT_SELECTOR = "table:not([width]) tbody tr td[class=subtext]";
  private PostCardScrollAdapter mAdapter;
  private CardScrollView mCardScrollView;
  private RequestQueue mRequestQueue;
  private List<Card> mCards;
  private List<Post> mPosts;
  private Card mLoadingCard;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mRequestQueue = Volley.newRequestQueue(this);
    mCards = new ArrayList<Card>();
    mPosts = new ArrayList<Post>();

    mLoadingCard = new Card(this);
    mLoadingCard
        .setText("Loading...")
        .setInfo(R.string.app_name);

    initCardScrollView();
    setContentView(mCardScrollView);
    fetchPosts();
  }

  private void initCardScrollView() {
    mCardScrollView = new CardScrollView(this);
    mCardScrollView.setOnItemClickListener(this);
    mAdapter = new PostCardScrollAdapter();
    mCardScrollView.setAdapter(mAdapter);
    mCardScrollView.activate();
  }

  private void fetchPosts() {
    mLoadingCard.setText("Loading...");
    mCards.clear();
    mCards.add(mLoadingCard);
    mAdapter.notifyDataSetChanged();

    mRequestQueue.add(new StringRequest(HOMEPAGE_URL, new Response.Listener<String>() {
      @Override
      public void onResponse(String html) {
        Document doc = Jsoup.parse(html);
        Elements postTitles = doc.select(POST_TITLE_SELECTOR);
        Elements subTexts = doc.select(POST_SUBTEXT_SELECTOR);

        if (postTitles.size() != subTexts.size()) {
          Log.e("fetchPosts()", "postTitles and subTexts are of varying lengths!");
          mLoadingCard.setText("Could not fetch, please try again!");
          mAdapter.notifyDataSetChanged();
          return;
        }

        Element postTitle, subText;
        Post post;
        Card card;
        for (int i = 0; i < postTitles.size(); i++) {
          postTitle = postTitles.get(i);
          subText = subTexts.get(i);
          post = Post.withPostTitleAndSubTextElements(postTitle, subText);

          card = new Card(getApplicationContext());
          card
              .setText(post.getTitle())
              .setInfo("By " + post.getAuthor());

          mPosts.add(post);
          mCards.add(card);
        }
        mLoadingCard.setText("Loaded!");
        mAdapter.notifyDataSetChanged();
      }
    }, null));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.post, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    if (mCardScrollView.getSelectedItemPosition() == 0) {
      menu.setGroupVisible(R.id.group_loading, true);
      menu.setGroupVisible(R.id.group_post, false);
    } else {
      menu.setGroupVisible(R.id.group_loading, false);
      menu.setGroupVisible(R.id.group_post, true);
    }

    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.view_website_menu_item:
        int postPosition = mCardScrollView.getSelectedItemPosition() - 1;
        String url = mPosts.get(postPosition).getPostUrl();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        return true;
      case R.id.refresh_menu_item:
        fetchPosts();
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    openOptionsMenu();
  }

  private class PostCardScrollAdapter extends CardScrollAdapter {
    @Override
    public int getCount() {
      return mCards.size();
    }

    @Override
    public Card getItem(int position) {
      return mCards.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
      return getItem(position).toView();
    }

    @Override
    public int findIdPosition(Object id) {
      return -1;
    }

    @Override
    public int findItemPosition(Object item) {
      return mCards.indexOf(item);
    }
  }
}

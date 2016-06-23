package base.brian.com.mymvvmdemo;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;

import base.brian.com.mymvvmdemo.network.command.ReplyCommand;
import base.brian.com.mymvvmdemo.model.ViewModel;
import base.brian.com.mymvvmdemo.news.TopNewsService;
import base.brian.com.mymvvmdemo.newsdetail.NewsDetailActivity;


/**
 * Created by kelin on 16-4-26.
 */
public class TopItemViewModel implements ViewModel {
    //context
    private Context context;

    //model
    public TopNewsService.News.TopStoriesBean topStoriesBean;

    //field to presenter
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> imageUrl = new ObservableField<>();

    public final ReplyCommand topItemClickCommand = new ReplyCommand(() -> {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.EXTRA_KEY_NEWS_ID, topStoriesBean.getId());
        context.startActivity(intent);
    });

    public TopItemViewModel(Context context, TopNewsService.News.TopStoriesBean topStoriesBean) {
        this.context = context;
        this.topStoriesBean = topStoriesBean;
        title.set(topStoriesBean.getTitle());
        imageUrl.set(topStoriesBean.getImage());
    }
}

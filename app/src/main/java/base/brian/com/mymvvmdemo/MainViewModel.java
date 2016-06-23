package base.brian.com.mymvvmdemo;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import base.brian.com.mymvvmdemo.network.messenger.Messenger;
import base.brian.com.mymvvmdemo.model.ViewModel;
import base.brian.com.mymvvmdemo.news.NewsViewModel;
import base.brian.com.mymvvmdemo.news.TopNewsService;
import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;

/**
 * Created by brian on 16/6/22.
 */
public class MainViewModel implements ViewModel{
    // Token to Messenger append package name to be unique
    public static final String TOKEN_UPDATE_INDICATOR = "token_update_indicator" + BrianApp.sPackageName;

    //context
    private Context context;

    // viewModel for recycler header viewPager
    public final ItemView topItemView = ItemView.of(
            base.brian.com.mymvvmdemo.BR.viewModel, base.brian.com.mymvvmdemo.R.layout.viewpager_item_top_news);
    public final ObservableList<TopItemViewModel> topItemViewModel = new ObservableArrayList<>();

    public MainViewModel(Activity activity) {
        context=activity;
        Messenger.getDefault().register(activity, NewsViewModel.TOKEN_TOP_NEWS_FINISH, TopNewsService.News.class,
                (news)->{
                    Observable.just(news)
                            .doOnNext(m -> {topItemViewModel.clear();})
                            .flatMap(n -> Observable.from(n.getTop_stories()))
                            .doOnNext(m -> topItemViewModel.add(new TopItemViewModel(context,m)))
                            .toList()
                            .subscribe((l) -> Messenger.getDefault().sendNoMsgToTargetWithToken(TOKEN_UPDATE_INDICATOR,activity));

                });
    }
}

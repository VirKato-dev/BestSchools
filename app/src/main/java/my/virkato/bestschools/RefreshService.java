package my.virkato.bestschools;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RefreshService extends Thread {

    interface OnComplete {
        void done(List<SchoolItem> schools);
    }

    private final OnComplete listener;

    private boolean isBusy = false;


    public RefreshService(OnComplete listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (isBusy) continue;
            refreshList();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
        Log.e("RefreshService", "RefreshService stopped");
    }

    /**
     * Отправить запрос и вызвать колбэк
     */
    private void refreshList() {
        CompletableFuture
                .supplyAsync(() -> {
                    isBusy = true;
                    try {
                        return getList();
                    } catch (IOException ignored) {
                        return new LinkedList<SchoolItem>();
                    }
                })
                .thenAccept(list -> {
                    if (listener != null) {
                        listener.done(list);
                    }
                    isBusy = false;
                });
    }

    /**
     * Парсим пост на Хабре
     * @return список школ с количеством голосов
     * @throws IOException при получении данных из интернета
     */
    private static List<SchoolItem> getList() throws IOException {
        List<SchoolItem> list = new ArrayList<>();
        String url = "https://habr.com/ru/post/710330/";
        Document doc = Jsoup.connect(url).get();
        Element body = doc.body();
        Elements schools = body.select("div .tm-article-poll__answer .tm-article-poll__answer-data");
        for (Element el : schools) {
            SchoolItem item = new SchoolItem();
            item.name = el.child(1).text();
            item.votes = Integer.parseInt(el.child(2).text());
            list.add(item);
        }
        return list;
    }

}

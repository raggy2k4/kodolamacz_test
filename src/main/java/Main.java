import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) {

        final String YOUR_API_KEY = "de10369345794239a2d97204d86f7581";
        final String FILE_PATH;
        String  fileName = "polandBusinessNews",
                fileType = ".txt",
                nextFileNumber = fileName;

        for (int i = 1; true ; i++) {
            if (new File(fileName + fileType).isFile()) {
                fileName = nextFileNumber + "_" + i;
                continue;
            }
            FILE_PATH = fileName + fileType;
            break;
        }



        NewsApiClient newsApiClient = new NewsApiClient(YOUR_API_KEY);

        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .category("business")
                        .country("pl")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        int apiTotalResults = response.getTotalResults();
                        if (apiTotalResults > 20){
                            apiTotalResults = 20;
                        }

                        String  title,
                                description,
                                author;
                        try {

                            title = response.getArticles().get(0).getTitle();
                            description = response.getArticles().get(0).getDescription();
                            author = response.getArticles().get(0).getAuthor();

                            Files.writeString(Paths.get(FILE_PATH),
                                    title+":"+description+":"+author+"\n"
                                    , StandardOpenOption.CREATE_NEW);

                            for (int i = 1; i < apiTotalResults; i++) {
                                title = response.getArticles().get(i).getTitle();
                                description = response.getArticles().get(i).getDescription();
                                author = response.getArticles().get(i).getAuthor();

                                Files.writeString(Paths.get(FILE_PATH),
                                        title+":"+description+":"+author+"\n"
                                        , StandardOpenOption.APPEND);

                            }
                            System.out.println("Business news for Poland written - \"application path\\"+FILE_PATH+".\"");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());


                    }

                }
        );

    }
}


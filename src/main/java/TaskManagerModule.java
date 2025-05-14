import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class TaskManagerModule {
    @Provides
    @Singleton
    TaskManager provideTaskManager(Gson gson)
    {
        return new TaskManager(gson);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }
}
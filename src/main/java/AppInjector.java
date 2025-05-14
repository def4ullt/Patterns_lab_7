import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {TaskManagerModule.class})
public interface AppInjector {
    TaskManager getTaskManager();
}

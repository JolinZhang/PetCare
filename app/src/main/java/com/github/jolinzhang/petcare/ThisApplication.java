package com.github.jolinzhang.petcare;

/**
 * Created by Shadow on 11/21/16.
 */

import android.app.Application;

import com.github.jolinzhang.model.DataRepoConfig;
import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.util.Configuration;
import com.github.jolinzhang.util.Util;

public class ThisApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataRepository.init(this);
        DataRepoConfig.init(this);
        Configuration.init(this);
        Util.init(this);
    }

}

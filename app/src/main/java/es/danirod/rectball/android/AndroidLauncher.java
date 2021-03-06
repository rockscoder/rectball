/*
 * This file is part of Rectball.
 * Copyright (C) 2015 Dani Rodríguez.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.danirod.rectball.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import es.danirod.rectball.RectballGame;
import es.danirod.rectball.model.GameState;

public class AndroidLauncher extends AndroidApplication {

    public static final String PACKAGE = "es.danirod.rectball.android";

    private AndroidPlatform platform;

    private RectballGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up platform.
        platform = new AndroidPlatform(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        if (platform.preferences().getBoolean("fullscreen")) {
            config.useImmersiveMode = true;
            config.hideStatusBar = true;
            putFullscreen();
        }

        if (savedInstanceState != null) {
            Json json = new Json();
            String jsonBoard = savedInstanceState.getString("state");
            if (jsonBoard != null) {
                GameState state = json.fromJson(GameState.class, jsonBoard);
                game = new RectballGame(platform, state);
            } else {
                game = new RectballGame(platform);
            }
            Log.d("Rectball", "Restoring state: " + jsonBoard);
        } else {
            game = new RectballGame(platform);
            Log.d("Rectball", "New execution. No restoring state needed.");
        }

        View rectballView = initializeForView(game, config);
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(rectballView);
        setContentView(layout);
    }

    private void putFullscreen() {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch (Exception ex) {
            log("AndroidApplication", "Cannot put FEATURE_NO_TITLE", ex);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the game state if the game is running.
        if (game.getState().isPlaying()) {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            String jsonBoard = json.toJson(game.getState(), GameState.class);
            outState.putString("state", jsonBoard);
            Gdx.app.debug("Rectball", "Saving State: " + jsonBoard);
        } else {
            Gdx.app.debug("Rectball", "Not playing, no need to save state.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        platform.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        platform.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        platform.onActivityResult(requestCode, resultCode, data);
    }
}

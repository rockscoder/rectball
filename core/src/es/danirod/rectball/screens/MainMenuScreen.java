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
package es.danirod.rectball.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import es.danirod.rectball.RectballGame;
import es.danirod.rectball.listeners.ScreenJumper;

import static es.danirod.rectball.Constants.STAGE_PADDING;

public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(RectballGame game) {
        super(game);
    }

    @Override
    public void setUpInterface(Table table) {
        // Build the actors.
        Image title = new Image(game.manager.get("logo.png", Texture.class));
        title.setScaling(Scaling.fit);
        TextButton play = new TextButton(game.getLocale().get("main.play"), game.getSkin());
        TextButton settings = new TextButton(game.getLocale().get("main.settings"), game.getSkin());
        TextButton statistics = new TextButton(game.getLocale().get("main.stats"), game.getSkin());
        TextButton about = new TextButton(game.getLocale().get("main.about"), game.getSkin());

        // Position the actors in the screen.
        table.add(title).pad(40).row();
        table.add(play).padBottom(STAGE_PADDING).fillX().height(100).row();
        table.add(settings).padBottom(STAGE_PADDING).fillX().height(100).row();
        table.add(statistics).padBottom(STAGE_PADDING).fillX().height(100).row();
        table.add(about).padBottom(STAGE_PADDING).fillX().height(100).row();

        // Then add the capture listeners for the buttons.
        play.addCaptureListener(new ScreenJumper(game, Screens.GAME));
        settings.addCaptureListener(new ScreenJumper(game, Screens.SETTINGS));
        statistics.addCaptureListener(new ScreenJumper(game, Screens.STATISTICS));
        about.addCaptureListener(new ScreenJumper(game, Screens.ABOUT));
    }

    @Override
    public int getID() {
        return Screens.MAIN_MENU;
    }
}
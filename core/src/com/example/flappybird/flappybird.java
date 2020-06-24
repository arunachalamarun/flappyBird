package com.example.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import sun.rmi.runtime.Log;

public class flappybird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background, birds[], tube[], gameOver;
    int flapState = 0;
    float accumulator = 0f;
    float birdY = 0f;    float birdX = 0f;

    float velocity = 0f;
    int gameState = 0;
    Music rainMusic;
    float changeVelocity = 2f;
    float gap = 400;
    int renderCount = 0;
    Random rand;

    int score = 0;
    int tubeIdentifier = 0;
    BitmapFont font;

    Circle birdCircle;
    ShapeRenderer shapeRenderer;
    Rectangle rectangle, rectangle2;


    float changeTube[] = new float[4];
    float tubeX[] = new float[4];
    float distanceBetweenTubes;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        tube = new Texture[2];
        tube[0] = new Texture("toptube.png");
        tube[1] = new Texture("bottomtube.png");
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        gameOver = new Texture("gameOver.png");
        font = new BitmapFont();
        font.setColor(Color.PURPLE);
        font.getData().setScale(10);
          rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));


        distanceBetweenTubes = (float) ((Gdx.graphics.getWidth() * 1.5) / 2);
        rand = new Random();

        birdCircle = new Circle();
        rectangle = new Rectangle();
        rectangle2 = new Rectangle();
        shapeRenderer = new ShapeRenderer();


        startGame();


    }


    public void startGame() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        birdX=Gdx.graphics.getWidth() / 2 - (birds[flapState].getWidth() * 3);
        for (int i = 0; i < 4; i++) {

            tubeX[i] = Gdx.graphics.getWidth() / 2 - tube[1].getWidth() / 2 + i * distanceBetweenTubes;
            rainMusic.play();
            changeTube[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
        }
    }

    @Override
    public void render() {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();

        //  tubeX = Gdx.graphics.getWidth() / 2 - tube[1].getWidth() / 2;


        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        // while (i != 0) {
        if (gameState == 1) {

            //bottomTube
            //, tube[0].getWidth(), Gdx.graphics.getHeight() / 2 + changeTube);

            if (tubeX[tubeIdentifier] < Gdx.graphics.getWidth() / 2) {
                score++;
                //score -= 1;
                //Gdx.app.log("Score", String.valueOf(score));
                if (tubeIdentifier <= 2) {
                    tubeIdentifier++;
                } else {
                    tubeIdentifier = 0;
                }
            }


            if (Gdx.input.justTouched()) {
                //  Gdx.app.log("touched", "workeed");
                velocity = -30;
            }

            if (birdY > 0) {
                velocity = velocity + 2;
                birdY = birdY - velocity;
                // Gdx.app.log("Velocity", String.valueOf(velocity));

                //   Gdx.app.log("birdY", String.valueOf(birdY));
            } else {

                gameState = 2;
            }


            for (int i = 0; i < 4; i++) {
                if (tubeX[i] < -tube[1].getWidth()) {
                    tubeX[i] += 4 * distanceBetweenTubes;
                    changeTube[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {
                    tubeX[i] = tubeX[i] - 4;
                }
                // Gdx.app.log("tubeX", String.valueOf(tubeX[i]));

                batch.draw(tube[1], tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tube[1].getHeight() + changeTube[i]);
                //topTube
                batch.draw(tube[0], tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + changeTube[i]);
            }


        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                //  Gdx.app.log("touched", "workeed");
                gameState = 1;
            }
        } else if (gameState == 2) {
            Gdx.app.log("gameover", "gameover");
            font.setColor(Color.GREEN);
rainMusic.stop();birdY=0f;birdX=300f;
            font.draw(batch, "GameOver", 175, Gdx.graphics.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameState = 0;
                score = 0;
                velocity = 0;
                tubeIdentifier = 0;
                startGame();
            }

            //   batch.draw(gameOver,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        }


        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        if (birdY < 0) {
            flapState = 0;

        }
        font.draw(batch, String.valueOf(score), 100, 100);
        batch.draw(birds[flapState], birdX, birdY);


        //   Gdx.app.log("Height is", String.valueOf(Gdx.graphics.getHeight()));
        batch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        birdCircle.set(Gdx.graphics.getWidth() / 2 - (birds[flapState].getWidth() * 3 - birds[flapState].getWidth() / 2), birdY + birds[flapState].getWidth() / 2, birds[flapState].getWidth() / 2);
        if (gameState == 1) {
            for (int i = 0; i < 4; i++) {
                rectangle.set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tube[1].getHeight() + changeTube[i], tube[1].getWidth(), tube[1].getHeight());
                rectangle2.set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + changeTube[i], tube[1].getWidth(), tube[1].getHeight());
                //   shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                //shapeRenderer.rect(rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
                if (Intersector.overlaps(birdCircle, rectangle) || Intersector.overlaps(birdCircle, rectangle2)) {
                    Gdx.app.log("collision", "YES");
                    gameState = 2;
                    //   gameState = 2;
                     rainMusic.stop();
                }
            }
        }


        // shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        shapeRenderer.end();


    }
    //   }


    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }


}

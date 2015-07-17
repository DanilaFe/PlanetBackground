package com.danilafe.planetbackground.service;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.danilafe.planetbackground.R;

/**
 * Created by danila on 7/17/15.
 */
public class PlanetBackground extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new PlanetEngine();
    }

    private class PlanetEngine extends Engine {
        //Properties
        float worldWidth = 200;
        float padding = 5;
        int pause = 20;
        int perSecond = 1000 / pause;
        int speedup = 15;

        float fromStart = 0;

        int numberPlanets = 2;
        Planet[] planets = new Planet[numberPlanets];
        int width, height;
        Handler drawHandler = new Handler();
        Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                draw(planets);
                fromStart += 1F / perSecond;
                drawHandler.removeCallbacks(this);
                drawHandler.postDelayed(this, pause);
            }
        };
        Paint defaultPaint = new Paint();



        //Sun properties
        float sunRadius = 15;
        Paint sunPaint = new Paint();

        public void draw(Planet[] planets){
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Canvas canvas = surfaceHolder.lockCanvas();

            //Store current width / height
            width = canvas.getWidth();
            height = canvas.getHeight();

            //Clear canvas
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), defaultPaint);

            //Determine Scale Factor
            float scaleFactor = ((width < height) ? width : height) / worldWidth;

            //Draw the sun
            canvas.drawCircle(width / 2, height / 2, sunRadius * scaleFactor, sunPaint);

            //Draw the planets
            float fromCenter;
            fromCenter = sunRadius * scaleFactor;
            fromCenter += padding * scaleFactor;
            for(Planet planet : planets){
                fromCenter += planet.radius * scaleFactor;

                //Draw the trail
                RectF oval = new RectF();
                oval.set(width / 2 - fromCenter, height / 2 - fromCenter, width / 2 + fromCenter, height / 2 + fromCenter);
                canvas.drawArc(oval, (float) (-360 * ((fromStart * speedup) % planet.orbitDays) / planet.orbitDays) + 90, planet.trailLength, false, planet.trailColor);

                canvas.drawCircle(width / 2 + (float) Math.sin((double) (2 * Math.PI * ((fromStart * speedup) % planet.orbitDays) / planet.orbitDays)) * fromCenter, height / 2 + (float) Math.cos((double) (2 * Math.PI * ((fromStart * speedup) % planet.orbitDays) / planet.orbitDays)) * fromCenter, planet.radius * scaleFactor, planet.mainColor);

                fromCenter += padding * scaleFactor;
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(visible){
                drawHandler.removeCallbacks(drawRunnable);
                drawHandler.postDelayed(drawRunnable, 0);
            } else {
                drawHandler.removeCallbacks(drawRunnable);
            }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            //Adjust variables
            defaultPaint.setColor(getResources().getColor(R.color.background));
            sunPaint.setColor(getResources().getColor(R.color.sun));
            sunPaint.setStyle(Paint.Style.FILL);
            sunPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

            //Create the planets
            Planet mercury = new Planet();
            mercury.radius = 5;
            mercury.orbitDays = 88;
            mercury.mainColor.setColor(getResources().getColor(R.color.mercuryPrimary));
            mercury.trailColor.setColor(getResources().getColor(R.color.mercurySecondary));
            mercury.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[0] = mercury;

            Planet venus = new Planet();
            venus.radius = 5;
            venus.orbitDays = 225;
            venus.mainColor.setColor(getResources().getColor(R.color.venusPrimary));
            venus.trailColor.setColor(getResources().getColor(R.color.venusSecondary));
            venus.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[1] = venus;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }

    private class Planet {
        float radius;
        int orbitDays;
        int trailLength = 90;
        Paint mainColor = new Paint();
        Paint trailColor = new Paint();

        public Planet(){
            mainColor.setFlags(Paint.ANTI_ALIAS_FLAG);
            trailColor.setFlags(Paint.ANTI_ALIAS_FLAG);
            trailColor.setStyle(Paint.Style.STROKE);
        }
    }
}

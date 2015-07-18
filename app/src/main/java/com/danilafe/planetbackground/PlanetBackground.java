package com.danilafe.planetbackground;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.danilafe.planetbackground.R;

import java.util.Random;

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
        float worldWidth = 250;
        float padding = 5;
        int pause = 20;
        int perSecond = 1000 / pause;
        int speedup = 15;

        float fromStart = 0;

        int numberPlanets = 9;
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
                canvas.drawArc(oval, (float) (-360 * ((fromStart * speedup + planet.offset) % planet.orbitDays) / planet.orbitDays) + 90, planet.trailLength, false, planet.trailColor);

                canvas.drawCircle(width / 2 + (float) Math.sin((double) (2 * Math.PI * ((fromStart * speedup + planet.offset) % planet.orbitDays) / planet.orbitDays)) * fromCenter, height / 2 + (float) Math.cos((double) (2 * Math.PI * ((fromStart * speedup + planet.offset) % planet.orbitDays) / planet.orbitDays)) * fromCenter, planet.radius * scaleFactor, planet.mainColor);

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
            Random random = new Random();

            Planet mercury = new Planet();
            mercury.radius = 5;
            mercury.orbitDays = 88;
            mercury.trailLength = random.nextInt(90) + 45;
            mercury.offset = random.nextInt(180);
            mercury.mainColor.setColor(getResources().getColor(R.color.mercuryPrimary));
            mercury.trailColor.setColor(getResources().getColor(R.color.mercurySecondary));
            mercury.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[0] = mercury;

            Planet venus = new Planet();
            venus.radius = 5;
            venus.orbitDays = 225;
            venus.trailLength = random.nextInt(90) + 45;
            venus.offset = random.nextInt(180);
            venus.mainColor.setColor(getResources().getColor(R.color.venusPrimary));
            venus.trailColor.setColor(getResources().getColor(R.color.venusSecondary));
            venus.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[1] = venus;

            Planet earth = new Planet();
            earth.radius = 5;
            earth.orbitDays = 360;
            earth.trailLength = random.nextInt(90) + 45;
            earth.offset = random.nextInt(180);
            earth.mainColor.setColor(getResources().getColor(R.color.earthPrimary));
            earth.trailColor.setColor(getResources().getColor(R.color.earthSecondary));
            earth.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[2] = earth;

            Planet mars = new Planet();
            mars.radius = 5;
            mars.orbitDays = 687;
            mars.trailLength = random.nextInt(90) + 45;
            mars.offset = random.nextInt(180);
            mars.mainColor.setColor(getResources().getColor(R.color.marsPrimary));
            mars.trailColor.setColor(getResources().getColor(R.color.marsSecondary));
            mars.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[3] = mars;

            Planet jupiter = new Planet();
            jupiter.radius = 5;
            jupiter.orbitDays = 890;
            jupiter.trailLength = random.nextInt(90) + 45;
            jupiter.offset = random.nextInt(180);
            jupiter.mainColor.setColor(getResources().getColor(R.color.jupiterPrimary));
            jupiter.trailColor.setColor(getResources().getColor(R.color.jupiterSecondary));
            jupiter.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[4] = jupiter;

            Planet saturn = new Planet();
            saturn.radius = 5;
            saturn.orbitDays = 1050;
            saturn.trailLength = random.nextInt(90) + 45;
            saturn.offset = random.nextInt(180);
            saturn.mainColor.setColor(getResources().getColor(R.color.saturnPrimary));
            saturn.trailColor.setColor(getResources().getColor(R.color.saturnSecondary));
            saturn.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[5] = saturn;

            Planet uranus = new Planet();
            uranus.radius = 5;
            uranus.orbitDays = 1250;
            uranus.trailLength = random.nextInt(90) + 45;
            uranus.offset = random.nextInt(180);
            uranus.mainColor.setColor(getResources().getColor(R.color.uranusPrimary));
            uranus.trailColor.setColor(getResources().getColor(R.color.uranusSecondary));
            uranus.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[6] = uranus;

            Planet neptune = new Planet();
            neptune.radius = 5;
            neptune.orbitDays = 1435;
            neptune.trailLength = random.nextInt(90) + 45;
            neptune.offset = random.nextInt(180);
            neptune.mainColor.setColor(getResources().getColor(R.color.neptunePrimary));
            neptune.trailColor.setColor(getResources().getColor(R.color.neptuneSecondary));
            neptune.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[7] = neptune;


            Planet pluto = new Planet();
            pluto.radius = 5;
            pluto.orbitDays = 1740;
            pluto.trailLength = random.nextInt(90) + 45;
            pluto.offset = random.nextInt(180);
            pluto.mainColor.setColor(getResources().getColor(R.color.plutoPrimary));
            pluto.trailColor.setColor(getResources().getColor(R.color.plutoSecondary));
            pluto.trailColor.setStrokeWidth(mercury.radius * 2);
            planets[8] = pluto;
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
        int offset;
        Paint mainColor = new Paint();
        Paint trailColor = new Paint();

        public Planet(){
            mainColor.setFlags(Paint.ANTI_ALIAS_FLAG);
            trailColor.setFlags(Paint.ANTI_ALIAS_FLAG);
            trailColor.setStyle(Paint.Style.STROKE);
        }
    }
}

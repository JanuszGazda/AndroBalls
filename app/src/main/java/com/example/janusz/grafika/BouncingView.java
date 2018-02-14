package com.example.janusz.grafika;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class BouncingView extends View {

    Paint paint = new Paint();
    RectF rectF  = new RectF();
    LinkedList<HashMap> ballsList;

    int maxX = getWidth();
    int maxY = getHeight();
    int initSpeedX = 5;
    int initSpeedY = 10;
    int radius = 120;

    public BouncingView(Context context) {
        super(context);
        ballsList = new LinkedList<>();
        paint.setColor(Color.BLUE);
        addNewBall();
    }

    private void addNewBall() {
        HashMap<String, Integer> ballFeatures = new HashMap<>();
        boolean canAddHere = false;
        Random random = new Random();
        int randomX = 50;
        int randomY = 50;
        //adding a ball in a place where there is no balls there
        //intended for bouncing against other balls
        //to be added in next update
        while (!canAddHere && maxX!=0 && maxY!=0){
            canAddHere = true;
            randomX = random.nextInt(maxX-radius);
            randomY = random.nextInt(maxY-radius);
            initSpeedX = random.nextInt(20);
            initSpeedY = random.nextInt(20);
            RectF newRect = new RectF(randomX, randomY, randomX+radius, randomY+radius);
            for (HashMap ball : ballsList) {
                int currentBallX = (int) ball.get("posX");
                int currentBallY = (int) ball.get("posY");
                RectF currentBallRect = new RectF(currentBallX, currentBallY, currentBallX+radius, currentBallY+radius);
                if(RectF.intersects(currentBallRect, newRect)){
                    canAddHere = false;
                }
            }
        }
        ballFeatures.put("R", random.nextInt(255));
        ballFeatures.put("G", random.nextInt(255));
        ballFeatures.put("B", random.nextInt(255));
        ballFeatures.put("posX", randomX);
        ballFeatures.put("posY", randomY);
        ballFeatures.put("speedX", initSpeedX);
        ballFeatures.put("speedY", initSpeedY);
        ballsList.add(ballFeatures);
    }

    @Override
    protected void onDraw(Canvas canvas){
        for (HashMap ball : ballsList) {
            int posX = (int) ball.get("posX");
            int posY = (int) ball.get("posY");
            int R = (int) ball.get("R");
            int G = (int) ball.get("G");
            int B = (int) ball.get("B");
            paint.setARGB(200, R, G, B);
            rectF.set(posX, posY, posX+radius, posY+radius);
            canvas.drawOval(rectF, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(70);
            //draw number of currently displayed balls
            canvas.drawText(String.valueOf(ballsList.size()), 30, 70, paint);
        }
        update();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_UP){
            addNewBall();
        }
        return true;
    }

    public void update() {
        for (HashMap ball : ballsList) {
            bounceWithWalls(ball);
        }
    }

    private void bounceWithWalls(HashMap ball) {
        int posX = (int) ball.get("posX");
        int posY = (int) ball.get("posY");
        int speedX = (int) ball.get("speedX");
        int speedY = (int) ball.get("speedY");

        //change direction of movement
        if (posX + radius > maxX + 50) {
            ball.put("speedX", speedX*=-1);
            ball.put("posX", maxX - radius + 50);
        } else if (posX < -50) {
            ball.put("speedX", speedX*=-1);
            ball.put("posX", -50);
        }
        if (posY + radius > maxY + 50) {
            ball.put("speedY", speedY*=-1);
            ball.put("posY", maxY - radius + 50);
        } else if (posY < -50) {
            ball.put("speedY", speedY *= -1);
            ball.put("posY", -50);
        }
        ball.put("posX", posX+speedX);
        ball.put("posY", posY+speedY);
    }

    // vertcial/horizontal
    @Override
    public void onSizeChanged(int width, int height, int oldW, int oldH){
        maxX = width - 1;
        maxY = height - 1;
    }
}
